package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class Listing_11_06_FontFileAndSizes {
    public static final String[] RESULT = {
            "./target/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_not_embedded.pdf",
            "./target/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_embedded.pdf",
            "./target/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_embedded_less_glyphs.pdf",
            "./target/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_compressed.pdf",
            "./target/book/part3/chapter11/Listing_11_06_FontFileAndSizes_font_full.pdf"
    };

    public static final String DEST = RESULT[0];

    public static final String FONT
            = /*"c:/windows/fonts/arial.ttf"*/"./src/main/resources/font/FreeSans.ttf";
    public static String TEXT
            = "quick brown fox jumps over the lazy dog";
    public static String OOOO
            = "ooooo ooooo ooo ooooo oooo ooo oooo ooo";

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_06_FontFileAndSizes().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfFont font;
        PdfDocument pdfDoc;
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[0]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_NOT_EMBEDDED);
        writeAndClosePdf(pdfDoc, font, TEXT);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[1]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        writeAndClosePdf(pdfDoc, font, TEXT);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[2]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        writeAndClosePdf(pdfDoc, font, OOOO);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[3]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        writeAndClosePdf(pdfDoc, font, TEXT);
        pdfDoc = new PdfDocument(new PdfWriter(RESULT[4]));
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        font.setSubset(false);
        writeAndClosePdf(pdfDoc, font, TEXT);
    }

    public void writeAndClosePdf(PdfDocument pdfDoc, PdfFont font, String text) throws IOException {
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph(text).setFont(font).setFontSize(12));
        doc.close();
    }
}
