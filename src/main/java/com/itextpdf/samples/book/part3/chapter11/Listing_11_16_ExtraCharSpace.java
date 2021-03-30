package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;

public class Listing_11_16_ExtraCharSpace {
    public static final String DEST
            = "./target/book/part3/chapter11/Listing_11_16_ExtraCharSpace.pdf";
    public static final String MOVIE
            = "Aanrijding in Moscou";

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_16_ExtraCharSpace().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font1 = PdfFontFactory.createFont(/*"c:/windows/fonts/arial.ttf"*/"./src/main/resources/font/FreeSans.ttf",
                PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
        doc.add(new Paragraph("Movie title: Moscou, Belgium").setFont(font1));
        doc.add(new Paragraph("directed by Christophe Van Rompaey").setFont(font1));
        Text text = new Text(MOVIE).setFont(font1);
        text.setCharacterSpacing(10);
        doc.add(new Paragraph(text));
        doc.close();
    }
}
