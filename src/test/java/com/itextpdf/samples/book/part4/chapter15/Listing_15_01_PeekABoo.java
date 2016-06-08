/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_15_01_PeekABoo extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter15/Listing_15_01_PickABoo.pdf";
    public static String RESULT2
            = "./target/test/resources/book/part4/chapter15/Listing_15_01_PickABoo2.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_15_01_PeekABoo().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(DEST, true);
        createPdf(RESULT2, false);
    }

    public void createPdf(String dest, boolean on) throws IOException {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_5));

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);

        pdfDoc.getCatalog().setPageLayout(PdfName.UseOC);

        //Write to canvas
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfLayer layer = new PdfLayer("Do you see me?", pdfDoc);
        layer.setOn(on);
        canvas.beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 18)
                .moveText(50, 760)
                .showText("Do you see me?")
                .beginLayer(layer)
                .moveText(0, -30)
                .showText("Peek-A-Boo!!!")
                .endLayer()
                .endText()
                .release();

        //Close document
        pdfDoc.close();
    }


}
