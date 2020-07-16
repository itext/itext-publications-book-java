package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.lowagie.filmfestival.Screening;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Listing_04_19_HeaderFooter2 {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_19_HeaderFooter2.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_19_HeaderFooter2().manipulatePdf(DEST);
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
            Table table = getTable(connection, day);
            doc.add(table);
            d++;
        }

        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection, Date day) throws UnsupportedEncodingException, SQLException {
        // Create a table with 7 columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2, 5, 1, 3, 2}));
        // Add the first header row
        Cell cell = new Cell(1, 7).add(new Paragraph(day.toString()).setFontColor(ColorConstants.WHITE));
        cell.setBackgroundColor(ColorConstants.BLACK);
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cell);

        // set headers and footers
        table.addHeaderCell(new Cell().add(new Paragraph("Location")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Time")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Run Length")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Title")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Year")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Directors")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Countries")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addFooterCell(new Cell().add(new Paragraph("Location")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addFooterCell(new Cell().add(new Paragraph("Time")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addFooterCell(new Cell().add(new Paragraph("Run Length")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addFooterCell(new Cell().add(new Paragraph("Title")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addFooterCell(new Cell().add(new Paragraph("Year")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addFooterCell(new Cell().add(new Paragraph("Directors")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addFooterCell(new Cell().add(new Paragraph("Countries")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

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
