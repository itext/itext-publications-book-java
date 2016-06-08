/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_12_06_HelloWorldCompression extends GenericTest {
    public static final String[] RESULT = {
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_not_at_all.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_zero.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_normal.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_high.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_full.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_full_too.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_06_HelloWorldCompression_compression_removed.pdf"
    };
    public static final String[] CMP_RESULT = {
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_not_at_all.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_zero.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_normal.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_high.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_full.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_full_too.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_06_HelloWorldCompression_compression_removed.pdf"
    };

    protected PdfFont bold;
    protected PdfFont boldItalic;
    protected PdfFont italic;
    protected PdfFont normal;

    public static void main(String args[]) throws IOException, XMPException, SQLException {
        new Listing_12_06_HelloWorldCompression().manipulatePdf(RESULT[0]);
    }

    public void createPdf(String dest, int compression) throws IOException, XMPException, SQLException {
        WriterProperties properties = new WriterProperties();
        switch (compression) {
            case -1:
                properties.setCompressionLevel(CompressionConstants.NO_COMPRESSION);
                break;
            case 0:
                properties.setCompressionLevel(CompressionConstants.DEFAULT_COMPRESSION);
                break;
            case 2:
                properties.setCompressionLevel(CompressionConstants.BEST_COMPRESSION);
                break;
            case 3:
                properties.setFullCompressionMode(true);
                break;
        }
        PdfWriter writer = new PdfWriter(dest, properties);

        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        boldItalic = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);
        italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
        normal = PdfFontFactory.createFont(FontConstants.HELVETICA);

        // Create database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
                        + "FROM film_country c, film_movie_country mc "
                        + "WHERE c.id = mc.country_id "
                        + "GROUP BY mc.country_id, country ORDER BY c DESC");

        // Create a new list
        List list = new List(ListNumberingType.DECIMAL);
        // loop over the countries
        while (rs.next()) {
            // create a list item for the country
            ListItem item = new ListItem(
                    String.format("%s: %d movies",
                            rs.getString("country"), rs.getInt("c")));
            item.setFont(boldItalic);
            // create a movie list for each country
            List movielist = new List(ListNumberingType.ENGLISH_LOWER);
            for (Movie movie :
                    PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                ListItem movieitem = new ListItem(movie.getMovieTitle());
                List directorlist = new List();
                for (Director director : movie.getDirectors()) {
                    directorlist.add(
                            String.format("%s, %s",
                                    director.getName(), director.getGivenName()));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        doc.add(list);

        stm.close();
        connection.close();
        doc.close();
    }

    public void compressPdf(String src, String dest) throws IOException {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties()
                .setPdfVersion(PdfVersion.PDF_1_5)
                .setFullCompressionMode(true));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), writer);
        //pdfDoc.getWriter().setCompressionLevel(PdfOutputStream.BEST_COMPRESSION);
        pdfDoc.close();
    }

    public void decompressPdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        pdfDoc.close();
    }

    public void manipulatePdf(String dest) throws IOException, XMPException, SQLException {
        createPdf(RESULT[0], -1);
        createPdf(RESULT[1], 0);
        createPdf(RESULT[2], 1);
        createPdf(RESULT[3], 2);
        createPdf(RESULT[4], 3);
        compressPdf(RESULT[1], RESULT[5]);
        decompressPdf(RESULT[5], RESULT[6]);
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        String outPath;
        for (int i = 0; i < RESULT.length; i++) {
            outPath = new File(RESULT[i]).getParent();
            if (compareXml) {
                if (!compareTool.compareXmls(RESULT[i], CMP_RESULT[i])) {
                    addError("The XML structures are different.");
                }
            } else {
                if (compareRenders) {
                    addError(compareTool.compareVisually(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                    addError(compareTool.compareLinkAnnotations(dest, cmp));
                } else {
                    addError(compareTool.compareByContent(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                }
                addError(compareTool.compareDocumentInfo(RESULT[i], CMP_RESULT[i]));
            }
        }

        if (errorMessage != null) Assert.fail(errorMessage);
    }

    // only for GenericTest running
    @Override
    protected String getDest() {
        File file = new File(RESULT[0]);
        file.getParentFile().mkdirs();
        return RESULT[0];
    }
}
