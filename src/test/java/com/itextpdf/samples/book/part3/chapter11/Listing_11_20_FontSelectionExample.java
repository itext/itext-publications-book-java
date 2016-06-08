/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore("Font selector not implemented")
@Category(SampleTest.class)
public class Listing_11_20_FontSelectionExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_20_FontSelectionExample.pdf";
    public static final String TEXT
            = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
            + "\u7121\u540d (Nameless), \u6b98\u528d (Broken Sword), "
            + "\u98db\u96ea (Flying Snow), \u5982\u6708 (Moon), "
            + "\u79e6\u738b (the King), and \u9577\u7a7a (Sky).";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_20_FontSelectionExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        // FontSelector selector = new FontSelector();
        PdfFont font1 = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        // f1.setColor(BaseColor.BLUE); // 12
        PdfFont font2 = PdfFontFactory.createFont(FontProgramFactory.createFont("MSung-Light"), "UniCNS-UCS2-H", false);
        // f2.setColor(BaseColor.RED);
        // selector.addFont(f1);
        // selector.addFont(f2);
        // Paragraph ph = selector.process(TEXT);
        // doc.add(ph);
        // step 5: we close the document
        doc.close();
    }
}
