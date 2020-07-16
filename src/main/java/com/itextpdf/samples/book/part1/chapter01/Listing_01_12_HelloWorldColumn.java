package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Listing_01_12_HelloWorldColumn {
    public static final String DEST =
            "./target/book/part1/chapter01/Listing_01_12_HelloWorldColumn.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_12_HelloWorldColumn().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);

        new Document(pdfDoc).showTextAligned("Hello World", 36, 788, TextAlignment.LEFT);

        //Close document
        pdfDoc.close();
    }

}
