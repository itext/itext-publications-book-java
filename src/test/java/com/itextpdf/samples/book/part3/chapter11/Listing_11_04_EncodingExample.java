/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

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
public class Listing_11_04_EncodingExample extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter11/Listing_11_04_EncodingExample.pdf";
    public static final String FONT = /*"c:/windows/fonts/arialbd.ttf"*/"./src/test/resources/font/FreeSansBold.ttf";
    public static final String[][] MOVIES = {
            {
                    "Cp1252",
                    "A Very long Engagement (France)",
                    "directed by Jean-Pierre Jeunet",
                    "Un long dimanche de fian\u00e7ailles"
            },
            {
                    "Cp1250",
                    "No Man's Land (Bosnia-Herzegovina)",
                    "Directed by Danis Tanovic",
                    "Nikogar\u0161nja zemlja"
            },
            {
                    "Cp1251",
                    "You I Love (Russia)",
                    "directed by Olga Stolpovskaja and Dmitry Troitsky",
                    "\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f"
            },
            {
                    "Cp1253",
                    "Brides (Greece)",
                    "directed by Pantelis Voulgaris",
                    "\u039d\u03cd\u03c6\u03b5\u03c2"
            }
    };

    public static void main(String[] agrs) throws Exception {
        new Listing_11_04_EncodingExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfSimpleFont font;
        for (int i = 0; i < 4; i++) {
            font = (PdfSimpleFont) PdfFontFactory.createFont(FONT, MOVIES[i][0], true);
            doc.add(new Paragraph("Font: " + font.getFontProgram().getFontNames().getFontName()
                    + " with encoding: " + font.getFontEncoding().getBaseEncoding()));
            doc.add(new Paragraph(MOVIES[i][1]));
            doc.add(new Paragraph(MOVIES[i][2]));
            doc.add(new Paragraph(MOVIES[i][3]).setFont(font).setFontSize(12));
            doc.add(new Paragraph("\n"));
        }
        doc.close();
    }
}
