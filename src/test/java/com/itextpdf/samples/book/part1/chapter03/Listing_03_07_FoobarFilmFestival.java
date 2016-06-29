/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.util.Locale;


@Category(SampleTest.class)
public class Listing_03_07_FoobarFilmFestival extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part1/chapter03/Listing_03_07_FoobarFilmFestival.pdf";

    public static void main(String[] args) throws Exception {
        new Listing_03_07_FoobarFilmFestival().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        String foobar = "Foobar Film Festival";
        // Measuring a String in Helvetica
        PdfFont helvetica = PdfFontFactory.createFont(FontConstants.HELVETICA);
        float width_helv = helvetica.getWidth(foobar, 12);
        Text c = new Text(foobar + ": " + width_helv).setFont(helvetica);
        doc.add(new Paragraph(c));
        // Text element might have various properties (word/char spacing, scaling, kerning, and so on), so
        // in order to correctly get a width of a text(former chunk) we need to layout its renderer.
        // For simplified version use font's methods for getting width by string.

        float chunkWidth = helvetica.getWidth(foobar + ": " + width_helv);
        doc.add(new Paragraph(String.format(Locale.ENGLISH, "Chunk width: %f", chunkWidth)));
        // Use FreeSans instead of Times
        PdfFont freeSans = PdfFontFactory.createFont("./src/test/resources/font/FreeSans.ttf", PdfEncodings.IDENTITY_H);
        float width_freeSans = freeSans.getWidth(foobar, 12);
        c = new Text(foobar + ": " + width_freeSans).setFont(freeSans);
        doc.add(new Paragraph(c));


        chunkWidth = freeSans.getWidth(foobar + ": " + width_freeSans);
        doc.add(new Paragraph(String.format(Locale.ENGLISH, "Chunk width: %f", chunkWidth)));
        // doc.add(new Paragraph("\n"));
        // Ascent and descent of the String
        doc.add(new Paragraph("Ascent Helvetica: "
                + helvetica.getAscent(foobar, 12)));
        doc.add(new Paragraph("Ascent Times: "
                + freeSans.getAscent(foobar, 12)));
        doc.add(new Paragraph("Descent Helvetica: "
                + helvetica.getDescent(foobar, 12)));
        doc.add(new Paragraph("Descent Times: "
                + freeSans.getDescent(foobar, 12)));


        width_helv = getWidthPointKerned(helvetica,foobar,12);
        c = new Text(foobar + ": " + width_helv).setFont(helvetica);
        doc.add(new Paragraph(c));
//        doc.add(new Paragraph("\n"));
        // Drawing lines to see where the text is added
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage())
                .saveState()
                .setLineWidth(0.05f)
                .moveTo(400, 806)
                .lineTo(400, 626)
                .moveTo(508.7f, 806)
                .lineTo(508.7f, 626)
                .moveTo(280, 788)
                .lineTo(520, 788)
                .moveTo(280, 752)
                .lineTo(520, 752)
                .moveTo(280, 716)
                .lineTo(520, 716)
                .moveTo(280, 680)
                .lineTo(520, 680)
                .moveTo(280, 644)
                .lineTo(520, 644)
                .stroke()
                .restoreState();
        // Adding text with Canvas.showTextAligned()
        canvas.setFontAndSize(helvetica, 12);
        new Canvas(canvas, pdfDoc, pdfDoc.getFirstPage().getPageSize())
                .showTextAligned(foobar, 400, 788, TextAlignment.LEFT)
                .showTextAligned(foobar, 400, 752, TextAlignment.RIGHT)
                .showTextAligned(foobar, 400, 716, TextAlignment.CENTER)
                .showTextAligned(foobar, 400, 680, TextAlignment.CENTER, (float) Math.toRadians(30))
                .showTextAligned(foobar, 400, 644, TextAlignment.LEFT);

        // More lines to see where the text is added
        canvas
                .saveState()
                .setLineWidth(0.05f)
                .moveTo(200, 590)
                .lineTo(200, 410)
                .moveTo(400, 590)
                .lineTo(400, 410)
                .moveTo(80, 572)
                .lineTo(520, 572)
                .moveTo(80, 536)
                .lineTo(520, 536)
                .moveTo(80, 500)
                .lineTo(520, 500)
                .moveTo(80, 464)
                .lineTo(520, 464)
                .moveTo(80, 428)
                .lineTo(520, 428)
                .stroke()
                .restoreState();
        // Adding text with Document.showTextAligned() ColumnText.showTextAligned()
        Paragraph phrase = new Paragraph(foobar).setFont(freeSans);
        doc.showTextAligned(phrase, 200, 572, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        doc.showTextAligned(phrase, 200, 536, 1, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
        doc.showTextAligned(phrase, 200, 500, 1, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        doc.showTextAligned(phrase, 200, 464, 1, TextAlignment.CENTER, VerticalAlignment.BOTTOM, (float) Math.toRadians(30));
        doc.showTextAligned(phrase, 200, 428, 1, TextAlignment.CENTER, VerticalAlignment.BOTTOM, (float) Math.toRadians(-30));
        // Chunk attributes
        c = new Text(foobar).setFont(freeSans);
        c.setHorizontalScaling(0.5f);
        phrase = new Paragraph(c);
        doc.showTextAligned(phrase, 400, 572, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        c = new Text(foobar).setFont(freeSans);

        c.setSkew(15, 15);
        phrase = new Paragraph(c);
        doc.showTextAligned(phrase, 400, 536, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        doc.showTextAligned(phrase, 400, 536, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        c = new Text(foobar).setFont(freeSans);

        c.setSkew(0, 25);
        phrase = new Paragraph(c);
        doc.showTextAligned(phrase, 400, 500, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        c = new Text(foobar).setFont(freeSans);
        c.setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.STROKE)
                .setStrokeWidth(0.1f)
                .setStrokeColor(Color.RED);
        phrase = new Paragraph(c);
        doc.showTextAligned(phrase, 400, 464, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        c = new Text(foobar).setFont(freeSans);
        c.setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE).
                setStrokeWidth(1);
        phrase = new Paragraph(c);
        doc.showTextAligned(phrase, 400, 428, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);

        doc.close();
    }

    public float getWidthPointKerned(PdfFont font, String text, float fontSize) {
        float size = font.getWidth(text) * 0.001f * fontSize;
        if (!font.getFontProgram().hasKernPairs())
            return size;
        int len = text.length() - 1;
        int kern = 0;
        char c[] = text.toCharArray();
        for (int k = 0; k < len; ++k) {
            kern += font.getFontProgram().getKerning(c[k], c[k + 1]);
        }
        return size + kern * 0.001f * fontSize;
    }
}
