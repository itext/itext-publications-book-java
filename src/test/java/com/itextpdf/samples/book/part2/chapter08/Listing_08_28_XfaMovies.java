/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_08_28_XfaMovies extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter08/Listing_08_28_XfaMovies.pdf";
    /** The original PDF. */
    public static final String RESOURCE = "./src/test/resources/pdfs/xfa_movies.pdf";
    /** Information about the form in xfa_movies.pdf */
    public static final String RESULTTXT = "./target/test/resources/book/part2/chapter08/movies_xfa.txt";
    /** The XML data that is going to be used to fill out the XFA form. */
    public static final String XMLDATA = "./target/test/resources/book/part2/chapter08/movies.xml";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param xml the XML data that needs to be added to the XFA form
     * @param dest the resulting PDF
     * @throws IOException
     */
    public void manipulatePdf2(String src, String xml, String dest)
            throws IOException {
        PdfReader reader = new PdfReader(src);
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        XfaForm xfa = form.getXfaForm();
        xfa.fillXfaForm(new FileInputStream(xml));
        xfa.write(pdfDoc);
        pdfDoc.close();
    }

    /**
     * Creates an XML file containing data about movies.
     * @param dest the path to the resulting XML file
     * @throws IOException
     */
    public void createXML(String dest) throws IOException, SQLException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dest), "UTF-8");
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        out.write("<movies>\n");
        for (Movie movie : movies) {
            out.write(getXml(movie));
        }
        out.write("</movies>");
        out.flush();
        out.close();
        connection.close();
    }

    /**
     * Creates an XML snippet containing information about a movie.
     * @param movie the Movie pojo
     * @return an XML snippet
     */
    public String getXml(Movie movie) {
        StringBuffer buf = new StringBuffer();
        buf.append("<movie duration=\"");
        buf.append(movie.getDuration());
        buf.append("\" imdb=\"");
        buf.append(movie.getImdb());
        buf.append("\" year=\"");
        buf.append(movie.getYear());
        buf.append("\">");
        buf.append("<title>");
        buf.append(movie.getMovieTitle());
        buf.append("</title>");
        if (movie.getOriginalTitle() != null) {
            buf.append("<original>");
            buf.append(movie.getOriginalTitle());
            buf.append("</original>");
        }
        buf.append("<directors>");
        for (Director director : movie.getDirectors()) {
            buf.append("<director>");
            buf.append(director.getName());
            buf.append(", ");
            buf.append(director.getGivenName());
            buf.append("</director>");
        }
        buf.append("</directors>");
        buf.append("<countries>");
        for (Country country : movie.getCountries()) {
            buf.append("<country>");
            buf.append(country.getCountry());
            buf.append("</country>");
        }
        buf.append("</countries>");
        buf.append("</movie>\n");
        return buf.toString();
    }

    public static void main(String[] args) throws Exception {
        new Listing_08_28_XfaMovies().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        new Listing_08_18_XfaMovie().readFieldnames(RESOURCE, RESULTTXT);
        createXML(XMLDATA);
        manipulatePdf2(RESOURCE, XMLDATA, DEST);
    }
}
