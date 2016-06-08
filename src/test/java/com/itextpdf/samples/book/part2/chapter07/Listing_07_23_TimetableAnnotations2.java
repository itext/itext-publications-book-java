/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class Listing_07_23_TimetableAnnotations2 extends Listing_07_21_TimetableAnnotations1 {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter07/Listing_07_23_TimetableAnnotations2.pdf";

    public static final String MOVIE_TEMPLATES = "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    /**
     * Path to IMDB.
     */
    public static final String IMDB = "http://imdb.com/title/tt%s/";

    public static void main(String args[]) throws IOException, SQLException {
        Listing_07_23_TimetableAnnotations2 application = new Listing_07_23_TimetableAnnotations2();
        application.afterManipulatePdf();
        application.manipulatePdf(DEST);
        application.beforeManipulatePdf();
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Listing_03_29_MovieTemplates.main(arguments);
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        locations = PojoFactory.getLocations(connection);
        // Create a reader
        PdfReader reader = new PdfReader(MOVIE_TEMPLATES);
        // Create a stamper
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        // Add annotations for every screening
        int page = 1;
        Rectangle rect;
        PdfLinkAnnotation annotation;
        for (Date day : PojoFactory.getDays(connection)) {
            for (Screening screening : PojoFactory.getScreenings(connection, day)) {
                rect = getPosition(screening);
                annotation = new PdfLinkAnnotation(rect);
                annotation.setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT);
                annotation.setAction(PdfAction.createURI(String.format(IMDB, screening.getMovie().getImdb())));
                annotation.setBorder(new PdfArray(new float[]{0, 0, 1}));
                pdfDoc.getPage(page).addAnnotation(annotation);
            }
            page++;
        }
        // Close the stamper
        pdfDoc.close();
        // Close the database connection
        connection.close();
    }
}
