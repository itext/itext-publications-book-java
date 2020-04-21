package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Listing_01_13_HelloZip {
    public static final String DEST = "./target/book/part1/chapter01/Listing_01_13_HelloZip.zip";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_13_HelloZip().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        // Creating a zip file with different PDF documents
        FileOutputStream fos = new FileOutputStream(DEST);
        ZipOutputStream zip = new ZipOutputStream(fos);
        for (int i = 1; i <= 3; i++) {
            ZipEntry entry = new ZipEntry("hello_" + i + ".pdf");
            zip.putNextEntry(entry);

            // Initialize writer
            PdfWriter writer = new PdfWriter(zip);

            // Initialize document
            PdfDocument pdfDoc = new PdfDocument(writer);
            writer.setCloseStream(false);
            Document doc = new Document(pdfDoc);

            // Add paragraph to the document
            doc.add(new Paragraph("Hello " + i));

            // Close document
            doc.close();

            zip.closeEntry();
        }
        zip.close();
    }
}
