/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_10_21_ClippingPath extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter10/Listing_10_21_ClippingPath.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/bruno_ingeborg.jpg";

    public static void main(String args[]) throws IOException {
        new Listing_10_21_ClippingPath().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        ImageData img = ImageDataFactory.create(RESOURCE);
        com.itextpdf.layout.element.Image imgModel = new com.itextpdf.layout.element.Image(img);
        float w = imgModel.getImageScaledWidth();
        float h = imgModel.getImageScaledHeight();
        PdfFormXObject xObject = new PdfFormXObject(new Rectangle(850, 600));
        PdfCanvas xObjectCanvas = new PdfCanvas(xObject, pdfDoc);
        xObjectCanvas.ellipse(0, 0, 850, 600);
        xObjectCanvas.clip();
        xObjectCanvas.newPath();
        xObjectCanvas.addImage(img, w, 0, 0, h, 0, -600);
        com.itextpdf.layout.element.Image clipped = new com.itextpdf.layout.element.Image(xObject);
        clipped.scale(0.5f, 0.5f);
        doc.add(clipped);
        doc.close();
    }
}
