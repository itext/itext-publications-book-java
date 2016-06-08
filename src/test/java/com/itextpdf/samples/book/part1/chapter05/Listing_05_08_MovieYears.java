/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.LinkRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_05_08_MovieYears extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_08_MovieYears.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_08_MovieYears().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD, PdfEncodings.WINANSI);
        PdfFont italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE, PdfEncodings.WINANSI);

        Paragraph p;
        Text text;
        Map<String, Integer> years = new TreeMap<>();

        Set<Movie> movies =
                new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
        movies.addAll(PojoFactory.getMovies(connection));
        for (Movie movie : movies) {
            p = new Paragraph().setFixedLeading(22);
            p.setNextRenderer(new LinedParagraphRenderer(p));
            text = new Text(String.format("%d ", movie.getYear())).setFont(bold);
            text.setNextRenderer(new StripTextRenderer(text));
            p.add(text);
            text = new Text(movie.getMovieTitle());
            p.add(text);
            text = new Text(String.format(" (%d minutes)  ", movie.getDuration())).setFont(italic);
            p.add(text);
            text = new Link("IMDB", PdfAction.createURI("http://www.imdb.com/title/tt" + movie.getImdb()))
                    .setFont(bold).setFontColor(Color.WHITE);
            text.setNextRenderer(new EllipseTextRenderer((Link) text));
            p.add(text);
            doc.add(p);
            // count
            Integer count = years.get(String.valueOf(movie.getYear()));
            if (count == null) {
                years.put(String.valueOf(movie.getYear()), 1);
            } else {
                years.put(String.valueOf(movie.getYear()), count + 1);
            }
        }
        doc.add(new AreaBreak());
        for (Map.Entry<String, Integer> entry : years.entrySet()) {
            p = new Paragraph(String.format("%s: %d movie(s)", entry.getKey(), entry.getValue()));
            doc.add(p);
        }

        doc.close();
        connection.close();
    }

    private class StripTextRenderer extends TextRenderer {
        public StripTextRenderer(Text textElement) {
            super(textElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            Rectangle rect = getOccupiedAreaBBox();
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.rectangle(rect.getLeft() - 1, rect.getBottom() - 5f,
                    rect.getWidth(), rect.getHeight() + 8);
            canvas.rectangle(rect.getLeft(), rect.getBottom() - 2,
                    rect.getWidth() - 2, rect.getHeight() + 2);
            float y1 = rect.getTop() + 0.5f;
            float y2 = rect.getBottom() - 4;
            for (float f = rect.getLeft(); f < rect.getRight() - 4; f += 5) {
                canvas.rectangle(f, y1, 4f, 1.5f);
                canvas.rectangle(f, y2, 4f, 1.5f);
            }
            canvas.eoFill();
            super.draw(drawContext);
        }
    }


    private class EllipseTextRenderer extends LinkRenderer {
        public EllipseTextRenderer(Link textElement) {
            super(textElement);
            setProperty(Property.FONT_COLOR, Color.WHITE);
        }

        @Override
        public void draw(DrawContext drawContext) {
            Rectangle rect = getOccupiedAreaBBox();
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.saveState();
            canvas.setFillColor(new DeviceRgb(0x00, 0x00, 0xFF));
            canvas.ellipse(rect.getLeft() - 3f, rect.getBottom() - 5f,
                    rect.getRight() + 3f, rect.getTop() + 3f);
            canvas.fill();
            canvas.restoreState();
            super.draw(drawContext);
        }
    }


    private class LinedParagraphRenderer extends ParagraphRenderer {
        public LinedParagraphRenderer(Paragraph modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            // bottom
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.moveTo(rect.getLeft(), rect.getTop() + 2);
            canvas.lineTo(rect.getRight(), rect.getTop() + 2);
            // top
            canvas.moveTo(rect.getLeft(), rect.getBottom() - 2);
            canvas.lineTo(rect.getRight(), rect.getBottom() - 2);

            canvas.stroke();
        }
    }
}