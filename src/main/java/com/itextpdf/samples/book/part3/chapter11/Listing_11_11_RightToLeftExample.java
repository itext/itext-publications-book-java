/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.BaseDirection;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;

public class Listing_11_11_RightToLeftExample {

    public static final String DEST = "./target/book/part3/chapter11/Listing_11_11_RightToLeftExample.pdf";
    /** A movie title. */
    public static final String MOVIE
            = "\u05d4\u05d0\u05e1\u05d5\u05e0\u05d5\u05ea \u05e9\u05dc \u05e0\u05d9\u05e0\u05d4";
    private static final String FONT = "src/main/resources/font/FreeSans.ttf";

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_11_RightToLeftExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Load the license file to use advanced typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc, PageSize.A4);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);
        document.add(new Paragraph("Movie title: Nina's Tragedies"));
        document.add(new Paragraph("directed by Savi Gabizon"));
        document.add(new Paragraph(MOVIE).setFont(font).setFontSize(14).setBaseDirection(BaseDirection.RIGHT_TO_LEFT));

        //Close document
        document.close();
    }
}
