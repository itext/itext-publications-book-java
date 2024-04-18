package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfTextArray;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class Listing_14_09_TextStateOperators {
    public static final String DEST
            = "./target/book/part4/chapter14/Listing_14_09_TextStateOperators.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_14_09_TextStateOperators().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        String text = "AWAY again";
        canvas.beginText().
                // line 1
                        setFontAndSize(font, 16).
                moveText(36, 806).
                moveTextWithLeading(0, -24).
                showText(text).
                // line 2
                        setWordSpacing(20).
                newlineShowText(text).
                // line 3
                        setCharacterSpacing(10).
                newlineShowText(text).
                setWordSpacing(0).
                setCharacterSpacing(0).
                // line 4
                        setHorizontalScaling(50).
                newlineShowText(text).
                setHorizontalScaling(100).
                // line 5
                        newlineShowText(text).
                setTextRise(15).
                setFontAndSize(font, 12).
                setFillColor(ColorConstants.RED).
                showText("2").
                setFillColor(DeviceGray.BLACK).
                // line 6
                        setLeading(56).
                newlineShowText("Changing the leading: " + text).
                setLeading(24).
                newlineText();

        PdfTextArray textArray = new PdfTextArray();
        textArray.add(font.convertToBytes("A"));
        textArray.add(120);
        textArray.add(font.convertToBytes("W"));
        textArray.add(120);
        textArray.add(font.convertToBytes("A"));
        textArray.add(95);
        textArray.add(font.convertToBytes("Y again"));

        canvas.showText(textArray).
                endText();

        canvas.setFillColor(ColorConstants.BLUE).
                beginText().
                setTextMatrix(360, 770).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL).
                setFontAndSize(font, 24).
                showText(text).
                endText().

                beginText().
                setTextMatrix(360, 730).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.STROKE).
                setFontAndSize(font, 24).
                showText(text).
                endText().

                beginText().
                setTextMatrix(360, 690).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE).
                setFontAndSize(font, 24).
                showText(text).
                endText().

                beginText().
                setTextMatrix(360, 650).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.INVISIBLE).
                setFontAndSize(font, 24).
                showText(text).
                endText();

        PdfFormXObject xObject = new PdfFormXObject(new Rectangle(200, 36));
        PdfCanvas xObjectCanvas = new PdfCanvas(xObject, pdfDoc).setLineWidth(2);
        for (int i = 0; i < 6; i++) {
            xObjectCanvas.moveTo(0, i * 6);
            xObjectCanvas.lineTo(200, i * 6);
        }
        xObjectCanvas.stroke();

        canvas.saveState().
                beginText().
                setTextMatrix(360, 610).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_CLIP).
                setFontAndSize(font, 24).
                showText(text).
                endText().
                addXObjectAt(xObject, 360, 610).
                restoreState().

                saveState().
                beginText().
                setTextMatrix(360, 570).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.STROKE_CLIP).
                setFontAndSize(font, 24).
                showText(text).
                endText().
                addXObjectAt(xObject, 360, 570).
                restoreState().

                saveState().
                beginText().
                setTextMatrix(360, 530).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE_CLIP).
                setFontAndSize(font, 24).
                showText(text).
                endText().
                addXObjectAt(xObject, 360, 530).
                restoreState().

                saveState().
                beginText().
                setTextMatrix(360, 490).
                setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.CLIP).
                setFontAndSize(font, 24).
                showText(text).
                endText().
                addXObjectAt(xObject, 360, 490).
                restoreState();

        //Close document
        pdfDoc.close();
    }
}
