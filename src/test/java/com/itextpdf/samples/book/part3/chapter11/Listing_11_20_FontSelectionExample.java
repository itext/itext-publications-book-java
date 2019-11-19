/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;

public class Listing_11_20_FontSelectionExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_20_FontSelectionExample.pdf";

    public static final String TEXT
            = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
            + "\u7121\u540d (Nameless),\n"
            + "\u6b98\u528d (Broken Sword),\n"
            + "\u98db\u96ea (Flying Snow),\n"
            + "\u5982\u6708 (Moon),\n"
            + "\u79e6\u738b (the King),\n"
            + "\u9577\u7a7a (Sky).";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_20_FontSelectionExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4);
        Paragraph paragraph = new Paragraph(TEXT);

        FontProvider provider = new FontProvider();
        provider.addFont(StandardFonts.TIMES_ROMAN);
        provider.addFont("KozMinPro-Regular", "UniJIS-UCS2-H");

        // Adding fonts to the font provider
        doc.setFontProvider(provider);

        // Set font family to the particular element what will lead to triggering iText mechanism to pick up the best
        // font for the element from provider's fonts    
        paragraph.setFontFamily("Times-Roman");

        doc.add(paragraph);
        doc.close();
    }
}
