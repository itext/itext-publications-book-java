package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Listing_07_14_CreateOutlineTree {
    public static final String DEST
            = "./target/book/part2/chapter07/Listing_07_14_CreateOutlineTree.pdf";
    public static final String DEST_XML
            = "./target/book/part2/chapter07/Listing_07_14_CreateOutlineTree.xml";

    /**
     * Pattern of the IMDB urls
     */
    public static final String RESOURCE = "http://imdb.com/title/tt%s/";
    /**
     * JavaScript snippet.
     */
    public static final String INFO = "app.alert('Movie produced in %s; run length: %s');";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException, TransformerException, ParserConfigurationException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_07_14_CreateOutlineTree().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest)
            throws IOException, SQLException, TransformerException, ParserConfigurationException {
        manipulatePdf2(dest);
        createXml(dest, DEST_XML);
    }

    public void manipulatePdf2 (String dest) throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        pdfDoc.getCatalog().setPageMode(PdfName.UseOutlines);

        // To get cached outline tree, set flag to false. If outlines have not been initialized before the method will return null
        PdfOutline root = pdfDoc.getOutlines(false);
        PdfOutline movieBookmark;
        PdfOutline link;
        PdfOutline info;
        String title;
        List<Movie> movies = PojoFactory.getMovies(connection);

        // Add page in order to have canvas to write on
        pdfDoc.addNewPage();
        for (Movie movie : movies) {
            title = movie.getMovieTitle();
            if ("3-Iron".equals(title)) {
                title = "\ube48\uc9d1";
            }
            movieBookmark = root.addOutline(title);

            // Create an action GoTo to the top of the movieBookmark area. In iText7 element's renderer
            // is used to layout and draw the element. By getting the current area of a document renderer we can obtain
            // the space yet to be covered by the document's content and, therefore, the position at which the very
            // next element will be placed.
            // For more info please follow https://itextpdf.com/en/blog/technical-notes/itext-pdf-renderer-framework
            PdfPage lastPage = pdfDoc.getLastPage();
            float topOfBookmark = doc.getRenderer().getCurrentArea().getBBox().getTop();
            movieBookmark.addAction(PdfAction.createGoTo(PdfExplicitDestination.createFitH(lastPage, topOfBookmark)));

            link = movieBookmark.addOutline("link to IMDB");
            link.setStyle(PdfOutline.FLAG_BOLD);
            link.setColor(ColorConstants.BLUE);
            link.addAction(PdfAction.createURI((String.format(RESOURCE, movie.getImdb()))));
            info = movieBookmark.addOutline("instant info");
            info.addAction(PdfAction.createJavaScript(
                    String.format(INFO, movie.getYear(), movie.getDuration())));
            doc.add(new Paragraph(movie.getMovieTitle()));
            doc.add(PojoToElementFactory.getDirectorList(movie));
            doc.add(PojoToElementFactory.getCountryList(movie));
        }

        doc.close();

        // Close the database connection
        connection.close();
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
        Element root = doc.createElement("Bookmark");
        doc.appendChild(root);

        List<PdfOutline> outlines = pdfDoc.getOutlines(false).getAllChildren();
        for (PdfOutline outline : outlines) {
            Element el = doc.createElement("Title");
            Element el2 = doc.createElement("Link");
            Element el3 = doc.createElement("Info");
            el.setTextContent(outline.getTitle());
            el.setAttribute("ElementsNumber", outline.getContent().get(PdfName.Parent).toString().substring(47, 55));
            el2.setTextContent(outline.getAllChildren().get(0).getTitle());
            el3.setTextContent(outline.getAllChildren().get(1).getTitle());
            root.appendChild(el);
            el.appendChild(el2);
            el.appendChild(el3);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty("encoding", "ISO8859-1");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        t.transform(new DOMSource(doc), new StreamResult(dest));

        pdfDoc.close();
    }
}
