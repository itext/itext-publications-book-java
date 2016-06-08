/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.junit.experimental.categories.Category;

import java.io.FileOutputStream;
import java.util.List;

@Category(SampleTest.class)
public class Listing_03_16_MovieColumns1 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_16_MovieColumns1.pdf";

    protected PdfFont normal, bold, italic, boldItalic;

    public static void main(String[] args) throws Exception {
        new Listing_03_16_MovieColumns1().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        normal = PdfFontFactory.createFont(FontConstants.HELVETICA);
        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
        boldItalic = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);

        doc.setProperty(Property.FONT, normal);

        Rectangle[] columns = {new Rectangle(36, 36, 260, 770), new Rectangle(299, 36, 260, 770)};
        doc.setRenderer(new ColumnDocumentRenderer(doc, columns));

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            doc.add(createMovieInformation(movie));
            doc.add(new LineSeparator(new SolidLine(0.3f)));
        }

        doc.close();
    }

    public Paragraph createMovieInformation(Movie movie) {
        Paragraph p = new Paragraph().
                setTextAlignment(TextAlignment.JUSTIFIED).
                setPaddingLeft(27).
                setFirstLineIndent(-27).
                setMultipliedLeading(1.2f);

        p.add(new Text("Title: ").setFont(boldItalic));
        p.add(new Text(movie.getMovieTitle()).setFont(normal));
        p.add(" ");
        if (movie.getOriginalTitle() != null) {
            p.add(new Text("Original title: ").setFont(boldItalic));
            p.add(new Text(movie.getOriginalTitle() != null ? movie.getOriginalTitle() : "").setFont(italic));
            p.add(" ");
        }
        p.add(new Text("Country: ").setFont(boldItalic));
        for (Country country : movie.getCountries()) {
            p.add(country.getCountry());
            p.add(" ");
        }
        p.add(new Text("Director: ").setFont(boldItalic));
        for (Director director : movie.getDirectors()) {
            p.add(new Text(director.getName() + ", ").setFont(bold));
            p.add(new Text(director.getGivenName() + " "));
        }
        p.add(new Text("Year: ").setFont(boldItalic));
        p.add(new Text(String.valueOf(movie.getYear())));
        p.add(new Text(" Duration: ").setFont(boldItalic));
        p.add(new Text(String.valueOf(movie.getDuration())));
        p.add(new Text(" minutes"));
        return p;
    }

}
