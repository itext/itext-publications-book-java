package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;

public class Listing_06_21_Concatenate {
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_21_Concatenate.pdf";
    public static final String MOVIE_LINKS1 =
            "./src/main/resources/pdfs/cmp_Listing_02_22_MovieLinks1.pdf";
    public static final String MOVIE_HISTORY =
            "./src/main/resources/pdfs/cmp_Listing_02_24_MovieHistory.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_21_Concatenate().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize source document 1
        PdfDocument sourceDoc1 = new PdfDocument(new PdfReader(MOVIE_LINKS1));
        int n1 = sourceDoc1.getNumberOfPages();

        //Initialize source document 2
        PdfDocument sourceDoc2 = new PdfDocument(new PdfReader(MOVIE_HISTORY));
        int n2 = sourceDoc2.getNumberOfPages();

        //Initialize destination document
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));

        for (int i = 1; i <= n1; i++) {
            PdfPage page = sourceDoc1.getPage(i).copyTo(resultDoc);
            resultDoc.addPage(page);
        }

        for (int i = 1; i <= n2; i++) {
            PdfPage page = sourceDoc2.getPage(i).copyTo(resultDoc);
            resultDoc.addPage(page);
        }

        //Close documents
        resultDoc.close();
        sourceDoc1.close();
        sourceDoc2.close();
    }
}
