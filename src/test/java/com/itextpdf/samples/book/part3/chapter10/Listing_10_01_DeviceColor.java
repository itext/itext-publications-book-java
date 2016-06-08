/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_10_01_DeviceColor extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter10/Listing_10_01_DeviceColor.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_10_01_DeviceColor().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException, MalformedURLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        // RGB Colors
        colorRectangle(canvas, new DeviceRgb(0x00, 0x00, 0x00), 36, 770, 36, 36);
        colorRectangle(canvas, new DeviceRgb(0x00, 0x00, 0xFF), 90, 770, 36, 36);
        colorRectangle(canvas, new DeviceRgb(0x00, 0xFF, 0x00), 144, 770, 36, 36);
        colorRectangle(canvas, new DeviceRgb(0xFF, 0x00, 0x00), 198, 770, 36, 36);
        colorRectangle(canvas, new DeviceRgb(0f, 1f, 1f), 252, 770, 36, 36);
        colorRectangle(canvas, new DeviceRgb(1f, 0f, 1f), 306, 770, 36, 36);
        colorRectangle(canvas, new DeviceRgb(1f, 1f, 0f), 360, 770, 36, 36);
        colorRectangle(canvas, Color.BLACK, 416, 770, 36, 36);
        colorRectangle(canvas, Color.LIGHT_GRAY, 470, 770, 36, 36);
        // CMYK Colors
        colorRectangle(canvas, new DeviceCmyk(0x00, 0x00, 0x00, 0x00), 36, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(0x00, 0x00, 0xFF, 0x00), 90, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(0x00, 0x00, 0xFF, 0xFF), 144, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(0x00, 0xFF, 0x00, 0x00), 198, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(0f, 1f, 0f, 0.5f), 252, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(1f, 0f, 0f, 0f), 306, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(1f, 0f, 0f, 0.5f), 360, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(1f, 0f, 0f, 1f), 416, 716, 36, 36);
        colorRectangle(canvas, new DeviceCmyk(0f, 0f, 0f, 1f), 470, 716, 36, 36);
        // Gray color
        colorRectangle(canvas, new DeviceGray(0x20), 36, 662, 36, 36);
        colorRectangle(canvas, new DeviceGray(0x40), 90, 662, 36, 36);
        colorRectangle(canvas, new DeviceGray(0x60), 144, 662, 36, 36);
        colorRectangle(canvas, new DeviceGray(0.5f), 198, 662, 36, 36);
        colorRectangle(canvas, new DeviceGray(0.625f), 252, 662, 36, 36);
        colorRectangle(canvas, new DeviceGray(0.75f), 306, 662, 36, 36);
        colorRectangle(canvas, new DeviceGray(0.825f), 360, 662, 36, 36);
        colorRectangle(canvas, Color.DARK_GRAY, 416, 662, 36, 36);
        colorRectangle(canvas, Color.LIGHT_GRAY, 470, 662, 36, 36);
        // Alternative ways to color the rectangle
        canvas.setFillColorRgb(0x00, 0x80, 0x80);
        canvas.rectangle(36, 608, 36, 36);
        canvas.fillStroke();
        canvas.setFillColorRgb(0.5f, 0.25f, 0.60f);
        canvas.rectangle(90, 608, 36, 36);
        canvas.fillStroke();
        canvas.setFillColorGray(0.5f);
        canvas.rectangle(144, 608, 36, 36);
        canvas.fillStroke();
        canvas.setFillColorCmyk(0xFF, 0xFF, 0x00, 0x80);
        canvas.rectangle(198, 608, 36, 36);
        canvas.fillStroke();
        canvas.setFillColorCmyk(0f, 1f, 1f, 0.5f);
        canvas.rectangle(252, 608, 36, 36);
        canvas.fillStroke();

        pdfDoc.close();
    }

    public void colorRectangle(PdfCanvas canvas, Color color, float x, float y, float width, float height) {
        canvas.saveState();
        canvas.setFillColor(color);
        canvas.rectangle(x, y, width, height);
        canvas.fillStroke();
        canvas.restoreState();
    }
}
