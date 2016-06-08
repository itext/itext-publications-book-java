/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(SampleTest.class)
public class Listing_06_03_SelectPages extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part2/chapter06/Listing_06_03_SelectPages_copy.pdf";
    public static final String MOVIE_TEMPLATES =
            "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_06_03_SelectPages().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfReader reader = new PdfReader(MOVIE_TEMPLATES);
        manipulateWithCopy(4, 8, reader);
    }

    private static void manipulateWithCopy(int pageFrom, int pageTo, PdfReader reader) throws IOException {
        PdfDocument srcDoc = new PdfDocument(reader);
        PdfDocument copy = new PdfDocument(new PdfWriter(DEST));
        copy.initializeOutlines();
        srcDoc.copyPagesTo(pageFrom, pageTo, copy);
        copy.close();
        srcDoc.close();
    }
}
