package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Listing_02_08_MovieParagraphs2 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_08_MovieParagraphs2.pdf";

    protected PdfFont bold;
    protected PdfFont boldItalic;
    protected PdfFont italic;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_08_MovieParagraphs2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        boldItalic = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLDOBLIQUE);
        italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            // Create a paragraph with the title
            Paragraph title = new Paragraph();
            for (Text text : PojoToElementFactory.getMovieTitlePhrase(movie, bold)) {
                title.add(text);
            }
            title.setHorizontalAlignment(HorizontalAlignment.LEFT);
            doc.add(title);
            // Add the original title next to it using a dirty hack
            if (movie.getOriginalTitle() != null) {
                Paragraph dummy = new Paragraph("\u00a0").setFont(normal);
                dummy.setFixedLeading(-18);
                doc.add(dummy);
                Paragraph originalTitle = new Paragraph();
                for (Text text : PojoToElementFactory.getOriginalTitlePhrase(movie, italic, normal)) {
                    originalTitle.add(text);
                }
                originalTitle.setTextAlignment(TextAlignment.RIGHT);
                doc.add(originalTitle);
            }
            // Info about the director
            Paragraph director;
            float indent = 20;
            // Loop over the directors
            for (Director pojo : movie.getDirectors()) {
                director = new Paragraph();
                for (Text text : PojoToElementFactory.getDirectorPhrase(pojo, bold, normal)) {
                    director.add(text);
                }
                director.setMarginLeft(indent);
                doc.add(director);
                indent += 20;
            }
            // Info about the country
            Paragraph country;
            indent = 20;
            // Loop over the countries
            for (Country pojo : movie.getCountries()) {
                country = new Paragraph();
                for (Text text : PojoToElementFactory.getCountryPhrase(pojo, normal)) {
                    country.add(text);
                }
                country.setTextAlignment(TextAlignment.RIGHT);
                country.setMarginRight(indent);
                doc.add(country);
                indent += 20;
            }
            // Extra info about the movie
            Paragraph info = new Paragraph();
            for (Text text : createYearAndDuration(movie)) {
                info.add(text);
            }
            info.setTextAlignment(TextAlignment.CENTER);
            info.setMarginTop(36);
            doc.add(info);
        }

        doc.close();
    }

    public List<Text> createYearAndDuration(Movie movie) {
        List<Text> info = new ArrayList<>();
        info.add(new Text("Year: ").setFont(boldItalic));
        info.add(new Text(String.valueOf(movie.getYear())).setFont(normal));
        info.add(new Text(" Duration: ").setFont(boldItalic));
        info.add(new Text(String.valueOf(movie.getDuration())).setFont(normal));
        info.add(new Text(" minutes").setFont(normal));
        return info;
    }
}
