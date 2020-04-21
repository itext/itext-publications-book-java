package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Listing_01_05_HelloWorldLandscape1 {
    public static final String DEST =
            "./target/book/part1/chapter01/Listing_01_05_HelloWorldLandscape1.pdf";

    /**
     * PageSize letter constant
     */
    public static final PageSize LETTER = new PageSize(612, 792);

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_05_HelloWorldLandscape1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        // Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, LETTER.rotate());

        // Add paragraph to the document
        doc.add(new Paragraph("Hello World!"));

        // Close document
        doc.close();
    }
}
