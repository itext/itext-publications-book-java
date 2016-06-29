/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.lowagie.filmfestival.Screening;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Category(SampleTest.class)
public class Listing_04_18_HeaderFooter1 extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter04/Listing_04_18_HeaderFooter1.pdf";
    public static final String RESOURCE = "./src/test/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_18_HeaderFooter1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        List<Date> days = PojoFactory.getDays(connection);
        int d = 1;
        for (Date day : days) {
            if (1 != d) {
                doc.add(new AreaBreak());
            }
            doc.add(getTable(connection, day));
            d++;
        }

        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection, Date day) throws UnsupportedEncodingException, SQLException {
        // Create a table with 7 columns
        Table table = new Table(new float[]{2, 1, 2, 5, 1, 3, 2});
        table.setWidthPercent(100);
        // Add the first header row
        Cell cell = new Cell(1, 7).add(new Paragraph(day.toString()).setFontColor(Color.WHITE));
        cell.setBackgroundColor(Color.BLACK);
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cell);

        // set headers and footers
        table.addHeaderCell(new Cell().add("Location").setBackgroundColor(Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add("Time").setBackgroundColor(Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add("Run Length").setBackgroundColor(Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add("Title").setBackgroundColor(Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add("Year").setBackgroundColor(Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add("Directors").setBackgroundColor(Color.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add("Countries").setBackgroundColor(Color.LIGHT_GRAY));

        table.addFooterCell(new Cell().add("Location").setBackgroundColor(Color.LIGHT_GRAY));
        table.addFooterCell(new Cell().add("Time").setBackgroundColor(Color.LIGHT_GRAY));
        table.addFooterCell(new Cell().add("Run Length").setBackgroundColor(Color.LIGHT_GRAY));
        table.addFooterCell(new Cell().add("Title").setBackgroundColor(Color.LIGHT_GRAY));
        table.addFooterCell(new Cell().add("Year").setBackgroundColor(Color.LIGHT_GRAY));
        table.addFooterCell(new Cell().add("Directors").setBackgroundColor(Color.LIGHT_GRAY));
        table.addFooterCell(new Cell().add("Countries").setBackgroundColor(Color.LIGHT_GRAY));

        // Now let's loop over the screenings
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            table.addCell(String.format("%d '", movie.getDuration()));
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
            cell = new Cell();
            cell.add(PojoToElementFactory.getDirectorList(movie));
            table.addCell(cell);
            cell = new Cell();
            cell.add(PojoToElementFactory.getCountryList(movie));
            table.addCell(cell);
        }
        return table;
    }
}

