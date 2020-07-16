package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_07_PrintPreferencesExample {
    public static final String DEST =
            "./target/book/part4/chapter13/Listing_13_07_PrintPreferencesExample.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_07_PrintPreferencesExample().manipulatePdf(DEST);
    }

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
