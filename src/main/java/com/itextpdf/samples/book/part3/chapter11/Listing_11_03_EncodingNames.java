package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class Listing_11_03_EncodingNames {
    public static final String DEST
            = "./target/book/part3/chapter11/Listing_11_03_EncodingNames.pdf";
    public static final String[] FONT = {
            "./src/main/resources/font/Puritan2.otf",
            /*"c:/windows/fonts/arialbd.ttf"*/"./src/main/resources/font/FreeSans.ttf"
    };

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_03_EncodingNames().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        showEncodings(doc, FONT[0]);
        doc.add(new AreaBreak());
        showEncodings(doc, FONT[1]);
        doc.close();
    }

    public void showEncodings(Document doc, String fontConstant) throws IOException {
        PdfFont font = PdfFontFactory.createFont(fontConstant, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);

        doc.add(new Paragraph("PostScript name: " + font.getFontProgram().getFontNames().getFontName()));
        doc.add(new Paragraph("Available code pages:"));

        String[] encoding = ((TrueTypeFont)font.getFontProgram()).getCodePagesSupported();
        for (int i = 0; i < encoding.length; i++) {
            doc.add(new Paragraph("encoding[" + i + "] = " + encoding[i]));
        }
    }
}
