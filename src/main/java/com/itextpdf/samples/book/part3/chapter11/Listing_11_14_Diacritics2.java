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

public class Listing_11_14_Diacritics2 {
    public static final String DEST =
            "./target/book/part3/chapter11/Listing_11_14_Diacritics2.pdf";
    public static final String MOVIE =
            "Tomten \u00a8ar far till alla barnen";
    public static final String[] FONTS = {
            /*"c:/windows/fonts/arial.ttf"*/"./src/main/resources/font/FreeSans.ttf",
            /*"c:/windows/fonts/cour.ttf"*/"./src/main/resources/font/LiberationMono-Regular.ttf"
    };

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_14_Diacritics2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Movie title: In Bed With Santa (Sweden)"));
        doc.add(new Paragraph("directed by Kjell Sundvall"));
        PdfFont f = PdfFontFactory.createFont(FONTS[0], PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
        f.getGlyph('\u00a8').setXAdvance((short)-450);
        doc.add(new Paragraph(MOVIE).setFont(f));
        f = PdfFontFactory.createFont(FONTS[1], PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
        f.getGlyph('\u00a8').setXAdvance((short)-600);
        doc.add(new Paragraph(MOVIE).setFont(f));
        doc.close();
    }
}
