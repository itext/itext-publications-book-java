package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.layer.PdfLayer;

import java.io.File;
import java.io.IOException;

public class Listing_15_01_PeekABoo {
    public static final String[] RESULT = {
            "./target/book/part4/chapter15/Listing_15_01_PickABoo.pdf",
            "./target/book/part4/chapter15/Listing_15_01_PickABoo2.pdf"
    };

    public static final String DEST = RESULT[0];

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_15_01_PeekABoo().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(DEST, true);
        createPdf(RESULT[1], false);
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
        canvas.beginText().setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 18)
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
