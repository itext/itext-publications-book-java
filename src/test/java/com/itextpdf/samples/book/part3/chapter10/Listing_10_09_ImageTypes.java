/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.barcodes.BarcodePDF417;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageData;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageHelper;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.awt.*;
import java.io.IOException;

@Category(SampleTest.class)
public class Listing_10_09_ImageTypes extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter10/Listing_10_09_ImageTypes.pdf";
    public static final String[] RESOURCES = {
            "bruno_ingeborg.jpg",
            "map.jp2",
            "info.png",
            "close.bmp",
            "movie.gif",
            "butterfly.wmf",
            "animated_fox_dog.gif",
            "marbles.tif",
            "amb.jb2"
    };
    public static final String RESOURCE = "src/test/resources/img/hitchcock.png";

    public static void main(String args[]) throws IOException {
        new Listing_10_09_ImageTypes().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img;
        for (int i = 0; i < RESOURCES.length; i++) {
            if (String.format("src/test/resources/img/%s", RESOURCES[i]).contains(".wmf")) {
                WmfImageHelper wmf
                        = new WmfImageHelper(new WmfImageData(String.format("src/test/resources/img/%s", RESOURCES[i])));
                img = new Image((PdfFormXObject) wmf.createPdfForm(pdfDoc));
            } else {
                ImageData image
                        = ImageDataFactory.create(String.format("src/test/resources/img/%s", RESOURCES[i]));
                img = new Image(new PdfImageXObject(image));
            }

            if (img.getImageWidth() > 300 || img.getImageHeight() > 300) {
                img.scaleToFit(300, 300);
            }
            doc.add(new Paragraph(String.format("%s is an image of type %s", RESOURCES[i], img.getClass().getName())));
            doc.add(img);
        }

        java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(RESOURCE);
        img = new Image(new PdfImageXObject(ImageDataFactory.create(awtImage, null)));
        doc.add(new Paragraph(String.format("%s is an image of type %s", "java.awt.Image", img.getClass().getName())));
        doc.add(img);

        BarcodeEAN codeEAN = new BarcodeEAN(pdfDoc);
        codeEAN.setCodeType(BarcodeEAN.EAN13);
        codeEAN.setCode("9781935182610");
        img = new Image(codeEAN.createFormXObject(null, null, pdfDoc));
        doc.add(new Paragraph(String.format("%s is an image of type %s", "Barcode", img.getClass().getName())));
        doc.add(img);

        BarcodePDF417 pdf417 = new BarcodePDF417();
        String text = "iText in Action, a book by Bruno Lowagie.";
        pdf417.setCode(text);
        PdfFormXObject xObject = pdf417.createFormXObject(null, pdfDoc);
        img = new Image(xObject);
        doc.add(new Paragraph(String.format("%s is an image of type %s", "Barcode", img.getClass().getName())));
        doc.add(img);

        //Close document
        pdfDoc.close();
    }
}
