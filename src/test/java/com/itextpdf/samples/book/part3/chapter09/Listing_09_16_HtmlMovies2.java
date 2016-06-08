/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.VerticalAlignment;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Listing_09_16_HtmlMovies2 extends Listing_09_15_HtmlMovies1 {
    public static final String HTML = "./target/test/resources/book/part3/chapter09/Listing_09_16_HtmlMovies2.html";
    public static final String DEST = "./target/test/resources/book/part3/chapter09/Listing_09_16_HtmlMovies2.pdf";

    public static void main(String[] args)
            throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_09_16_HtmlMovies2().manipulatePdf(DEST);
    }

    @Override
    public void manipulatePdf(String pdf)
            throws SQLException, IOException, ParserConfigurationException, SAXException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // Create a stream to produce HTML
        PrintStream out = new PrintStream(HTML, "UTF-8");
        out.println("<html>\n<body>");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdf));
        Document doc = new Document(pdfDoc);

        List<Movie> movies = PojoFactory.getMovies(connection);
        String snippet;
        for (Movie movie : movies) {
            // create the snippet
            snippet = createHtmlSnippet(movie);
            // use the snippet for the HTML page
            out.println(snippet);
        }
        // flush and close the HTML stream
        out.print("</body>\n</html>");
        out.flush();
        out.close();

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(new InputSource(new FileInputStream(HTML)), new CustomHandler(doc));

        // close the database connection and the document
        connection.close();
        doc.close();
    }

    /**
     * Creates an HTML snippet with info about a movie.
     *
     * @param movie the movie for which we want to create HTML
     * @return a String with HTML code
     */
    public String createHtmlSnippet(Movie movie) {
        StringBuilder buf = new StringBuilder("<table width=\"500\">\n<tr>\n");
        buf.append("\t<td><img src=\"./src/test/resources/img/posters/");
        buf.append(movie.getImdb());
        buf.append(".jpg\" /></td>\t<td>\n");
        buf.append(super.createHtmlSnippet(movie));
        buf.append("\t</td>\n</tr>\n</table>");
        return buf.toString();
    }

    private static class CustomHandler extends DefaultHandler {
        public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

        protected PdfFont font;
        protected Paragraph paragraph;
        protected com.itextpdf.layout.element.List list;
        protected ListItem listItem;
        protected Document document;
        protected Cell cell;
        protected Table table;
        protected Image img;
        protected boolean isItalic ;

        public CustomHandler(Document document) {
            this.document = document;
            try {
                font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            paragraph = new Paragraph().setFont(font);
            list = new com.itextpdf.layout.element.List();
            listItem = new ListItem();
            cell = new Cell().setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE);
            table = new Table(2).setBorder(Border.NO_BORDER);
            isItalic = false;
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            if ("span".equals(qName)) {
                if ("director".equals(attributes.getValue("class"))) {
                    // MidNightBlue color
                    paragraph.setFontColor(new DeviceRgb(0.09803922f, 0.09803922f, 0.4392157f));
                    paragraph.setBold();
                }
            } else if ("ul".equals(qName)) {
                list.setSymbolIndent(10);
            } else if ("li".equals(qName)) {
                paragraph = new Paragraph().setFont(font);
                if ("country".equals(attributes.getValue("class"))) {
                    isItalic = true;
                    paragraph.setFontColor(new DeviceRgb(0, 128, 128));
                }
                paragraph.setFixedLeading(14);
            } else if ("i".equals(qName)) {
                isItalic = true;
            } else if ("br".equals(qName)) {
                paragraph.add(new Text("\n"));
            } else if ("img".equals(qName)) {
                String path = attributes.getValue("src");
                try {
                    img = new Image(ImageDataFactory.create(path));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if ("span".equals(qName)) {
                cell.add(paragraph);
                paragraph = new Paragraph().setFont(font);
            } else if ("ul".equals(qName)) {
                cell.add(list);
                list = new com.itextpdf.layout.element.List();
            } else if ("li".equals(qName)) {
                listItem.add(paragraph);
                list.add(listItem);
                paragraph = new Paragraph().setFont(font);
                listItem = new ListItem();
            } else if ("i".equals(qName)) {
                isItalic = false;
            } else if ("tr".equals(qName)) {
                document.add(table);
                table = new Table(2).setBorder(Border.NO_BORDER);
            } else if ("td".equals(qName)) {
                table.addCell(cell);
                cell = new Cell().setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE);
            } else if ("img".equals(qName)) {
                cell.add(img);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            Text text = new Text(strip(new StringBuffer().append(ch, start, length)));
            if (isItalic) {
                text.setItalic();
            }
            if (0 != text.getText().length()) {
                paragraph.add(text);
            }
        }

        /**
         * Replaces all the newline characters by a space.
         *
         * @param buf the original StringBuffer
         * @return a String without newlines
         */
        protected String strip(StringBuffer buf) {
            while (buf.length() != 0 && (buf.charAt(0) == '\n' || buf.charAt(0) == '\t')) {
                buf.deleteCharAt(0);
            }
            while (buf.length() != 0 && (buf.charAt(0) == '\n' || buf.charAt(0) == '\t'))
                buf.deleteCharAt(buf.length() - 1);
            return buf.toString();
        }
    }
}