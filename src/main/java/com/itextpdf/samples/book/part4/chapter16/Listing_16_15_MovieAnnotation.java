package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfScreenAnnotation;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;

import java.io.File;

public class Listing_16_15_MovieAnnotation {
    public static final String DEST = "./target/book/part4/chapter16/Listing_16_15_MovieAnnotation.pdf";
    public static final String RESOURCE = "./src/main/resources/img/foxdog.mpg";

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_16_15_MovieAnnotation().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfScreenAnnotation screenAnnotation = new PdfScreenAnnotation(new Rectangle(200, 700, 200, 100));
        PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, RESOURCE,
                "Fox and Dog", "Fox and Dog", null, null);
        PdfAction action = PdfAction.createRendition("foxdog.mpg", spec, "video/mpeg", screenAnnotation);
        screenAnnotation.setAction(action);
        pdfDoc.addNewPage().addAnnotation(screenAnnotation);
        pdfDoc.close();
    }
}
