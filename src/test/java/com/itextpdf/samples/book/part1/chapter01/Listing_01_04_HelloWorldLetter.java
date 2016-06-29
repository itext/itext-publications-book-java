/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;

@Category(SampleTest.class)
public class Listing_01_04_HelloWorldLetter extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter01/Listing_01_04_HelloWorldLetter.pdf";

    /**
     * PageSize letter constant
     */
    public static final PageSize LETTER = new PageSize(612, 792);

    public static void main(String args[]) throws IOException {
        new Listing_01_04_HelloWorldLetter().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        // Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, LETTER);

        // Add paragraph to the document
        doc.add(new Paragraph("Hello World!"));

        // Close document
        doc.close();
    }
}
