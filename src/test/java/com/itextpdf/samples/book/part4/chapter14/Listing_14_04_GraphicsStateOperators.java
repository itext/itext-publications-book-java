/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_14_04_GraphicsStateOperators extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter14/Listing_14_04_GraphicsStateOperators.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_14_04_GraphicsStateOperators().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());

        // line widths
        canvas.saveState();
        for (int i = 25; i > 0; i--) {
            canvas.setLineWidth((float) i / 10)
                    .moveTo(50, 806 - (5 * i))
                    .lineTo(320, 806 - (5 * i))
                    .stroke();
        }
        canvas.restoreState();

        // line cap
        canvas.moveTo(350, 800)
                .lineTo(350, 750)
                .moveTo(540, 800)
                .lineTo(540, 750)
                .stroke();

        canvas.saveState()
                .setLineWidth(8)
                .setLineCapStyle(PdfCanvasConstants.LineCapStyle.BUTT)
                .moveTo(350, 790)
                .lineTo(540, 790)
                .stroke()
                .setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND)
                .moveTo(350, 775)
                .lineTo(540, 775)
                .stroke()
                .setLineCapStyle(PdfCanvasConstants.LineCapStyle.PROJECTING_SQUARE)
                .moveTo(350, 760)
                .lineTo(540, 760)
                .stroke()
                .restoreState();

        // join miter
        canvas.saveState()
                .setLineWidth(8)
                .setLineJoinStyle(PdfCanvasConstants.LineJoinStyle.MITER)
                .moveTo(387, 700)
                .lineTo(402, 730)
                .lineTo(417, 700)
                .stroke()
                .setLineJoinStyle(PdfCanvasConstants.LineJoinStyle.ROUND)
                .moveTo(427, 700)
                .lineTo(442, 730)
                .lineTo(457, 700)
                .stroke()
                .setLineJoinStyle(PdfCanvasConstants.LineJoinStyle.BEVEL)
                .moveTo(467, 700)
                .lineTo(482, 730)
                .lineTo(497, 700)
                .stroke()
                .restoreState();

        // line dash
        canvas.saveState()
                .setLineWidth(3)
                .moveTo(50, 660)
                .lineTo(320, 660)
                .stroke()
                .setLineDash(6, 0)
                .moveTo(50, 650)
                .lineTo(320, 650)
                .stroke()
                .setLineDash(6, 3)
                .moveTo(50, 640)
                .lineTo(320, 640)
                .stroke()
                .setLineDash(15, 10, 5)
                .moveTo(50, 630)
                .lineTo(320, 630)
                .stroke();
        float[] dash1 = {10, 5, 5, 5, 20};
        canvas.setLineDash(dash1, 5)
                .moveTo(50, 620)
                .lineTo(320, 620)
                .stroke();
        float[] dash2 = {9, 6, 0, 6};
        canvas.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND)
                .setLineDash(dash2, 0)
                .moveTo(50, 610)
                .lineTo(320, 610)
                .stroke()
                .restoreState();

        // miter limit
        PdfFormXObject hooks = new PdfFormXObject(new Rectangle(300, 120));
        PdfCanvas hooksCanvas = new PdfCanvas(hooks, pdfDoc);
        hooksCanvas.setLineWidth(8)
                .moveTo(46, 50)
                .lineTo(65, 80)
                .lineTo(84, 50)
                .stroke()
                .moveTo(87, 50)
                .lineTo(105, 80)
                .lineTo(123, 50)
                .stroke()
                .moveTo(128, 50)
                .lineTo(145, 80)
                .lineTo(162, 50)
                .stroke()
                .moveTo(169, 50)
                .lineTo(185, 80)
                .lineTo(201, 50)
                .stroke()
                .moveTo(210, 50)
                .lineTo(225, 80)
                .lineTo(240, 50)
                .stroke();

        canvas.saveState()
                .setMiterLimit(2)
                .addXObject(hooks, 300, 600)
                .restoreState();

        canvas.saveState()
                .setMiterLimit(2.1f)
                .addXObject(hooks, 300, 550)
                .restoreState();

        pdfDoc.close();
    }
}
