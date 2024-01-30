/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

public class Listing_06_14_TwoPasses {
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_14_TwoPasses.pdf";

    protected PdfFont bold;
    protected PdfFont italic;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_14_TwoPasses().manipulatePdf(DEST);
    }

    /**
     * Create a header table with page X of Y
     *
     * @param x the page number
     * @param y the total number of pages
     * @return a table that can be used as header
     */
    public static Table getHeaderTable(int x, int y) {
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setWidth(523);
        Style style = new Style()
                .setBorder(Border.NO_BORDER)
                .setHeight(20)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorderBottom(new SolidBorder(1));
        table.setBorder(Border.NO_BORDER);
        table.addCell(new Cell()
                .add(new Paragraph("FOOBAR FILMFESTIVAL")).addStyle(style));
        table.addCell(new Cell()
                .add(new Paragraph(String.format("Page %d of %d", x, y))).addStyle(style));
        return table;
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // FIRST PASS, CREATE THE PDF WITHOUT HEADER

        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));
        doc.setMargins(54, 36, 36, 36);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

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
                    doc.add(
                            new Paragraph(movie.getOriginalTitle()).setFont(italic));
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

        // SECOND PASS, ADD THE HEADER

        // Create a pdf document
        pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())),
                new PdfWriter(DEST));

        // Loop over the pages and add a header to each page
        int n = pdfDoc.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            new Canvas(new PdfCanvas(pdfDoc.getPage(i)),
                    new Rectangle(36, 803, 523, 30)).add(getHeaderTable(i, n));
        }

        // Close the pdf document
        pdfDoc.close();
    }
}
