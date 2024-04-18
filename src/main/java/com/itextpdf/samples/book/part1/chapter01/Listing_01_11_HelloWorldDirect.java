package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.IOException;

public class Listing_01_11_HelloWorldDirect {
    public static final String DEST =
            "./target/book/part1/chapter01/Listing_01_11_HelloWorldDirect.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_11_HelloWorldDirect().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);

        //Initialize canvas, add page and write text to it
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas
                .saveState()
                .beginText()
                .moveText(36, 600)
                .setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 12)
                .showText("Hello World")
                .endText()
                .restoreState()
                .release();

        //close document
        pdfDoc.close();
    }

}
