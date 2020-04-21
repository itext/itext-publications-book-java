package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Listing_01_09_HelloWorldMemory {
    public static final String DEST = "./target/book/part1/chapter01/Listing_01_09_HelloWorldMemory.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_09_HelloWorldMemory().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        // We'll create the file in memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        //Add paragraph to the document
        doc.add(new Paragraph("Hello World!"));

        //Close document
        doc.close();

        // Let's write the file in memory to a file anyway
        FileOutputStream fos = new FileOutputStream(DEST);
        fos.write(baos.toByteArray());
        fos.close();
    }
}
