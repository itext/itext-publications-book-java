/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Listing_11_13_Diacritics1 {
    public static final String DEST =
            "./target/book/part3/chapter11/Listing_11_13_Diacritics1.pdf";
    public static final String MOVIE =
            "\u0e1f\u0e49\u0e32\u0e17\u0e30\u0e25\u0e32\u0e22\u0e42\u0e08\u0e23";
    public static final String POSTER =
            "./src/main/resources/img/posters/0269217.jpg";
    public static final String[] FONTS = {
            /*"c:/windows/fonts/angsa.ttf"*/"./src/main/resources/font/NotoSansThai-Regular.ttf",
            /*"c:/windows/fonts/arialuni.ttf"*/"./src/main/resources/font/NotoSerifThai-Regular.ttf"
    };

    public static void main(String[] agrs) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_13_Diacritics1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        //Load the license file to use advanced typography features (Thai script)
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font;
        Image img = new Image(ImageDataFactory.create(POSTER));
        img.scale(0.5f, 0.5f);
        img.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 18f));
        img.setHorizontalAlignment(HorizontalAlignment.LEFT);
        doc.add(img);
        doc.add(new Paragraph(
                "Movie title: Tears of the Black Tiger (Thailand)"));
        doc.add(new Paragraph("directed by Wisit Sasanatieng"));
        for (int i = 0; i < 2; i++) {
            font = PdfFontFactory.createFont(FONTS[i], PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);
            doc.add(new Paragraph("Font: " + font.getFontProgram().getFontNames().getFontName()));
            doc.add(new Paragraph(MOVIE).setFont(font).setFontSize(20));
        }
        doc.close();
    }
}
