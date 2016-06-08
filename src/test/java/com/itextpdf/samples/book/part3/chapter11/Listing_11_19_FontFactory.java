/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_11_19_FontFactory extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_19_FontFactory.pdf";
    public static final String FONT_DIR
            = "./src/test/resources/book/part3/chapter11/font/";

    public static void main(String[] args) throws Exception {
        new Listing_11_19_FontFactory().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        doc.add(new Paragraph("Times-Roman").setFont(font));
        PdfFont fontBold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        doc.add(new Paragraph("Times-Roman, Bold").setFont(fontBold));
        doc.add(new Paragraph("\n"));

        FontProgramFactory.registerFont(FONT_DIR + "EBGaramond12-Regular.ttf", "my_bold_font");
        PdfFont myBoldFont = PdfFontFactory.createRegisteredFont("my_bold_font");
        doc.add(new Paragraph(font.getFontProgram().getFontNames().getFontName()).setFont(myBoldFont));
        String[][] name = myBoldFont.getFontProgram().getFontNames().getFullName();
        for (int i = 0; i < name.length; i++) {
            doc.add(new Paragraph(name[i][3] + " (" + name[i][0]
                    + "; " + name[i][1] + "; " + name[i][2] + ")"));
        }

        PdfFont myBoldFont2 = PdfFontFactory.createRegisteredFont("EB Garamond 12 Regular");
        doc.add(new Paragraph("EB Garamond 12").setFont(myBoldFont2));
        doc.add(new Paragraph("\n"));

        doc.add(new Paragraph("Registered fonts:"));
        // "./src/test/resources/font" include so many fonts that we decided to use the other directory
        FontProgramFactory.registerFontDirectory(FONT_DIR);
        for (String f : FontProgramFactory.getRegisteredFonts()) {
            doc.add(new Paragraph(f).setFont(PdfFontFactory.createRegisteredFont(f, "", true)));
        }
        doc.add(new Paragraph("\n"));

        PdfFont cmr10 = PdfFontFactory.createRegisteredFont("cmr10", FontEncoding.FONT_SPECIFIC, true);
        doc.add(new Paragraph("Computer Modern").setFont(cmr10));
        doc.add(new Paragraph("\n"));

        FontProgramFactory.registerSystemFontDirectories();
        for (String f : FontProgramFactory.getRegisteredFontFamilies()) {
            doc.add(new Paragraph(f));
        }
        doc.add(new Paragraph("\n"));

        PdfFont garamond = PdfFontFactory.createRegisteredFont("EB Garamond", PdfEncodings.WINANSI, true);
        doc.add(new Paragraph("EB Garamond").setFont(garamond));
        PdfFont garamondItalic = PdfFontFactory.createRegisteredFont("EB Garamond", PdfEncodings.WINANSI, true, FontConstants.ITALIC);
        doc.add(new Paragraph("EB Garamond Italic").setFont(garamondItalic).setFontSize(12));

        doc.close();
    }
}
