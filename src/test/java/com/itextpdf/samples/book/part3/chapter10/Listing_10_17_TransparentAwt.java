/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.awt.*;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_10_17_TransparentAwt extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter10/Listing_10_17_TransparentAwt.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/hitchcock.gif";

    public static void main(String args[]) throws IOException {
        new Listing_10_17_TransparentAwt().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        Rectangle rect = new Rectangle(PageSize.A4);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(rect));
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler(new DeviceGray(0.8f)));

        java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(RESOURCE);
        doc.add(new Paragraph("Hitchcock in Red."));
        Image img1 = new Image(ImageDataFactory.create(awtImage, null));
        doc.add(img1);
        doc.add(new Paragraph("Hitchcock in Black and White."));
        Image img2 = new Image(ImageDataFactory.create(awtImage, null, true));
        doc.add(img2);
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hitchcock in Red and Yellow."));
        Image img3 = new Image(ImageDataFactory.create(awtImage, new java.awt.Color(0xFF, 0xFF, 0x00)));
        doc.add(img3);
        doc.add(new Paragraph("Hitchcock in Black and White."));
        Image img4 = new Image(ImageDataFactory.create(awtImage, new java.awt.Color(0xFF, 0xFF, 0x00), true));
        doc.add(img4);

        doc.close();
    }


    protected class PageBackgroundsEventHandler implements IEventHandler {
        protected Color color;

        public PageBackgroundsEventHandler(Color color) {
            this.color = color;
        }

        @Override
        public void handleEvent(com.itextpdf.kernel.events.Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle rect = page.getPageSize();
            canvas
                    .saveState()
                    .setFillColor(color)
                    .rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight())
                    .fillStroke()
                    .restoreState();
        }
    }
}
