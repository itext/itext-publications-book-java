/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
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
public class Listing_06_15_StampStationery extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part2/chapter06/Listing_06_15_StampStationery.pdf";
    public static final String ORIGINAL =
            "./target/test/resources/book/part2/chapter06/Listing_06_15_StampStationery_original.pdf";
    public static final String STATIONERY_WATERMARK =
            "./src/test/resources/book/part2/chapter06/cmp_Listing_06_08_Stationery_watermark.pdf";

    protected PdfFont bold;
    protected PdfFont italic;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_15_StampStationery().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // new Listing_06_08_Stationery().createStationery(Listing_06_08_Stationery.SOURCE);
        Listing_06_15_StampStationery stationary = new Listing_06_15_StampStationery();
        stationary.createPdf(ORIGINAL);
        stationary.manipulatePdf2(ORIGINAL, STATIONERY_WATERMARK, DEST);
    }

    public void manipulatePdf2(String src, String stationery, String dest) throws IOException {
        PdfReader stationeryReader = new PdfReader(stationery);
        PdfDocument stationeryDoc = new PdfDocument(stationeryReader);

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        int n = pdfDoc.getNumberOfPages();

        PdfPage page;
        PdfFormXObject watermark = stationeryDoc.getFirstPage().copyAsFormXObject(pdfDoc);
        for (int i = 1; i <= n; i++) {
            page = pdfDoc.getPage(i);
            new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc).addXObject(watermark, 0, 0);
        }

        pdfDoc.close();
        stationeryDoc.close();
    }

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws IOException
     * @throws SQLException
     */
    public void createPdf(String filename) throws SQLException, IOException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));
        doc.setMargins(72, 36, 36, 36);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
        normal = PdfFontFactory.createFont(FontConstants.HELVETICA);

        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT country, id FROM film_country ORDER BY country");
        while (rs.next()) {
            doc.add(new Paragraph(rs.getString("country")).setFont(bold));
            doc.add(new Paragraph("\n"));
            Set<Movie> movies = new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
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
        connection.close();
    }
}
