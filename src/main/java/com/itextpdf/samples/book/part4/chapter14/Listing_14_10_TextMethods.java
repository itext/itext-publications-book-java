package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class Listing_14_10_TextMethods {
    public static final String DEST
            = "./target/book/part4/chapter14/Listing_14_10_TextMethods.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_14_10_TextMethods().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Load the license file to use advanced typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // draw helper lines
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.setLineWidth(0f)
                .moveTo(150, 600)
                .lineTo(150, 800)
                .moveTo(50, 760)
                .lineTo(250, 760)
                .moveTo(50, 700)
                .lineTo(250, 700)
                .moveTo(50, 640)
                .lineTo(250, 640)
                .stroke();
        // draw text
        String text = "AWAY again ";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        canvas.beginText()
                .setFontAndSize(font, 12)
                .setTextMatrix(50, 800)
                .showText(text);
        Canvas canvasModel = new Canvas(canvas, pdfDoc.getDefaultPageSize());
        canvasModel.showTextAligned(text + " Center", 150, 760, TextAlignment.CENTER);
        canvasModel.showTextAligned(text + " Right", 150, 700, TextAlignment.RIGHT);
        canvasModel.showTextAligned(text + " Left", 150, 640, TextAlignment.LEFT);
        canvasModel.showTextAlignedKerned(text + " Left", 150, 628,
                TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        canvas.setTextMatrix(0, 1, -1, 0, 300, 600);
        canvas.showText("Position 300,600, rotated 90 degrees.");
        for (int i = 0; i < 360; i += 30) {
            canvasModel.showTextAligned(text, 400, 700, TextAlignment.LEFT, (float) (i * Math.PI / 180));
        }
        canvas.endText();

        pdfDoc.close();
    }
}
