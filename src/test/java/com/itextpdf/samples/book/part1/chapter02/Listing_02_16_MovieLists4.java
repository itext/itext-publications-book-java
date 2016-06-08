/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class Listing_02_16_MovieLists4 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_16_MovieLists4.pdf";

    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_16_MovieLists4().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
                        + "FROM film_country c, film_movie_country mc "
                        + "WHERE c.id = mc.country_id "
                        + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // Create a list for the countries
        List list = new List(ListNumberingType.DECIMAL);
        list.setItemStartIndex(9);
        // Loop over the countries
        while (rs.next()) {
            // Create a list item for a country
            ListItem item = new ListItem(String.format("%s: %d movies",
                    rs.getString("country"), rs.getInt("c")));
            // Create a list for the movies
            List movielist = new List();
            movielist.setListSymbol(new Text("Movie: ").setFont(bold));
            for (Movie movie : PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                ListItem movieitem = new ListItem(movie.getMovieTitle());
                // Create a list for the directors
                Text listSymbol = new Text(String.valueOf((char)42) + " ").setFont(PdfFontFactory.createFont(FontConstants.ZAPFDINGBATS));
                List directorlist = new List().setListSymbol(listSymbol);
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
