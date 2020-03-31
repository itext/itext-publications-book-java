/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_09_RotatePages {
    public static final String DEST
            = "./target/book/part4/chapter13/Listing_13_09_RotatePages.pdf";
    public static final String MOVIE_TEMPLATES
            = "./src/main/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_09_RotatePages().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        changePdf(MOVIE_TEMPLATES, dest);
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        int n = pdfDoc.getNumberOfPages();
        PdfDictionary pageDict;
        int rot;
        for (int i = 1; i <= n; i++) {
            rot = pdfDoc.getPage(i).getRotation();
            pageDict = pdfDoc.getPage(i).getPdfObject();
            pageDict.put(PdfName.Rotate, new PdfNumber(rot + 90));
        }
        pdfDoc.close();
    }
}