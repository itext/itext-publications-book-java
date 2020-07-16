package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listing_02_22_MovieLinks1 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_22_MovieLinks1.pdf";

    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_22_MovieLinks1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String destination) throws IOException, SQLException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destination));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
                        + "FROM film_country c, film_movie_country mc "
                        + "WHERE c.id = mc.country_id "
                        + "GROUP BY mc.country_id, country ORDER BY c DESC");
        Link imdb;
        // loop over the countries
        while (rs.next()) {
            Paragraph anchor = new Paragraph(rs.getString("country"));
            anchor.setFont(bold);
            anchor.setProperty(Property.DESTINATION, rs.getString("country_id"));

            doc.add(anchor);
            // loop over the movies
            for(Movie movie : PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                // the movie title will be an external link
                imdb = new Link(movie.getMovieTitle(),
                        PdfAction.createURI(String.format("http://www.imdb.com/title/tt%s/", movie.getImdb())));
                doc.add(new Paragraph().add(imdb));
            }
            doc.add(new AreaBreak());
        }
        // Create an internal link to the first page
        Link toUS = new Link("Go back to the first page.", PdfAction.createGoTo("US"));
        doc.add(new Paragraph(toUS));
        doc.close();
        stm.close();
        connection.close();
    }
}
