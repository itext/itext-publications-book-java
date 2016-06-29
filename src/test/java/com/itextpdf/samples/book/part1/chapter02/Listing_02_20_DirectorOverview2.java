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
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.*;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


@Category(SampleTest.class)
public class Listing_02_20_DirectorOverview2 extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter02/Listing_02_20_DirectorOverview2.pdf";

    protected PdfFont bold;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_20_DirectorOverview2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        normal = PdfFontFactory.createFont(FontConstants.HELVETICA);

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
                        + "FROM film_director d, film_movie_director md WHERE d.id = md.director_id "
                        + "GROUP BY d.id, d.name, d.given_name ORDER BY c DESC");
        Director director;
        // loop over the directors
        while (rs.next()) {
            // create a paragraph for the director
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph();
            for (Text text : PojoToElementFactory.getDirectorPhrase(director, bold, normal)) {
                p.add(text);
            }
            p.addTabStops(new TabStop(750, TabAlignment.RIGHT, new DottedLine()));
            p.add(new Tab());
            p.add(String.format("movies: %d", rs.getInt("c")));
            doc.add(p);
            // Creates a list
            List list = new List(ListNumberingType.DECIMAL);
            list.setMarginLeft(36);
            list.setMarginRight(36);
            // Gets the movies of the current director
            Set<Movie> movies = new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            ListItem movieitem;
            // loops over the movies
            for (Movie movie : movies) {
                // creates a list item with a movie title
                Paragraph paragraphtoAdd = new Paragraph();
                java.util.List<TabStop> tabStops = new ArrayList<>();
                tabStops.add(new TabStop(750, TabAlignment.RIGHT));
                tabStops.add(new TabStop(850, TabAlignment.RIGHT));
                paragraphtoAdd.addTabStops(tabStops);
                paragraphtoAdd.add(movie.getMovieTitle());
                paragraphtoAdd.add(new Tab());
                paragraphtoAdd.add(new Text(String.valueOf(movie.getYear())));
                if (movie.getYear() > 1999) {
                    Tab tab = new Tab();
                    tab.setNextRenderer(new PositionedArrowTabRenderer(tab, doc, false));
                    paragraphtoAdd.add(tab);
                }
                movieitem = (ListItem) new ListItem().add(paragraphtoAdd);
                list.add(movieitem);
            }
            // add the list to the document
            doc.add(list);
        }
        doc.close();
        stm.close();
        connection.close();
    }
}
