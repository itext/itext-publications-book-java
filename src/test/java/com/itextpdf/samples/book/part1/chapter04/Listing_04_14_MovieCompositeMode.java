/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_04_14_MovieCompositeMode extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter04/Listing_04_14_MovieCompositeRole.pdf";
    public static final String RESOURCE = "./src/test/resources/img/posters/%s.jpg";

    protected PdfFont normal;
    protected PdfFont bold;
    protected PdfFont italic;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_14_MovieCompositeMode().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        normal = PdfFontFactory.createFont(FontConstants.HELVETICA); // 12
        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD); // 12
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE); // 12

        doc.add(new Paragraph("Movies:"));
        java.util.List<Movie> movies = PojoFactory.getMovies(connection);
        List list;
        Cell cell;
        for (Movie movie : movies) {
            // a table with two columns
            Table table = new Table(new float[]{1, 7});
            table.setWidthPercent(100);
            table.setMarginTop(5);
            // a cell with an image
            cell = new Cell().add(new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb()))).setAutoScaleWidth(true));
            cell.setBorder(Border.NO_BORDER);
            table.addCell(cell);
            cell = new Cell();
            // a cell with paragraphs and lists
            Paragraph p = new Paragraph(movie.getTitle()).setFont(bold);
            p.setTextAlignment(TextAlignment.CENTER);
            p.setMarginTop(5);
            p.setMarginBottom(5);
            cell.add(p);
            cell.setBorder(Border.NO_BORDER);
            if (movie.getOriginalTitle() != null) {
                p = new Paragraph(movie.getOriginalTitle()).setFont(italic);
                p.setTextAlignment(TextAlignment.RIGHT);
                cell.add(p);
            }
            list = PojoToElementFactory.getDirectorList(movie);
            list.setMarginLeft(30);
            cell.add(list);
            p = new Paragraph(
                    String.format("Year: %d", movie.getYear())).setFont(normal);
            p.setMarginLeft(15);
            p.setFixedLeading(24);
            cell.add(p);
            p = new Paragraph(
                    String.format("Run length: %d", movie.getDuration())).setFont(normal);
            p.setFixedLeading(14);
            p.setMarginLeft(30);
            cell.add(p);
            list = PojoToElementFactory.getCountryList(movie);
            list.setMarginLeft(40);
            cell.add(list);
            table.addCell(cell.setKeepTogether(true));
            // every movie corresponds with one table
            doc.add(table);
            // but the result looks like one big table
        }
        doc.close();
    }
}

