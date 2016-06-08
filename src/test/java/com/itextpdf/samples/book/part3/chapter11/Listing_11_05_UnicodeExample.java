/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfType0Font;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_11_05_UnicodeExample extends Listing_11_04_EncodingExample {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_05_UnicodeExample.pdf";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_05_UnicodeExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font;
        for (int i = 0; i < 4; i++) {
            font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
            // IDENTITY_H results in PdfType0Font and PdfType0Font supports cmap encoding
            doc.add(new Paragraph("Font: " + font.getFontProgram().getFontNames().getFontName()
                    + " with encoding: " + ((PdfType0Font)font).getCmap().getCmapName()));
            doc.add(new Paragraph(MOVIES[i][1]));
            doc.add(new Paragraph(MOVIES[i][2]));
            doc.add(new Paragraph(MOVIES[i][3]).setFont(font).setFontSize(12));
            doc.add(new Paragraph("\n"));
        }
        // close the document
        doc.close();
    }
}
