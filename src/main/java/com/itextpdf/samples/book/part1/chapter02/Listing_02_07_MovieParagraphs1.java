/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
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

public class Listing_02_07_MovieParagraphs1 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_07_MovieParagraphs1.pdf";

    protected PdfFont bold;
    protected PdfFont boldItalic;
    protected PdfFont italic;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_07_MovieParagraphs1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        createFonts();
        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            Paragraph p = createMovieInformation(movie);
            doc.add(p.setMarginLeft(18)
                    .setMarginTop(18)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFirstLineIndent(-18));
        }
        doc.close();
    }

    /**
     * Creates a Paragraph containing information about a movie.
     *
     * @param movie the movie for which you want to create a Paragraph
     */
    public Paragraph createMovieInformation(Movie movie) {
        Paragraph p = new Paragraph();
        p.setFont(normal);
        p.add(new Text("Title: ").setFont(boldItalic));
        for (Text text : PojoToElementFactory.getMovieTitlePhrase(movie, normal)) {
            p.add(text);
        }
        p.add(" ");
        if (movie.getOriginalTitle() != null) {
            p.add(new Text("Original title: ").setFont(boldItalic));
            for (Text text : PojoToElementFactory.getOriginalTitlePhrase(movie, italic, normal)) {
                p.add(text);
            }
            p.add(" ");
        }
        p.add(new Text("Country: ").setFont(boldItalic));
        for (Country country : movie.getCountries()) {
            for (Text text : PojoToElementFactory.getCountryPhrase(country, normal)) {
                p.add(text);
            }
            p.add(" ");
        }
        p.add(new Text("Director: ").setFont(boldItalic));
        for (Director director : movie.getDirectors()) {
            for (Text text : PojoToElementFactory.getDirectorPhrase(director, bold, normal)) {
                p.add(text);
            }
            p.add(" ");
        }
        for (Text text : createYearAndDuration(movie)) {
            p.add(text);
        }
        return p;
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

    protected void createFonts() throws IOException {
        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        boldItalic = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLDOBLIQUE);
        italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }
}
