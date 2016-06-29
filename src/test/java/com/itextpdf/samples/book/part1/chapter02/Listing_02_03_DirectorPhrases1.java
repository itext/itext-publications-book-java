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
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Category(SampleTest.class)
public class Listing_02_03_DirectorPhrases1 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_03_DirectorPhrases1.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_03_DirectorPhrases1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // create the statement
        Statement stm = connection.createStatement();
        // execute the query
        ResultSet rs = stm.executeQuery("SELECT name, given_name FROM film_director ORDER BY name, given_name");
        // loop over the results
        while (rs.next()) {
            doc.add(createDirectorPhrase(rs, pdfDoc));
        }
        stm.close();
        connection.close();
        doc.close();
    }

    public Paragraph createDirectorPhrase(ResultSet rs, PdfDocument pdfDoc)
            throws SQLException, IOException {
        PdfFont boldUnderlined = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        PdfFont normal = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        Paragraph director = new Paragraph();
        director.add(new Text(rs.getString("name"))
                .setFont(boldUnderlined)
                .setFontSize(12)
                .setUnderline());
        director.add(new Text(",")
                .setFont(boldUnderlined)
                .setFontSize(12)
                .setUnderline());
        director.add(new Text(" ")
                .setFont(normal)
                .setFontSize(12));
        director.add(new Text(rs.getString("given_name"))
                .setFont(normal)
                .setFontSize(12));
        return director;
    }
}
