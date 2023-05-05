/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.BaseDirection;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;

public class Listing_11_18_Ligatures2 {

    public static final String DEST = "./target/book/part3/chapter11/Listing_11_18_Ligatures2.pdf";
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
    private static final String FONT = "src/main/resources/font/NotoNaskhArabic-Regular.ttf";
    // "c:/windows/fonts/arialuni.ttf"
    // c:/windows/fonts/arial.ttf

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_18_Ligatures2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Load the license file to use advanced typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        PdfFont bf = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);
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
