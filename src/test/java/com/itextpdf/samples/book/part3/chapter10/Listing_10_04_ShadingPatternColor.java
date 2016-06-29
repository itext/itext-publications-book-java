/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.PatternColor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;

@Category(SampleTest.class)
public class Listing_10_04_ShadingPatternColor extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter10/Listing_10_04_ShadingPatternColor.pdf";

    public static void colorRectangle(PdfCanvas canvas, Color color, float x, float y, float width, float height) {
        canvas.saveState().setFillColor(color).rectangle(x, y, width, height).fillStroke().restoreState();
    }

    public static void main(String args[]) throws IOException {
        new Listing_10_04_ShadingPatternColor().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        //Write to canvas
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());

        com.itextpdf.kernel.pdf.colorspace.PdfShading axial
                = new PdfShading.Axial(new PdfDeviceCs.Rgb(), 36, 716, new float[]{1, .784f, 0}, 396, 788, new float[]{0, 0, 1}, new boolean[] {true, true});
        canvas.paintShading(axial);
        canvas = new PdfCanvas(pdfDoc.addNewPage());
        com.itextpdf.kernel.pdf.colorspace.PdfShading radial
                = new PdfShading.Radial(new PdfDeviceCs.Rgb(), 200, 700, 50, new float[] {1, 0.968f, 0.58f}, 300, 700, 100, new float[] {0.968f, 0.541f, 0.42f});
        canvas.paintShading(radial);

        PdfPattern.Shading shading = new PdfPattern.Shading(axial);
        colorRectangle(canvas, new PatternColor(shading), 150, 420, 126, 126);
        canvas.setFillColorShading(shading);
        canvas.rectangle(300, 420, 126, 126);
        canvas.fillStroke();

        //Close document
        pdfDoc.close();
    }
}
