package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.Property;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listing_02_23_MovieLinks2 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_23_MovieLinks2.pdf";
    public static final String MOVIE_LINKS1 = "Listing_02_22_MovieLinks1.pdf";

    protected PdfFont bold;
    protected PdfFont italic;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_23_MovieLinks2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String destination) throws IOException, SQLException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destination));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

        Paragraph p = new Paragraph();
        Text top = new Text("Country List").setFont(bold);
        top.setProperty(Property.DESTINATION, "top");
        p.add(top);
        doc.add(p);
        // create an external link
        Link imdb = new Link("Internet Movie Database", PdfAction.createURI("http://www.imdb.com/"));
        imdb.setFont(italic);
        p = new Paragraph("Click on a country, and you'll get a list of movies, "
                + "containing links to the ");
        p.add(imdb);
        p.add(".");
        doc.add(p);
        // Create a remote goto
        p = new Paragraph("This list can be found in a ");
        Link page1 = new Link("separate document", PdfAction.createGoToR(MOVIE_LINKS1, 1));
        p.add(page1);
        p.add(".");
        doc.add(p);
        doc.add(new Paragraph("\n"));

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
                        + "FROM film_country c, film_movie_country mc "
                        + "WHERE c.id = mc.country_id "
                        + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // loop over the results
        while (rs.next()) {
            // add country with remote goto
            Paragraph country = new Paragraph(rs.getString("country"));
            country.add(": ");
            Link link = new Link(String.format("%d movies", rs.getInt("c")),
                    PdfAction.createGoToR(MOVIE_LINKS1, rs.getString("country_id")));
            country.add(link);
            doc.add(country);
        }
        doc.add(new Paragraph("\n"));
        // Create local goto to top
        p = new Paragraph("Go to ");
        Link topLink = new Link("top", PdfAction.createGoTo("top"));
        p.add(topLink);
        p.add(".");
        doc.add(p);
        doc.close();
        stm.close();
        connection.close();
    }
}
