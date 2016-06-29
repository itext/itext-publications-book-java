/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

@Category(SampleTest.class)
public class Listing_06_08_Stationery extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter06/Listing_06_08_Stationery.pdf";
    public static final String SOURCE
            = "./target/test/resources/book/part2/chapter06/Listing_06_08_Stationery_watermark.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/loa.jpg";

    protected PdfFont bold;
    protected PdfFont italic;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_08_Stationery().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        // First create the original file
        createStationery(SOURCE);

        // Initialize result document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));

        StationeryEventHandler eventHandler = new StationeryEventHandler();
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, eventHandler);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
        normal = PdfFontFactory.createFont(FontConstants.HELVETICA);

        doc.setMargins(72, 36, 36, 36);
        // useStationary(writer);
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT country, id FROM film_country ORDER BY country");
        while (rs.next()) {
            doc.add(new Paragraph(rs.getString("country")).setFont(bold));
            doc.add(new Paragraph("\n"));
            Set<Movie> movies =
                    new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getString("id")));
            for (Movie movie : movies) {
                doc.add(new Paragraph(movie.getMovieTitle()).setFont(bold));
                if (movie.getOriginalTitle() != null)
                    doc.add(new Paragraph(movie.getOriginalTitle()).setFont(italic));
                doc.add(new Paragraph(
                        String.format("Year: %d; run length: %d minutes",
                                movie.getYear(), movie.getDuration())).setFont(normal));
                doc.add(PojoToElementFactory.getDirectorList(movie));
            }
            if (!rs.isLast()) {
                doc.add(new AreaBreak());
            }
        }
        doc.close();

        // Close the database connection
        connection.close();
    }

    public void createStationery(String filename) throws IOException {
        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        Table table = new Table(1).setWidthPercent(80).setHorizontalAlignment(HorizontalAlignment.CENTER);
        Style style = new Style().setTextAlignment(TextAlignment.CENTER);
        table.addCell(new Cell()
                .addStyle(style)
                .add(new Paragraph("FOOBAR FILM FESTIVAL").setFont(bold)));
        doc.add(table);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getLastPage().newContentStreamBefore(),
                pdfDoc.getLastPage().getResources(), pdfDoc);
        new Canvas(canvas, pdfDoc, pdfDoc.getLastPage().getPageSize())
                .setFontColor(new DeviceGray(0.75f))
                .setFontSize(52)
                .setFont(font)
                .showTextAligned(new Paragraph("FOOBAR FILM FESTIVAL"), 297.5f, 421, pdfDoc.getNumberOfPages(),
                        TextAlignment.CENTER, VerticalAlignment.MIDDLE, (float)Math.PI / 4);
        doc.close();
    }


    protected class StationeryEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
            int pageNum = pdfDoc.getPageNumber(((PdfDocumentEvent) event).getPage());

            PdfReader reader = null;
            try {
                reader = new PdfReader(SOURCE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            PdfDocument srcDoc = new PdfDocument(reader);
            PdfFormXObject watermark = null;
            try {
                watermark = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new PdfCanvas(pdfDoc.getPage(pageNum).newContentStreamBefore(),
                    pdfDoc.getPage(pageNum).getResources(), pdfDoc)
                    .addXObject(watermark, 0, 0);
        }
    }
}
