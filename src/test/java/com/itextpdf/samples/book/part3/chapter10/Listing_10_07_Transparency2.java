/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfTransparencyGroup;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_10_07_Transparency2 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter10/Listing_10_07_Transparency2.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_10_07_Transparency2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
            float gap = (pdfDoc.getDefaultPageSize().getWidth() - 400) / 3;

            pictureBackdrop(gap, 500, canvas);
            pictureBackdrop(200 + 2 * gap, 500, canvas);
            pictureBackdrop(gap, 500 - 200 - gap, canvas);
            pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, canvas);
            PdfFormXObject tp;
            PdfCanvas xObjectCanvas;
            PdfTransparencyGroup group;

            tp = new PdfFormXObject(new Rectangle(200, 200));
            xObjectCanvas = new PdfCanvas(tp, pdfDoc);
            pictureCircles(0, 0, xObjectCanvas);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(true);
            tp.setGroup(group);
            canvas.addXObject(tp, gap, 500);

            tp = new PdfFormXObject(new Rectangle(200, 200));
            xObjectCanvas = new PdfCanvas(tp, pdfDoc);
            pictureCircles(0, 0, xObjectCanvas);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(false);
            tp.setGroup(group);
            canvas.addXObject(tp, 200 + 2 * gap, 500);

            tp = new PdfFormXObject(new Rectangle(200, 200));
            xObjectCanvas = new PdfCanvas(tp, pdfDoc);
            pictureCircles(0, 0, xObjectCanvas);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(true);
            tp.setGroup(group);
            canvas.addXObject(tp, gap, 500 - 200 - gap);

            tp = new PdfFormXObject(new Rectangle(200, 200));
            xObjectCanvas = new PdfCanvas(tp, pdfDoc);
            pictureCircles(0, 0, xObjectCanvas);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(false);
            tp.setGroup(group);
            canvas.addXObject(tp, 200 + 2 * gap, 500 - 200 - gap);

            pdfDoc.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    /**
     * Prints a square and fills half of it with a gray rectangle.
     *
     * @param x
     * @param y
     * @param canvas
     * @throws Exception
     */
    public static void pictureBackdrop(float x, float y, PdfCanvas canvas) {
        PdfShading.Axial axial = new PdfShading.Axial(new PdfDeviceCs.Rgb(), x, y, Color.YELLOW.getColorValue(),
                x + 200, y, Color.RED.getColorValue());
        PdfPattern.Shading axialPattern = new PdfPattern.Shading(axial);
        canvas
                .setFillColorShading(axialPattern)
                .setStrokeColor(Color.BLACK)
                .setLineWidth(2)
                .rectangle(x, y, 200, 200)
                .fillStroke();
    }

    /**
     * Prints 3 circles in different colors that intersect with eachother.
     *
     * @param x
     * @param y
     * @param canvas
     * @throws Exception
     */
    public static void pictureCircles(float x, float y, PdfCanvas canvas) {
        PdfExtGState gs = new PdfExtGState();
        gs.setBlendMode(PdfExtGState.BM_MULTIPLY);
        gs.setFillOpacity(1f);
        canvas
                .setExtGState(gs)
                .setFillColor(new DeviceCmyk(0f, 0f, 0f, 0.15f))
                .circle(x + 75, y + 75, 70)
                .fill()
                .circle(x + 75, y + 125, 70)
                .fill()
                .circle(x + 125, y + 75, 70)
                .fill()
                .circle(x + 125, y + 125, 70)
                .fill();
    }
}
