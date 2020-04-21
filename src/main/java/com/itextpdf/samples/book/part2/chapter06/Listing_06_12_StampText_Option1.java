package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.IOException;

// Notice that this example actually is not from book
public class Listing_06_12_StampText_Option1 {

    public static final String DEST = "./target/book/part2/chapter06/Listing_06_12_StampText_Option1.pdf";
    public static final String SOURCE = "./src/main/resources/pdfs/source.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_12_StampText_Option1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize reader
        PdfReader reader = new PdfReader(SOURCE);

        //Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        //Initialize canvas and write to it
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.saveState().beginText().setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 12).
                moveText(36, 540).showText("Hello people!").endText().restoreState();
        canvas.release();

        //Close document
        pdfDoc.close();
    }
}
