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
import com.itextpdf.kernel.pdf.layer.PdfLayerMembership;
import com.itextpdf.kernel.pdf.layer.PdfVisibilityExpression;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_15_07_LayerMembershipExample2 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter15/Listing_15_07_LayerMembershipExample2.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_15_07_LayerMembershipExample2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_6));

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage())
                .setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 18);

        PdfLayer dog = new PdfLayer("layer 1", pdfDoc);
        PdfLayer tiger = new PdfLayer("layer 2", pdfDoc);
        PdfLayer lion = new PdfLayer("layer 3", pdfDoc);
        PdfLayerMembership cat = new PdfLayerMembership(pdfDoc);
        PdfVisibilityExpression ve1 = new PdfVisibilityExpression(PdfName.Or);
        ve1.addOperand(tiger);
        ve1.addOperand(lion);
        cat.setVisibilityExpression(ve1);
        PdfLayerMembership no_cat = new PdfLayerMembership(pdfDoc);
        PdfVisibilityExpression ve2 = new PdfVisibilityExpression(PdfName.Not);
        ve2.addOperand(ve1);
        no_cat.setVisibilityExpression(ve2);
        canvas
                .beginLayer(dog)
                .beginText()
                .moveText(50, 725)
                .showText("dog")
                .endText()
                .endLayer();
        canvas
                .beginLayer(tiger)
                .beginText()
                .moveText(50, 700)
                .showText("tiger")
                .endText()
                .endLayer();
        canvas
                .beginLayer(lion)
                .beginText()
                .moveText(50, 675)
                .showText("lion")
                .endText()
                .endLayer();
        canvas
                .beginLayer(cat)
                .beginText()
                .moveText(50, 650)
                .showText("cat")
                .endText()
                .endLayer();
        canvas
                .beginLayer(no_cat)
                .beginText()
                .moveText(50, 650)
                .showText("no cat")
                .endText()
                .endLayer();

        //Close document
        pdfDoc.close();
    }
}
