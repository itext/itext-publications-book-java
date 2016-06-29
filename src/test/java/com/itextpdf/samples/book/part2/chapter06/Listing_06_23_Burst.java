/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_06_23_Burst extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part2/chapter06/Listing_06_23_Burst.pdf";
    public static final String FORMATTEDDEST =
            "./target/test/resources/book/part2/chapter06/Listing_06_23_Burst%d.pdf";
    public static final String MOVIE_TEMPLATES =
            "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_23_Burst().manipulatePdf(FORMATTEDDEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES));

        for (int i = 1; i <= srcDoc.getNumberOfPages(); i++) {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(String.format(FORMATTEDDEST, i)));
            pdfDoc.initializeOutlines();

            srcDoc.copyPagesTo(i, i, pdfDoc, new PdfPageFormCopier());
            pdfDoc.close();
        }

        // Make file to compare via CompareTool
        String[] names = new String[srcDoc.getNumberOfPages()];
        for (int i = 0; i < names.length; i++) {
            names[i] = String.format(FORMATTEDDEST, i+1);
        }
        srcDoc.close();
        // Create file to compare via CompareTool
        concatenateResults(DEST, names);
    }

    // Only for testing reasons
    protected void concatenateResults(String dest, String[] names) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();
        PdfDocument tempDoc;
        for (String name : names) {
            tempDoc = new PdfDocument(new PdfReader(name));
            tempDoc.copyPagesTo(1, tempDoc.getNumberOfPages(), pdfDoc);
            tempDoc.close();
        }
        pdfDoc.close();
    }
}
