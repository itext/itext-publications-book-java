/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.junit.experimental.categories.Category;

import java.util.List;

@Category(SampleTest.class)
public class Listing_03_19_MovieColumns4 extends Listing_03_16_MovieColumns1 {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_19_MovieColumns4.pdf";

    public static final Rectangle[] COLUMNS = {
            new Rectangle(36, 666, 260, 136), new Rectangle(110, 580, 190, 90),
            new Rectangle(36, 480, 260, 100) , new Rectangle(36, 390, 190, 90), new Rectangle(36, 36, 260, 350),
            new Rectangle(299, 480, 260, 322), new Rectangle(373, 390, 190, 90),
            new Rectangle(299, 250, 260, 136), new Rectangle(299, 150, 190, 100), new Rectangle(299, 36, 260, 110)
    };

    public static void main(String[] args) throws Exception {
        new Listing_03_19_MovieColumns4().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        normal = PdfFontFactory.createFont(FontConstants.HELVETICA);
        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
        boldItalic = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);

        doc.setProperty(Property.FONT, normal);

        doc.setRenderer(new ColumnDocumentRenderer(doc, COLUMNS));
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new IEventHandler() {
            @Override
            public void handleEvent(Event event) {
                drawRectangles(new PdfCanvas(((PdfDocumentEvent)event).getPage()));
            }
        });

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            doc.add(createMovieInformation(movie));
        }

        doc.close();
    }

    public void drawRectangles(PdfCanvas canvas) {
        canvas.saveState();
        canvas.setFillColorGray(0.9f);
        canvas.rectangle(33, 592, 72, 72);
        canvas.rectangle(263, 406, 72, 72);
        canvas.rectangle(491, 168, 72, 72);
        canvas.fillStroke();
        canvas.restoreState();
    }

    @Override
    public Paragraph createMovieInformation(Movie movie) {
        return super.createMovieInformation(movie).
                setPaddingLeft(0).
                setFirstLineIndent(0);
    }
}
