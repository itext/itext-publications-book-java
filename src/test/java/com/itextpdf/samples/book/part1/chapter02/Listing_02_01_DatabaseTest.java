/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class Listing_02_01_DatabaseTest extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_01_DatabaseTest.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_01_DatabaseTest().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException, SQLException, UnsupportedEncodingException {

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // create the statement
        Statement stm = connection.createStatement();
        // execute the query
        ResultSet rs = stm.executeQuery("SELECT country FROM film_country ORDER BY country");
        // loop over the results
        while (rs.next()) {
            // write a country to the text file
           doc.add(new Paragraph(rs.getString("country")));
        }
        // close the statement
        stm.close();
        // close the database connection
        connection.close();
        doc.close();

    }
}
