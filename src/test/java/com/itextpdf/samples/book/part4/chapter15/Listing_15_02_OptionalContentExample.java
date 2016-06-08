/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_15_02_OptionalContentExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter15/Listing_15_02_OptionalContentExample.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_15_02_OptionalContentExample().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_5));

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Working with layers and writing to canvas.
        PdfLayer nested = new PdfLayer("Nested layers", pdfDoc);
        PdfLayer nested_1 = new PdfLayer("Nested layer 1", pdfDoc);
        PdfLayer nested_2 = new PdfLayer("Nested layer 2", pdfDoc);
        nested.addChild(nested_1);
        nested.addChild(nested_2);
        nested_2.setLocked(true);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage())
                .setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 18);
        canvas
                .beginLayer(nested)
                .beginText()
                .moveText(50, 750)
                .showText("nested layers")
                .endText()
                .endLayer();
        canvas
                .beginLayer(nested_1)
                .beginText()
                .moveText(100, 780)
                .showText("nested layer 1")
                .endText()
                .endLayer();
        canvas
                .beginLayer(nested_2)
                .beginText()
                .moveText(100, 720)
                .showText("nested layer 2")
                .endText()
                .endLayer();

        PdfLayer group = PdfLayer.createTitle("Grouped layers", pdfDoc);
        PdfLayer layer1 = new PdfLayer("Group: layer 1", pdfDoc);
        PdfLayer layer2 = new PdfLayer("Group: layer 2", pdfDoc);
        group.addChild(layer1);
        group.addChild(layer2);
        canvas
                .beginLayer(layer1)
                .beginText()
                .moveText(50, 700)
                .showText("layer 1 in the group")
                .endText()
                .endLayer();
        canvas
                .beginLayer(layer2)
                .beginText()
                .moveText(50, 675)
                .showText("layer 2 in the group")
                .endText()
                .endLayer();

        PdfLayer radiogroup = PdfLayer.createTitle("Radio group", pdfDoc);
        PdfLayer radio1 = new PdfLayer("Radiogroup: layer 1", pdfDoc);
        radio1.setOn(true);
        PdfLayer radio2 = new PdfLayer("Radiogroup: layer 2", pdfDoc);
        radio2.setOn(false);
        PdfLayer radio3 = new PdfLayer("Radiogroup: layer 3", pdfDoc);
        radio3.setOn(false);
        radiogroup.addChild(radio1);
        radiogroup.addChild(radio2);
        radiogroup.addChild(radio3);
        List<PdfLayer> options = Arrays.asList(radio1, radio2, radio3);
        PdfLayer.addOCGRadioGroup(pdfDoc, options);
        canvas
                .beginLayer(radio1)
                .beginText()
                .moveText(50, 600)
                .showText("option 1")
                .endText()
                .endLayer();
        canvas
                .beginLayer(radio2)
                .beginText()
                .moveText(50, 575)
                .showText("option 2")
                .endText()
                .endLayer();
        canvas
                .beginLayer(radio3)
                .beginText()
                .moveText(50, 550)
                .showText("option 3")
                .endText()
                .endLayer();

        PdfLayer not_printed = new PdfLayer("not printed", pdfDoc);
        not_printed.setOnPanel(false);
        not_printed.setPrint("Print", false);
        canvas
                .beginLayer(not_printed)
                .beginText()
                .moveText(300, 700)
                .showText("PRINT THIS PAGE")
                .endText()
                .endLayer();

        PdfLayer zoom = new PdfLayer("Zoom 0.75-1.25", pdfDoc);
        zoom.setOnPanel(false);
        zoom.setZoom(0.75f, 1.25f);
        canvas
                .beginLayer(zoom)
                .beginText()
                .moveText(30, 530)
                .showText("Only visible if the zoomfactor is between 75 and 125%")
                .endText()
                .endLayer()
                .release();

        //Close document
        pdfDoc.close();
    }
}
