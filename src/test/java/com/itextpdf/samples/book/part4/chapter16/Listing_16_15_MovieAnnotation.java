/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfScreenAnnotation;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_16_15_MovieAnnotation extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_15_MovieAnnotation.pdf";
    public static final String RESOURCE = "./src/test/resources/img/foxdog.mpg";

    public static void main(String args[]) throws Exception {
        new Listing_16_15_MovieAnnotation().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfAnnotation screenAnnotation = new PdfScreenAnnotation(new Rectangle(200, 700, 200, 100));
        PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, RESOURCE,
                "Fox and Dog", "Fox and Dog", null, null, false);
        PdfAction action = PdfAction.createRendition("foxdog.mpg", spec, "video/mpeg", screenAnnotation);
        screenAnnotation.setAction(action);
        pdfDoc.addNewPage().addAnnotation(screenAnnotation);
        pdfDoc.close();
    }
}
