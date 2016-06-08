/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(SampleTest.class)
public class Listing_10_12_CompressImage extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter10/Listing_10_12_CompressImage.pdf";
    public static final String RESULT1
            = "./target/test/resources/book/part3/chapter10/uncompressed.pdf";
    public static final String RESULT2
            = "./target/test/resources/book/part3/chapter10/compressed.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/butterfly.bmp";

    public static void main(String args[]) throws IOException {
        new Listing_10_12_CompressImage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(RESULT1, false);
        createPdf(RESULT2, true);
        concatenateResults(dest, new String[]{RESULT1, RESULT2});
    }

    public void createPdf(String filename, boolean compress) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);
        Image img = new Image(ImageDataFactory.create(RESOURCE));
        if (compress) {
            img.getXObject().getPdfObject().setCompressionLevel(9);
        }
        doc.add(img);
        doc.close();
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
