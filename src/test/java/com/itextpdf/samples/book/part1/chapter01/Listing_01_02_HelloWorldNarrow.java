/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_01_02_HelloWorldNarrow extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter01/Listing_01_02_HelloWorldNarrow.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_01_02_HelloWorldNarrow().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        // Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        // Create custom pageSize
        PageSize pageSize = new PageSize(216f, 720f);

        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, pageSize);

        // Set margins
        doc.setMargins(108f, 72f, 180f, 36f);

        // Add paragraph to the document
        doc.add(new Paragraph("Hello World! Hello People! " +
                "Hello Sky! Hello Sun! Hello Moon! Hello Stars!"));

        // Close the document
        doc.close();
    }
}
