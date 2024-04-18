package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

public class Listing_03_02_GraphicsStateStack {
    public static final String DEST = "./target/book/part1/chapter03/Listing_03_02_GraphicsStateStack.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_02_GraphicsStateStack().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(new PageSize(200, 120));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());

        canvas.setFillColor(new DeviceRgb(0xff, 0x45, 0x00)).
                // fill a rectangle in state 1
                rectangle(10, 10, 60, 60).
                fill().
                saveState().
                // state 2;
                setLineWidth(3).
                setFillColor(new DeviceRgb(0x8b, 0x00, 0x00)).
                // fill and stroke a rectangle in state 2
                rectangle(40, 20, 60, 60).
                fillStroke().
                saveState().
                // state 3:
                concatMatrix(1, 0, 0.1f, 1, 0, 0).
                setStrokeColor(new DeviceRgb(0xff, 0x45, 0x00)).
                setFillColor(new DeviceRgb(0xff, 0xd7, 0x00)).
                // fill and stroke a rectangle in state 3
                rectangle(70, 30, 60, 60).
                fillStroke().
                restoreState().
                // stroke a rectangle in state 2
                rectangle(100, 40, 60, 60).
                stroke().
                restoreState().
                // fill and stroke a rectangle in state 1
                rectangle(130, 50, 60, 60).
                fillStroke();

        canvas.release();

        pdfDoc.close();
    }
}
