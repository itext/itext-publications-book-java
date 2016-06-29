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
public class Listing_06_26_ConcatenateForms1 extends GenericTest {
    public static final String DATASHEET =
            "./src/test/resources/pdfs/datasheet.pdf";
    public static final String DEST =
            "./target/test/resources/book/part2/chapter06/Listing_06_26_ConcatenateForms1.pdf";

    public static void main(String[] args) throws SQLException, IOException {
        new Listing_06_26_ConcatenateForms1().manipulatePdf(DEST);
    }

    @Override
    public void manipulatePdf(String dest) throws SQLException, IOException {
        // Create the result document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();

        // Create the first source document
        PdfDocument srcDoc = new PdfDocument(new PdfReader(DATASHEET));

        srcDoc.copyPagesTo(1, 1, pdfDoc, new PdfPageFormCopier());
        srcDoc.close();

        // Create the second source document
        srcDoc = new PdfDocument(new PdfReader(DATASHEET));

        srcDoc.copyPagesTo(1, 1, pdfDoc, new PdfPageFormCopier());
        srcDoc.close();

        pdfDoc.close();
    }
}
