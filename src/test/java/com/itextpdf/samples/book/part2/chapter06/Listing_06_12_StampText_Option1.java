/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

// Notice that this example actually is not from book
@Category(SampleTest.class)
public class Listing_06_12_StampText_Option1 extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part2/chapter06/Listing_06_12_StampText_Option1.pdf";
    public static final String SOURCE = "./src/test/resources/pdfs/source.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_06_12_StampText_Option1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize reader
        FileInputStream fis = new FileInputStream(SOURCE);
        PdfReader reader = new PdfReader(fis);

        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        //Initialize canvas and write to it
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.saveState().beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12).
                moveText(36, 540).showText("Hello people!").endText().restoreState();
        canvas.release();

        //Close document
        pdfDoc.close();
    }
}
