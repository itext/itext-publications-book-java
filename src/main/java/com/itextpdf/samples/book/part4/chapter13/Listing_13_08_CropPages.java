package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_08_CropPages {
    public static final String DEST = "./target/book/part4/chapter13/Listing_13_08_CropPages.pdf";
    public static final String MOVIE_TEMPLATES
            = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_08_CropPages().manipulatePdf(DEST);
    }

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
