/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_11_06_FontFileAndSizes extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_not_embedded.pdf";
    public static final String[] RESULT = {
            "./target/test/resources/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_not_embedded.pdf",
            "./target/test/resources/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_embedded.pdf",
            "./target/test/resources/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_embedded_less_glyphs.pdf",
            "./target/test/resources/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_compressed.pdf",
            "./target/test/resources/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_full.pdf"
    };
    public static final String[] CMP_RESULT = {
            "./src/test/resources/book/part3/chapter11/cmp_Listing_11_06_FontFileAndSizes_font_not_embedded.pdf",
            "./src/test/resources/book/part3/chapter11/cmp_Listing_11_06_FontFileAndSizes_font_embedded.pdf",
            "./src/test/resources/book/part3/chapter11/cmp_Listing_11_06_FontFileAndSizes_font_embedded_less_glyphs.pdf",
            "./src/test/resources/book/part3/chapter11/cmp_Listing_11_06_FontFileAndSizes_font_compressed.pdf",
            "./src/test/resources/book/part3/chapter11/cmp_Listing_11_06_FontFileAndSizes_font_full.pdf"
    };
    public static final String FONT
            = /*"c:/windows/fonts/arial.ttf"*/"./src/test/resources/font/FreeSans.ttf";
    public static String TEXT
            = "quick brown fox jumps over the lazy dog";
    public static String OOOO
            = "ooooo ooooo ooo ooooo oooo ooo oooo ooo";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_06_FontFileAndSizes().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfFont font;
        PdfDocument pdfDoc;
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[0]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, false);
        writeAndClosePdf(pdfDoc, font, TEXT);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[1]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
        writeAndClosePdf(pdfDoc, font, TEXT);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[2]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
        writeAndClosePdf(pdfDoc, font, OOOO);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[3]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
        writeAndClosePdf(pdfDoc, font, TEXT);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[4]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
        font.setSubset(false);
        writeAndClosePdf(pdfDoc, font, TEXT);
    }

    public void writeAndClosePdf(PdfDocument pdfDoc, PdfFont font, String text) throws IOException {
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph(text).setFont(font).setFontSize(12));
        doc.close();
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        String outPath;
        for (int i = 0; i < RESULT.length; i++) {
            outPath = new File(RESULT[i]).getParent();
            if (compareXml) {
                if (!compareTool.compareXmls(RESULT[i], CMP_RESULT[i])) {
                    addError("The XML structures are different.");
                }
            } else {
                if (compareRenders) {
                    addError(compareTool.compareVisually(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                    addError(compareTool.compareLinkAnnotations(dest, cmp));
                } else {
                    addError(compareTool.compareByContent(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                }
                addError(compareTool.compareDocumentInfo(RESULT[i], CMP_RESULT[i]));
            }
        }
        if (errorMessage != null) Assert.fail(errorMessage);
    }
}
