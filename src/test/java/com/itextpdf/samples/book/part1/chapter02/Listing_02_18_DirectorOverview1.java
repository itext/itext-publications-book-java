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
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.Text;
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
import java.util.Set;
import java.util.TreeSet;

@Category(SampleTest.class)
public class Listing_02_18_DirectorOverview1 extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter02/Listing_02_18_DirectorOverview1.pdf";

    protected PdfFont bold;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_18_DirectorOverview1().manipulatePdf(DEST);
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
                        + "GROUP BY d.id, d.name, d.given_name ORDER BY name");
        Director director;
        // looping over the directors
        while (rs.next()) {
            // get the director object and use it in a Paragraph
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph();
            for (Text text : PojoToElementFactory.getDirectorPhrase(director, bold, normal)) {
                p.add(text);
            }
            // if there are more than 2 movies for this director
            if (rs.getInt("c") > 2) {
                // add the paragraph with the arrow to the document
                Tab tab = new Tab();
                tab.setNextRenderer(new PositionedArrowTabRenderer(tab, doc, true));
                p.add(tab);
            }
            doc.add(new LineSeparator(new SolidLine(1)));
            doc.add(p);

            // Get the movies of the directory, ordered by year
            Set<Movie> movies = new TreeSet<>(
                    new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            // loop over the movies
            for (Movie movie : movies) {
                p = new Paragraph(movie.getMovieTitle());
                p.add(": ");
                p.add(new Text(String.valueOf(movie.getYear())));
                if (movie.getYear() > 1999) {
                    Tab tab = new Tab();
                    tab.setNextRenderer(new PositionedArrowTabRenderer(tab, doc, false));
                    p.add(tab);
                }
                doc.add(p);
            }
            // add a star separator after the director info is added
            doc.add(new StarSeparator());
        }
        doc.close();
        stm.close();
        connection.close();
    }
}
