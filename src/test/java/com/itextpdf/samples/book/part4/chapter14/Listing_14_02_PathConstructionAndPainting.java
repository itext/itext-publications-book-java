/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_14_02_PathConstructionAndPainting extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter14/Listing_14_02_PathConstructionAndPainting.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_14_02_PathConstructionAndPainting().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        // draw squares
        createSquares(canvas, 50, 720, 80, 20);
        new Canvas(canvas, pdfDoc, pdfDoc.getDefaultPageSize()).showTextAligned(
                new Paragraph("Methods moveTo(), lineTo(), stroke(), closePathStroke(), fill(), and closePathFill()"),
                50, 700, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        // draw Bezier curves
        createBezierCurves(canvas, 70, 600, 80, 670, 140, 690, 160, 630, 160);
        new Canvas(canvas, pdfDoc, pdfDoc.getDefaultPageSize()).showTextAligned(
                new Paragraph("Different curveTo() methods, followed by stroke()"),
                50, 580, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        // draw stars and circles
        createStarsAndCircles(canvas, 50, 470, 40, 20);
        new Canvas(canvas, pdfDoc, pdfDoc.getDefaultPageSize()).showTextAligned(
                new Paragraph("Methods fill(), eoFill(), newPath(), fillStroke(), and eoFillStroke()"),
                50, 450, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        // draw different shapes using convenience methods
        canvas
                .saveState()
                .setStrokeColor(new DeviceGray(0.2f))
                .setFillColor(new DeviceGray(0.9f))
                .arc(50, 270, 150, 330, 45, 270)
                .ellipse(170, 270, 270, 330)
                .circle(320, 300, 30)
                .roundRectangle(370, 270, 80, 60, 20)
                .fillStroke()
                .restoreState();
        Rectangle rect = new Rectangle(470, 270, 80, 60);
        Div div = new Div();
        div.setBorderBottom(new SolidBorder(new DeviceGray(0), 10));
        div.setBorderLeft(new SolidBorder(new DeviceGray(0.9f), 4));
        div.setBackgroundColor(new DeviceGray(0.4f));
        div.setFixedPosition(470, 270, 80);
        div.setHeight(60);

        new Canvas(canvas, pdfDoc, pdfDoc.getDefaultPageSize()).add(div);
        new Canvas(canvas, pdfDoc, pdfDoc.getDefaultPageSize()).showTextAligned(
                new Paragraph("Convenience methods"),
                50, 250, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        pdfDoc.close();
    }

    public void createSquares(PdfCanvas canvas,
                              float x, float y, float side, float gutter) {
        canvas.saveState()
                .setStrokeColor(new DeviceGray(0.2f))
                .setFillColor(new DeviceGray(0.9f))
                .moveTo(x, y)
                .lineTo(x + side, y)
                .lineTo(x + side, y + side)
                .lineTo(x, y + side)
                .stroke();
        x = x + side + gutter;
        canvas.moveTo(x, y)
                .lineTo(x + side, y)
                .lineTo(x + side, y + side)
                .lineTo(x, y + side)
                .closePathStroke();
        x = x + side + gutter;
        canvas.moveTo(x, y)
                .lineTo(x + side, y)
                .lineTo(x + side, y + side)
                .lineTo(x, y + side)
                .fill();
        x = x + side + gutter;
        canvas.moveTo(x, y)
                .lineTo(x + side, y)
                .lineTo(x + side, y + side)
                .lineTo(x, y + side)
                .fillStroke();
        x = x + side + gutter;
        canvas.moveTo(x, y)
                .lineTo(x + side, y)
                .lineTo(x + side, y + side)
                .lineTo(x, y + side)
                .closePathFillStroke()
                .restoreState();
    }

    public void createBezierCurves(PdfCanvas canvas, float x0, float y0,
                                   float x1, float y1, float x2, float y2, float x3, float y3, float distance) {
        canvas.moveTo(x0, y0)
                .lineTo(x1, y1)
                .moveTo(x2, y2)
                .lineTo(x3, y3)
                .moveTo(x0, y0)
                .curveTo(x1, y1, x2, y2, x3, y3);
        x0 += distance;
        x1 += distance;
        x2 += distance;
        x3 += distance;
        canvas.moveTo(x2, y2)
                .lineTo(x3, y3)
                .moveTo(x0, y0)
                .curveTo(x2, y2, x3, y3);
        x0 += distance;
        x1 += distance;
        x2 += distance;
        x3 += distance;
        canvas.moveTo(x0, y0)
                .lineTo(x1, y1)
                .moveTo(x0, y0)
                .curveTo(x1, y1, x3, y3)
                .stroke();

    }

    public static void createStarsAndCircles(PdfCanvas canvas,
                                             float x, float y, float radius, float gutter) {
        canvas.saveState()
                .setStrokeColor(new DeviceGray(0.2f))
                .setFillColor(new DeviceGray(0.9f));
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        canvas.fill();
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        canvas.eoFill();
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        canvas.newPath();
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, false);
        canvas.fillStroke();
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        canvas.eoFillStroke()
                .restoreState();
    }

    public static void createStar(PdfCanvas canvas, float x, float y) {
        canvas.moveTo(x + 10, y)
                .lineTo(x + 80, y + 60)
                .lineTo(x, y + 60)
                .lineTo(x + 70, y)
                .lineTo(x + 40, y + 90)
                .closePath();
    }

    public static void createCircle(PdfCanvas canvas, float x, float y,
                                    float r, boolean clockwise) {
        float b = 0.5523f;
        if (clockwise) {
            canvas.moveTo(x + r, y)
                    .curveTo(x + r, y - r * b, x + r * b, y - r, x, y - r)
                    .curveTo(x - r * b, y - r, x - r, y - r * b, x - r, y)
                    .curveTo(x - r, y + r * b, x - r * b, y + r, x, y + r)
                    .curveTo(x + r * b, y + r, x + r, y + r * b, x + r, y);
        } else {
            canvas.moveTo(x + r, y)
                    .curveTo(x + r, y + r * b, x + r * b, y + r, x, y + r)
                    .curveTo(x - r * b, y + r, x - r, y + r * b, x - r, y)
                    .curveTo(x - r, y - r * b, x - r * b, y - r, x, y - r)
                    .curveTo(x + r * b, y - r, x + r, y - r * b, x + r, y);
        }
    }
}
