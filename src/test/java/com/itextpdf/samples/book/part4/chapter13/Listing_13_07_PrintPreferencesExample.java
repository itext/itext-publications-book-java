/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_13_07_PrintPreferencesExample extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part4/chapter13/Listing_13_07_PrintPreferencesExample.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        new Listing_13_07_PrintPreferencesExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_5)));
        Document doc = new Document(pdfDoc);
        PdfViewerPreferences prefs = new PdfViewerPreferences();
        prefs.setPrintScaling(PdfViewerPreferences.PdfViewerPreferencesConstants.NONE);
        prefs.setNumCopies(3);
        prefs.setPickTrayByPDFSize(true);
        pdfDoc.getCatalog().setViewerPreferences(prefs);
        doc.add(new Paragraph("Hello World"));
        doc.close();
    }
}
