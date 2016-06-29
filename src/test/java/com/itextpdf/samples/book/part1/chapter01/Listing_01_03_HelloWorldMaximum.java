/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;

@Category(SampleTest.class)
/**
 * Creates a PDF with the biggest possible page size.
 */
public class Listing_01_03_HelloWorldMaximum extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter01/Listing_01_03_HelloWorldMaximum.pdf";

    /**
     * Creates a PDF file: hello_maximum.pdf
     * Important notice: the PDF is valid (in conformance with
     * ISO-32000), but some PDF viewers won't be able to render
     * the PDF correctly due to their own limitations.
     *
     * @param args no arguments needed
     */
    public static void main(String args[]) throws IOException {
        new Listing_01_03_HelloWorldMaximum().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        // Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        // Create maximum pageSize
        PageSize pageSize = new PageSize(14400, 14400);

        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, pageSize);

        // Add paragraph to the document
        doc.add(new Paragraph("Hello World!"));

        // Change the user unit
        pdfDoc.getPage(1).getPdfObject().put(PdfName.UserUnit, new PdfNumber(75000f));

        // Close document
        doc.close();
    }
}
