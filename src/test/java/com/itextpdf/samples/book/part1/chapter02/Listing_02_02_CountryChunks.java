/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_02_02_CountryChunks extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_02_CountryChunks.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_02_CountryChunks().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // create the statement
        Statement stm =
                connection.createStatement();
        // execute the query
        ResultSet rs = stm.executeQuery("SELECT country, id FROM film_country ORDER BY country");
        // loop over the results
        while (rs.next()) {
            // write a country to the text file
            Paragraph p = new Paragraph().setFixedLeading(16);
            p.add(new Text(rs.getString("country")));
            p.add(new Text(" "));
            Text id = new Text(rs.getString("id")).setFont(font).setFontSize(6).setFontColor(Color.WHITE);
            // with a background color and a text rise
            id.setBackgroundColor(Color.BLACK, 1f, 0.5f, 1f, 1.5f).setTextRise(6);
            p.add(id);
            doc.add(p);
        }

        stm.close();
        connection.close();
        doc.close();
    }
}
