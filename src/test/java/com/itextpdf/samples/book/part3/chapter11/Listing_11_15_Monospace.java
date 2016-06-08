/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfSimpleFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_11_15_Monospace extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_15_Monospace.pdf";
    public static final String MOVIE
            = "Aanrijding in Moscou";

    public static void main(String[] agrs) throws Exception {
        new Listing_11_15_Monospace().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font1 = PdfFontFactory.createFont(/*"c:/windows/fonts/arial.ttf"*/"./src/test/resources/font/FreeSans.ttf",
                PdfEncodings.CP1252, true);
        doc.add(new Paragraph("Movie title: Moscou, Belgium").setFont(font1));
        doc.add(new Paragraph("directed by Christophe Van Rompaey").setFont(font1));
        doc.add(new Paragraph(MOVIE).setFont(font1));
        PdfFont font2 = PdfFontFactory.createFont(/*"c:/windows/fonts/cour.ttf*/"./src/test/resources/font/LiberationMono-Regular.ttf",
                PdfEncodings.CP1252, true);
        doc.add(new Paragraph(MOVIE).setFont(font2));
        PdfSimpleFont font3 = (PdfSimpleFont) PdfFontFactory.createFont("./src/test/resources/font/FreeSansBold.ttf"/*"c:/windows/fonts/arialbd.ttf"*/,
                PdfEncodings.CP1252, true);
        int[] widths = font3.getFontProgram().getFontMetrics().getGlyphWidths();
        for (int k = 0; k < widths.length; ++k) {
            if (widths[k] != 0)
                widths[k] = 600;
        }
        font3.setForceWidthsOutput(true);
        doc.add(new Paragraph(MOVIE).setFont(font3));
        doc.close();
    }
}
