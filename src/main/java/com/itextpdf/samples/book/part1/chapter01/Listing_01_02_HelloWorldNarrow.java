package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Listing_01_02_HelloWorldNarrow {
    public static final String DEST =
            "./target/book/part1/chapter01/Listing_01_02_HelloWorldNarrow.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_02_HelloWorldNarrow().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        // Initialize writer
        PdfWriter writer = new PdfWriter(dest);

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
