/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class Listing_13_01_PdfA {
    public static final String DEST = "./target/book/part4/chapter13/Listing_13_01_PdfA.pdf";
    public static final String FONT = /*"c:/windows/fonts/arial.ttf"*/"./src/test/resources/font/FreeSans.ttf";
    public static final String sourceFolder = "./src/test/resources/img/";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_01_PdfA().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        createPdfA(dest);
    }

    public void createPdfA(String dest) throws IOException, XMPException {
        InputStream is = new FileInputStream(sourceFolder + "sRGB Color Space Profile.icm");

        PdfADocument pdfADocument = new PdfADocument(new PdfWriter(dest),
                PdfAConformanceLevel.PDF_A_1B,
                new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", is));
        pdfADocument.addNewPage();

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.CP1252, true);
        Document doc = new Document(pdfADocument).add(new Paragraph("Hello World").setFont(font));
        doc.close();
    }
}
