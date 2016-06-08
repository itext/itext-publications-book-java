/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_04_06_MovieTextMode extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter04/Listing_04_06_MovieTextMode.pdf";

    protected PdfFont normal;
    protected PdfFont bold;
    protected PdfFont italic;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_06_MovieTextMode().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        normal = PdfFontFactory.createFont(FontConstants.HELVETICA); // 12
        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD); // 12
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE); // 12

        doc.add(new Paragraph("Movies:"));
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            Table table = new Table(new float[]{1, 4});
            table.setWidthPercent(100);
            Cell cell;
            cell = new Cell(1, 2).add(new Paragraph(movie.getTitle()).setFont(bold));
            cell.setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            if (movie.getOriginalTitle() != null) {
                Paragraph p = new Paragraph();
                for (Text text : PojoToElementFactory.getOriginalTitlePhrase(movie, italic, normal)) {
                    p.add(text);
                }
                cell = new Cell(1, 2).add(p);
                cell.setTextAlignment(TextAlignment.RIGHT);
                table.addCell(cell);
            }
            List<Director> directors = movie.getDirectors();
            cell = new Cell(directors.size(), 1).add("Directors:");
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(cell);
            int count = 0;
            for (Director pojo : directors) {
                Paragraph p = new Paragraph();
                for (Text text : PojoToElementFactory.getDirectorPhrase(pojo, bold, normal)) {
                    p.add(text);
                }
                cell = new Cell().add(p);
                cell.setMarginLeft(10 * count++);
                table.addCell(cell);
            }
            table.addCell(new Cell().add("Year:")
                    .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(String.valueOf(movie.getYear()))
                    .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add("Run length:")
                    .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(String.valueOf(movie.getDuration()))
                    .setTextAlignment(TextAlignment.RIGHT));
            List<Country> countries = movie.getCountries();
            cell = new Cell(countries.size(), 1).add("Countries:");
            cell.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table.addCell(cell);
            for (Country country : countries) {
                table.addCell(new Cell().add(country.getCountry())
                        .setTextAlignment(TextAlignment.CENTER));
            }
            doc.add(table);
        }
        doc.close();
    }
}
