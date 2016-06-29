/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;

@Category(SampleTest.class)
public class Listing_12_01_MetadataPdf extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter12/Listing_12_01_MetadataPdf.pdf";
    public static final String RESULT2
            = "./target/test/resources/book/part3/chapter12/Listing_12_01_MetadataPdf_pdf_metadata_changed.pdf";
    public static void main(String args[]) throws IOException {
        new Listing_12_01_MetadataPdf().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(DEST);
        changePdf(DEST, RESULT2);
    }

    public void createPdf(String dest) throws FileNotFoundException {
        //Initialize pdf document and document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info
                .setTitle("Hello World example")
                .setAuthor("Bruno Lowagie")
                .setSubject("This example shows how to add metadata")
                .setKeywords("Metadata, iText, PDF")
                .setCreator("My program using iText");

        doc.add(new Paragraph("Hello World"));

        doc.close();
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("Hello World stamped");
        info.setSubject("Hello World with changed metadata");
        info.setKeywords("iText in Action, PdfStamper");
        info.setCreator("Silly standalone example");
        info.setAuthor("Also Bruno Lowagie");
        pdfDoc.close();
    }
}
