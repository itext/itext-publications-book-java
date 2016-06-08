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
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;

public class Listing_11_18_Ligatures2 extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part3/chapter11/Listing_11_18_Ligatures2.pdf";
    /**
     * Correct movie title.
     */
    public static final String MOVIE
            = "\u0644\u0648\u0631\u0627\u0646\u0633 \u0627\u0644\u0639\u0631\u0628";
    /**
     * Correct movie title.
     */
    public static final String MOVIE_WITH_SPACES
            = "\u0644 \u0648 \u0631 \u0627 \u0646 \u0633   \u0627 \u0644 \u0639 \u0631 \u0628";

    // Note that english glyphs are not supported by this fonts, so you will see some squares instead of english text.
    // Use any other font supporting latin to test englihs+arabic combination
    private static final String FONT = "src/test/resources/font/NotoNaskhArabic-Regular.ttf";
    // "c:/windows/fonts/arialuni.ttf"
    // c:/windows/fonts/arial.ttf

    public static void main(String[] args) throws Exception {
        new Listing_11_18_Ligatures2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Load the license file to use advanced typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        PdfFont bf = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
        document.add(new Paragraph("Movie title: Lawrence of Arabia (UK)"));
        document.add(new Paragraph("directed by David Lean"));
        document.add(new Paragraph("Autodetect: " + MOVIE).setFont(bf).setFontSize(20));

        Style arabic = new Style().setTextAlignment(TextAlignment.RIGHT).setBaseDirection(BaseDirection.RIGHT_TO_LEFT).
                setFontSize(20).setFont(bf);

        document.add(new Paragraph("Wrong: " + MOVIE_WITH_SPACES).addStyle(arabic));
        document.add(new Paragraph(MOVIE).setFontScript(Character.UnicodeScript.ARABIC).addStyle(arabic));

        //Close document
        document.close();
    }
}
