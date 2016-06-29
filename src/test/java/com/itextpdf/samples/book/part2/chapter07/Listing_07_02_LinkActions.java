/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Category(SampleTest.class)
public class Listing_07_02_LinkActions extends GenericTest {
    public static final String SRC = "./src/test/resources/book/part1/chapter02/cmp_Listing_02_22_MovieLinks1.pdf";
    public static final String DEST = "./target/test/resources/book/part2/chapter07/Listing_07_02_LinkActions2.pdf";
    public static final String DEST2 = "./target/test/resources/book/part2/chapter07/Listing_07_02_LinkActions.xml";

    public static void main(String args[]) throws IOException, SQLException, TransformerException, ParserConfigurationException {
        new Listing_07_02_LinkActions().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException, TransformerException, ParserConfigurationException {
        manipulatePdf2(DEST);
        createXml(SRC, DEST2);
    }

    public void manipulatePdf2(String dest) throws FileNotFoundException, SQLException {
        // Open the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Click on a country, and you'll get a list of movies, containing links to the ").
                add(new Link("Internet Movie Database", PdfAction.createURI("http://www.imdb.com"))).
                add(".");
        doc.add(p);

        p = new Paragraph("This list can be found in a ").
                add(new Link("separate document", PdfAction.createGoToR("../../part1/chapter02/Listing_02_22_MovieLinks1.pdf", 1))).
                add(".");
        doc.add(p);

        // Get a list with countries from the database
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
                        + "FROM film_country c, film_movie_country mc WHERE c.id = mc.country_id "
                        + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // Loop over the countries
        while (rs.next()) {
            Paragraph country = new Paragraph(rs.getString("country"));
            country.add(": ");
            Link link = new Link(String.format("%d movies", rs.getInt("c")),
                    PdfAction.createGoToR("../../part1/chapter02/Listing_02_22_MovieLinks1.pdf", rs.getString("country_id"), true));
            country.add(link);
            doc.add(country);
        }
        p = new Paragraph("Go to ").
                add(new Link("top", PdfAction.createGoTo("top"))).
                add(".");
        doc.add(p);

        PdfArray array = new PdfArray();
        array.add(pdfDoc.getFirstPage().getPdfObject());
        array.add(PdfName.XYZ);
        array.add(new PdfNumber(36));
        array.add(new PdfNumber(842));
        array.add(new PdfNumber(1));

        pdfDoc.addNamedDestination("top", array);

        //Close document
        doc.close();
    }


    /**
     * Create an XML file with named destinations
     *
     * @param src  The path to the PDF with the destinations
     * @param dest The path to the XML file
     * @throws IOException
     */
    public static void createXml(String src, String dest) throws IOException, ParserConfigurationException, TransformerException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = db.newDocument();
        Element root = doc.createElement("Destination");
        doc.appendChild(root);

        Map<String, PdfObject> names = pdfDoc.getCatalog().getNameTree(PdfName.Dests).getNames();
        for (Map.Entry<String, PdfObject> name : names.entrySet()) {
            Element el = doc.createElement("Name");
            el.setAttribute("Page", name.getValue().toString());
            el.setTextContent(name.getKey());
            root.appendChild(el);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty("encoding", "ISO8859-1");

        t.transform(new DOMSource(doc), new StreamResult(dest));
        pdfDoc.close();
    }
}
