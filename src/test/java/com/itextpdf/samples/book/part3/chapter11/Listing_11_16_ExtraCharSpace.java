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
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_11_16_ExtraCharSpace extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_16_ExtraCharSpace.pdf";
    public static final String MOVIE
            = "Aanrijding in Moscou";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_16_ExtraCharSpace().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font1 = PdfFontFactory.createFont(/*"c:/windows/fonts/arial.ttf"*/"./src/test/resources/font/FreeSans.ttf",
                PdfEncodings.CP1252, true);
        doc.add(new Paragraph("Movie title: Moscou, Belgium").setFont(font1));
        doc.add(new Paragraph("directed by Christophe Van Rompaey").setFont(font1));
        Text text = new Text(MOVIE).setFont(font1);
        text.setCharacterSpacing(10);
        doc.add(new Paragraph(text));
        doc.close();
    }
}
