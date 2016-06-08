/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfTransparencyGroup;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_10_06_Transparency1 extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter10/Listing_10_06_Transparency1.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_10_06_Transparency1().manipulatePdf(DEST);
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

            pictureCircles(gap, 500, canvas);
            canvas.saveState();
            PdfExtGState gs1 = new PdfExtGState();
            gs1.setFillOpacity(0.5f);
            canvas.setExtGState(gs1);
            pictureCircles(200 + 2 * gap, 500, canvas);
            canvas.restoreState();

            canvas.saveState();
            PdfFormXObject tp = new PdfFormXObject(new Rectangle(200, 200));
            PdfCanvas xObjectCanvas = new PdfCanvas(tp, pdfDoc);
            PdfTransparencyGroup transparencyGroup = new PdfTransparencyGroup();
            tp.setGroup(transparencyGroup);
            pictureCircles(0, 0, xObjectCanvas);
            canvas.setExtGState(gs1);
            canvas.addXObject(tp, gap, 500 - 200 - gap);
            canvas.restoreState();

            canvas.saveState();
            tp = new PdfFormXObject(new Rectangle(200, 200));
            xObjectCanvas = new PdfCanvas(tp, pdfDoc);
            tp.setGroup(transparencyGroup);
            PdfExtGState gs2 = new PdfExtGState();
            gs2.setFillOpacity(0.5f);
            gs2.setBlendMode(PdfExtGState.BM_HARD_LIGHT);
            xObjectCanvas.setExtGState(gs2);
            pictureCircles(0, 0, xObjectCanvas);
            canvas.addXObject(tp, 200 + 2 * gap, 500 - 200 - gap);
            canvas.restoreState();

            canvas.resetFillColorRgb();
            // ColumnText ct = new ColumnText(canvas);
            Paragraph p = new Paragraph("Ungrouped objects\nObject opacity = 1.0");
            new Canvas(canvas, pdfDoc, new Rectangle(gap, 0, 200, 500))
                    .add(p.setTextAlignment(TextAlignment.CENTER).setFontSize(18));

            p = new Paragraph("Ungrouped objects\nObject opacity = 0.5");
            new Canvas(canvas, pdfDoc, new Rectangle(200 + 2 * gap, 0, 200, 500))
                    .add(p.setTextAlignment(TextAlignment.CENTER).setFontSize(18));

            p = new Paragraph(
                    "Transparency group\nObject opacity = 1.0\nGroup opacity = 0.5\nBlend mode = Normal");
            new Canvas(canvas, pdfDoc, new Rectangle(gap, 0, 200, 500 - 200 - gap))
                    .add(p.setTextAlignment(TextAlignment.CENTER).setFontSize(18));

            p = new Paragraph(
                    "Transparency group\nObject opacity = 0.5\nGroup opacity = 1.0\nBlend mode = HardLight");
            new Canvas(canvas, pdfDoc, new Rectangle(200 + 2 * gap, 0, 200, 500 - 200 - gap))
                    .add(p.setTextAlignment(TextAlignment.CENTER).setFontSize(18));

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
        canvas.setStrokeColor(Color.DARK_GRAY)
                .setFillColor(new DeviceGray(0.8f))
                .rectangle(x, y, 100, 200)
                .fill()
                .setLineWidth(2)
                .rectangle(x, y, 200, 200)
                .stroke();
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
        canvas.setFillColor(Color.RED)
                .circle(x + 70, y + 70, 50)
                .fill()
                .setFillColor(Color.YELLOW)
                .circle(x + 100, y + 130, 50)
                .fill()
                .setFillColor(Color.BLUE)
                .circle(x + 130, y + 70, 50)
                .fill();
    }
}
