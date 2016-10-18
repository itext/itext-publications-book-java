/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class Listing_09_15_HtmlMovies1 extends GenericTest {
    public static final String HTML = "./target/test/resources/book/part3/chapter09/Listing_09_15_HtmlMovies1.html";
    public static final String DEST = "./target/test/resources/book/part3/chapter09/Listing_09_15_HtmlMovies1.pdf";

    public static void main(String[] args)
            throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_09_15_HtmlMovies1().manipulatePdf(DEST);
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
        InputStream inputStream = new FileInputStream(HTML);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");
        parser.parse(is, new CustomHandler(doc));

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
        StringBuffer buf = new StringBuffer("\t<span class=\"title\">");
        buf.append(movie.getMovieTitle());
        buf.append("</span><br />\n");
        buf.append("\t<ul>\n");
        for (Country country : movie.getCountries()) {
            buf.append("\t\t<li class=\"country\">");
            buf.append(country.getCountry());
            buf.append("</li>\n");
        }
        buf.append("\t</ul>\n");

        buf.append("<span>");
        buf.append("\tYear: <i>");
        buf.append(movie.getYear());
        buf.append(" minutes</i><br />\n");
        buf.append("\tDuration: <i>");
        buf.append(movie.getDuration());
        buf.append(" minutes</i><br />\n");
        buf.append("</span>");
        buf.append("\t<ul>\n");
        for (Director director : movie.getDirectors()) {
            buf.append("\t\t<span class=\"director\"><li>");
            buf.append(director.getName());
            buf.append(", ");
            buf.append(director.getGivenName());
            buf.append("</li></span>\n");
        }
        buf.append("\t</ul>\n");
        return buf.toString();
    }


    private static class CustomHandler extends DefaultHandler {
        public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

        protected Document document;
        protected Paragraph paragraph;
        protected com.itextpdf.layout.element.List list;
        protected ListItem listItem;
        protected PdfFont font;
        protected boolean isItalic;

        public CustomHandler(Document document) {
            this.document = document;
            list = new com.itextpdf.layout.element.List().setListSymbol("").setSymbolIndent(10);
            listItem = new ListItem();
            try {
                font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            paragraph = new Paragraph().setFont(font);
            isItalic = false;
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            if ("i".equals(qName)) {
                isItalic = true;
            } else if ("br".equals(qName)) {
                paragraph.add(new Text("\n"));
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if ("span".equals(qName)) {
                if (!paragraph.isEmpty()) {
                    document.add(paragraph);
                }
                paragraph = new Paragraph().setFont(font);
            } else if ("ul".equals(qName)) {
                document.add(list);
                list = new com.itextpdf.layout.element.List();
                list.setListSymbol("").setSymbolIndent(10);
            } else if ("li".equals(qName)) {
                listItem.add(paragraph);
                list.add(listItem);
                listItem = new ListItem();
                paragraph = new Paragraph().setFont(font);
            } else if ("i".equals(qName)) {
                isItalic = false;
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