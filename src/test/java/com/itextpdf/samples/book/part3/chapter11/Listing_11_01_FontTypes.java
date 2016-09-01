/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeCollection;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_11_01_FontTypes extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter11/Listing_11_01_FontTypes.pdf";
    public static String TEXT = "quick brown fox jumps over the lazy dog\nQUICK BROWN FOX JUMPS OVER THE LAZY DOG";
    public static String[][] FONTS = {
            {FontConstants.HELVETICA, PdfEncodings.WINANSI},
            {"./src/test/resources/font/cmr10.afm", PdfEncodings.WINANSI},
            {"./src/test/resources/font/cmr10.pfm", FontEncoding.FONT_SPECIFIC},
            {"./src/test/resources/font/FreeSans.ttf", PdfEncodings.WINANSI},
            {"./src/test/resources/font/FreeSans.ttf", PdfEncodings.IDENTITY_H},
            {"./src/test/resources/font/Puritan2.otf", PdfEncodings.WINANSI},
            {"./src/test/resources/font/ipam.ttc", PdfEncodings.IDENTITY_H},
            {"KozMinPro-Regular", "UniJIS-UCS2-H"}
    };

    public static void main(String[] agrs) throws Exception {
        new Listing_11_01_FontTypes().manipulatePdf(DEST);
    }

    @Override
    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font;
        for (int i = 0; i < FONTS.length; i++) {
            if (FONTS[i][0].endsWith(".ttc")) {
                TrueTypeCollection coll = new TrueTypeCollection(FONTS[i][0], FONTS[i][1]);
                font = PdfFontFactory.createFont(coll.getFontByTccIndex(0), FONTS[i][1], true);
            } else {
                font = PdfFontFactory.createFont(FONTS[i][0], FONTS[i][1], true);
            }
            doc.add(new Paragraph(String.format("Font file: %s with encoding %s", FONTS[i][0], FONTS[i][1])));
            doc.add(new Paragraph(String.format("iText class: %s", font.getClass().getName())));
            doc.add(new Paragraph(TEXT).setFont(font).setFontSize(12));
            ILineDrawer line = new SolidLine(0.5f);
            doc.add(new LineSeparator(line));
        }
        doc.close();
    }
}
