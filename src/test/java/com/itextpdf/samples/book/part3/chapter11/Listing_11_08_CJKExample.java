/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore("See DEVSIX-660")
@Category(SampleTest.class)
public class Listing_11_08_CJKExample extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part3/chapter11/Listing_11_08_CJKExample.pdf";
    public static final String[][] MOVIES = {
            {
                    "STSong-Light", "UniGB-UCS2-H",
                    "Movie title: House of The Flying Daggers (China)",
                    "directed by Zhang Yimou",
                    "\u5341\u950a\u57cb\u4f0f"
            },
            {
                    "KozMinPro-Regular", "UniJIS-UCS2-H",
                    "Movie title: Nobody Knows (Japan)",
                    "directed by Hirokazu Koreeda",
                    "\u8ab0\u3082\u77e5\u3089\u306a\u3044"
            },
            {
                    "HYGoThic-Medium", "UniKS-UCS2-H",
                    "Movie title: '3-Iron' aka 'Bin-jip' (South-Korea)",
                    "directed by Kim Ki-Duk",
                    "\ube48\uc9d1"
            }
    };

    public static void main(String[] agrs) throws Exception {
        new Listing_11_08_CJKExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font;
        for (int i = 0; i < 3; i++) {
            font = PdfFontFactory.createFont(MOVIES[i][0], MOVIES[i][1], false);
            doc.add(new Paragraph(font.getFontProgram().getFontNames().getFontName()).setFont(font));
            for (int j = 2; j < 5; j++)
                doc.add(new Paragraph(MOVIES[i][j]).setFont(font));
            doc.add(new Paragraph("\n"));
        }
        doc.close();
    }
}
