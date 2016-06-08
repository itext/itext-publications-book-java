/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_07_16_BookmarkedTimeTable extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter07/Listing_07_16_BookmarkedTimeTable.pdf";
    public static final String RESOURCE
            = "./src/test/resources/js/print_page.js";

    public static final String MOVIE_TEMPLATES = "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        Listing_07_16_BookmarkedTimeTable application = new Listing_07_16_BookmarkedTimeTable();
        application.arguments = args;
        application.manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create a reader
        PdfReader reader = new PdfReader(MOVIE_TEMPLATES);
        // Create a stamper
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // Create a list with bookmarks
        PdfOutline root = pdfDoc.getOutlines(false);
        root.setTitle("Calendar");
        int page = 1;
        PdfOutline kid;
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            kid = root.addOutline(day.toString());
            kid.addAction(PdfAction.createGoTo(PdfExplicitDestination.createFit(page)));
            page++;
        }
        // Close the document
        pdfDoc.close();
        // Close the database connection
        connection.close();
    }
}
