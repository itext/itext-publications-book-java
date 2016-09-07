/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeCollection;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_11_02_TTCExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_02_TTCExample.pdf";
    // Notice that we'va changed windows MS Gothic to IPA Gothic so the results in comparison with itext5 are different
    public static final String FONT
            = "./src/test/resources/font/ipam.ttc";
    // public static final String FONT = "c:/windows/fonts/msgothic.ttc";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_02_TTCExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font;
        TrueTypeCollection coll = new TrueTypeCollection(FONT);
        for (int i = 0; i < coll.getTTCSize(); i++) {
            font = PdfFontFactory.createFont(coll.getFontByTccIndex(i), PdfEncodings.IDENTITY_H, true);
            doc.add(new Paragraph("font " + i + ": " + coll.getFontByTccIndex(i).getFontNames().getFontName())
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("Rash\u00f4mon")
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("Directed by Akira Kurosawa")
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("\u7f85\u751f\u9580")
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("\n"));
        }
        doc.close();
    }
}
