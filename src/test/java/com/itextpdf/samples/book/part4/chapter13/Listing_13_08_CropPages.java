/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_13_08_CropPages extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter13/Listing_13_08_CropPages.pdf";
    public static final String MOVIE_TEMPLATES 
            = "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        new Listing_13_08_CropPages().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        changePdf(MOVIE_TEMPLATES, dest);
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        int n = pdfDoc.getNumberOfPages();
        PdfDictionary pageDict;
        PdfArray rect = new PdfArray(new int[]{55, 76, 560, 816});
        for (int i = 1; i <= n; i++) {
            pageDict = pdfDoc.getPage(i).getPdfObject();
            pageDict.put(PdfName.CropBox, rect);
        }
        pdfDoc.close();
    }
}
