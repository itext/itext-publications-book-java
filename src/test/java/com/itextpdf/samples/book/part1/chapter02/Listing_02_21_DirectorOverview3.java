/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.*;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Ignore
// TODO DEVSIX-584
@Category(SampleTest.class)
public class Listing_02_21_DirectorOverview3 extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter02/Listing_02_21_DirectorOverview3.pdf";

    protected PdfFont bold;
    protected PdfFont normal;


    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_21_DirectorOverview3().manipulatePdf(DEST);
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
                        + "FROM film_director d, film_movie_director md "
                        + "WHERE d.id = md.director_id "
                        + "GROUP BY d.id, d.name, d.given_name ORDER BY c DESC");
        Director director;
        // creates line separators
        List<TabStop> tabStops = new ArrayList<>();
        tabStops.add(new TabStop(200, TabAlignment.LEFT));
        tabStops.add(new TabStop(350, TabAlignment.LEFT));
        tabStops.add(new TabStop(450, TabAlignment.LEFT, new DottedLine()));
        // loops over the directors
        while (rs.next()) {
            // creates a paragraph with the director name
            Div div = new Div();
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph();
            for (Text text : PojoToElementFactory.getDirectorPhrase(director, bold, normal)) {
                p.add(text);
            }
            // adds a separator
            SolidLine line = new SolidLine(.5f);
            line.setColor(Color.BLUE);
            p.addTabStops(new TabStop(1000, TabAlignment.RIGHT, line));
            p.add(new Tab());
            // adds more info about the director
            p.add(String.format("movies: %d", rs.getInt("c")));
            div.add(p);
            // adds a separator
            div.add(new LineSeparator(new SolidLine(1)));
            // adds the paragraph to the document
            doc.add(div);
            // gets all the movies of the current director
            Set<Movie> movies
                    = new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            // loop over the movies
            for (Movie movie : movies) {
                // create a Paragraph with the movie title
                p = new Paragraph();
                p.addTabStops(tabStops);

                p.add(movie.getMovieTitle());
                // insert a tab
                p.add(new Tab());
                // add the original title
                if (movie.getOriginalTitle() != null) {
                    p.add(new Text(movie.getOriginalTitle()));
                }
                // insert a tab
                p.add(new Tab());
                // add the run length of the movie
                p.add(new Text(String.format("%d minutes", movie.getDuration())));
                // insert a tab
                p.add(new Tab());
                // add the production year of the movie
                p.add(new Text(String.valueOf(movie.getYear())));
                // add the paragraph to the document
                doc.add(p);
            }
        }
        doc.close();
        stm.close();
        connection.close();
    }
}
