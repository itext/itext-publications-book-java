/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(SampleTest.class)
public class Listing_10_18_ImageMask extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter10/Listing_10_18_ImageMask.pdf";
    public static final String RESULT1
            = "./target/test/resources/book/part3/chapter10/hardmask.pdf";
    public static final String RESULT2
            = "./target/test/resources/book/part3/chapter10/softmask.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/bruno.jpg";

    public static void main(String args[]) throws IOException {
        new Listing_10_18_ImageMask().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        byte circledata[] = {(byte) 0x3c, (byte) 0x7e, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x7e,
                (byte) 0x3c};
        ImageData mask = ImageDataFactory.create(8, 8, 1, 1, circledata, null);
        mask.makeMask();
        mask.setInverted(true);
        createPdf(RESULT1, mask);
        byte gradient[] = new byte[256];
        for (int i = 0; i < 256; i++)
            gradient[i] = (byte) i;
        mask = ImageDataFactory.create(256, 1, 1, 8, gradient, null);
        mask.makeMask();
        createPdf(RESULT2, mask);
        // Create a file to compare via CompareTool
        concatenateResults(DEST, new String[]{RESULT1, RESULT2});
    }

    public void createPdf(String filename, ImageData mask) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);
        ImageData img = ImageDataFactory.create(RESOURCE);
        img.setImageMask(mask);
        doc.add(new com.itextpdf.layout.element.Image(img));
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
