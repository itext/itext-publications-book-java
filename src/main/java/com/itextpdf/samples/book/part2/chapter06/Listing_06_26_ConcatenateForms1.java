package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_06_26_ConcatenateForms1 {
    public static final String DATASHEET =
            "./src/main/resources/pdfs/datasheet.pdf";
    public static final String DEST =
            "./target/book/part2/chapter06/Listing_06_26_ConcatenateForms1.pdf";

    public static void main(String[] args) throws SQLException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_26_ConcatenateForms1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws SQLException, IOException {
        // Create the result document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();

        PdfPageFormCopier formCopier = new PdfPageFormCopier();

        // Create the first source document
        PdfDocument srcDoc = new PdfDocument(new PdfReader(DATASHEET));

        srcDoc.copyPagesTo(1, 1, pdfDoc, formCopier);
        srcDoc.close();

        // Create the second source document
        srcDoc = new PdfDocument(new PdfReader(DATASHEET));

        srcDoc.copyPagesTo(1, 1, pdfDoc, formCopier);
        srcDoc.close();

        pdfDoc.close();
    }
}
