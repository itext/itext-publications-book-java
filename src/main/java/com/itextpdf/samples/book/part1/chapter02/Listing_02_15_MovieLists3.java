/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.properties.ListNumberingType;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listing_02_15_MovieLists3 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_15_MovieLists3.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_15_MovieLists3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
                        + "FROM film_country c, film_movie_country mc "
                        + "WHERE c.id = mc.country_id "
                        + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // Create a list for the countries
        List list = new List(ListNumberingType.ROMAN_UPPER);
        // Loop over the countries
        while (rs.next()) {
            // Create a list item for the country
            ListItem item = new ListItem(
                    String.format("%s: %d movies", rs.getString("country"), rs.getInt("c")));
            // Create a list for the movies
            List movielist = new List(ListNumberingType.GREEK_LOWER);
            // Loop over the movies
            for (Movie movie :
                    PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                ListItem movieitem = new ListItem(movie.getMovieTitle());
                // Create a list for the directors
                List directorlist = new List(ListNumberingType.ZAPF_DINGBATS_1);
                // Loop over the directors
                for (Director director : movie.getDirectors()) {
                    directorlist.add(String.format("%s, %s",
                            director.getName(), director.getGivenName()));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        doc.add(list);
        doc.close();
        stm.close();
        connection.close();
    }
}
