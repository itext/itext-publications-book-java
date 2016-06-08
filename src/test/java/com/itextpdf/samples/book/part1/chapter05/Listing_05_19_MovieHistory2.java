/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.BlockRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import com.lowagie.filmfestival.PojoToElementFactory;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_05_19_MovieHistory2 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter05/Listing_05_19_MovieHistory2.pdf";

    public static final String[] EPOCH =
            {"Forties", "Fifties", "Sixties", "Seventies", "Eighties",
                    "Nineties", "Twenty-first Century"};

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_19_MovieHistory2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        doc.setMargins(54, 36, 54, 36);

        HeadertHandler handler = new HeadertHandler();
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, handler);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);


        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> movies =
                new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
        movies.addAll(PojoFactory.getMovies(connection));
        int epoch = -1;
        int currentYear = 0;
        Div firstTitle = null;
        Div secondTitle = null;
        Div thirdTitle = null;

        PdfOutline rootOutLine = pdfDoc.getOutlines(false);
        PdfOutline firstLevel = null;
        PdfOutline secondLevel = null;
        PdfOutline thirdLevel = null;
        for (Movie movie : movies) {
            // add the chapter if we're in a new epoch
            if (epoch < (movie.getYear() - 1940) / 10) {
                epoch = (movie.getYear() - 1940) / 10;
                if (null != firstTitle){
                    if (null == handler.getHeader()) {
                        handler.setHeader(EPOCH[epoch-1]);
                        doc.add(firstTitle);
                    } else {
                        handler.setHeader(EPOCH[epoch-1]);
                        doc.add(new AreaBreak());
                        doc.add(firstTitle);
                    }
                }
                // chapter
                firstTitle = new Div().add(new Paragraph(EPOCH[epoch]).setFont(font).setFontSize(24).setBold());
                firstTitle.setNextRenderer(new SectionRenderer(firstTitle, 1));
                firstTitle.setProperty(Property.DESTINATION, EPOCH[epoch]);
                firstLevel = rootOutLine.addOutline(EPOCH[epoch]);
                firstLevel.addDestination(PdfDestination.makeDestination(new PdfString(EPOCH[epoch])));
            }
            // switch to a new year
            if (currentYear < movie.getYear()) {
                currentYear = movie.getYear();
                // section
                secondTitle = new Div().add(new Paragraph(
                        String.format("The year %d", movie.getYear())).setFont(font).setFontSize(18));
                secondTitle.setProperty(Property.DESTINATION, String.valueOf(movie.getYear()));
                secondTitle.setNextRenderer(new SectionRenderer(secondTitle, 2));
                secondLevel = firstLevel.addOutline(String.valueOf(movie.getYear()));
                secondLevel.addDestination(PdfDestination.makeDestination(new PdfString(String.valueOf(movie.getYear()))));

                secondTitle.add(new Paragraph(
                        String.format("Movies from the year %d:", movie.getYear())));

                firstTitle.add(secondTitle);

            }
            // subsection
            thirdTitle = new Div().add(new Paragraph(movie.getMovieTitle()).setFont(font).setFontSize(14).setMarginLeft(20));
            thirdTitle.setNextRenderer(new SectionRenderer(thirdTitle, 3));
            thirdTitle.setProperty(Property.DESTINATION, movie.getMovieTitle());
            thirdLevel = secondLevel.addOutline(movie.getMovieTitle());
            thirdLevel.addDestination(PdfDestination.makeDestination(new PdfString(movie.getMovieTitle())));


            thirdTitle.add(new Paragraph("Duration: " + movie.getDuration()).setFont(bold));
            thirdTitle.add(new Paragraph("Director(s):").setFont(bold));
            thirdTitle.add(PojoToElementFactory.getDirectorList(movie));
            thirdTitle.add(new Paragraph("Countries:").setFont(bold));
            thirdTitle.add(PojoToElementFactory.getCountryList(movie));

            secondTitle.add(thirdTitle);
        }
        handler.setHeader(EPOCH[EPOCH.length-1]);
        doc.add(new AreaBreak());
        doc.add(firstTitle);

        doc.close();

        connection.close();
    }


    protected class HeadertHandler implements IEventHandler {
        protected String header;

        public void setHeader(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();

            Rectangle artBox = new Rectangle(36, 54, 523, 734);

            page.setArtBox(artBox);
            PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();

            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            new Canvas(canvas, pdfDoc, artBox)
                    .add(new Paragraph(header).setFixedPosition(36, 800, 150));
            new Canvas(canvas, pdfDoc, artBox)
                    .add(new Paragraph("Movie History").setFixedPosition(470, 800, 150));
            new Canvas(canvas, pdfDoc, artBox)
                    .add(new Paragraph(Integer.toString(pdfDoc.getPageNumber(page))).setFixedPosition(285, 36, 30));

        }

    }


    class SectionRenderer extends BlockRenderer {
        protected int depth;
        protected boolean drawLine = true;


        public SectionRenderer(BlockElement modelElement, int depth) {
            super(modelElement);
            this.depth = depth;
        }


        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle position = getOccupiedAreaBBox();
            if (drawLine) {
                drawContext.getCanvas()
                        .saveState()
                        .moveTo(position.getLeft(), depth > 1 ? position.getBottom() - 5 : position.getBottom() - 3)
                        .lineTo(position.getRight(), depth > 1 ? position.getBottom() - 5 : position.getBottom() - 3)
                        .stroke()
                        .restoreState();
            }
        }

        @Override
        public SectionRenderer getNextRenderer() {
            return new SectionRenderer((BlockElement) modelElement, depth);
        }

        @Override
        protected AbstractRenderer createSplitRenderer(int layoutResult) {
            SectionRenderer splitRenderer = getNextRenderer();
            splitRenderer.parent = parent;
            splitRenderer.modelElement = modelElement;
            splitRenderer.occupiedArea = occupiedArea;
            splitRenderer.isLastRendererForModelElement = false;
            // We should draw line only after the whole section will be rendered
            splitRenderer.drawLine = false;
            return splitRenderer;
        }

        protected AbstractRenderer createOverflowRenderer(int layoutResult) {
            SectionRenderer overflowRenderer = getNextRenderer();
            overflowRenderer.parent = parent;
            overflowRenderer.modelElement = modelElement;
            overflowRenderer.properties = properties;
            return overflowRenderer;
        }
    }
}
