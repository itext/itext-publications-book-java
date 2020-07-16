package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class Listing_06_01_PageInformation {
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_01_PageInformation.txt";

    public static final String HELLO_WORLD_LANDSCAPE1
            = "./src/main/resources/pdfs/cmp_Listing_01_05_HelloWorldLandscape1.pdf";
    public static final String HELLO_WORLD_LANDSCAPE2
            = "./src/main/resources/pdfs/cmp_Listing_01_06_HelloWorldLandscape2.pdf";
    public static final String MOVIE_TEMPLATES
            = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";
    public static final String HERO1
            = "./src/main/resources/pdfs/cmp_Listing_05_15_Hero1.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_01_PageInformation().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        // Use old examples to create PDFs
        // Inspecting PDFs
        PrintWriter writer = new PrintWriter(new FileOutputStream(dest));
        inspect(writer, HELLO_WORLD_LANDSCAPE1);
        inspect(writer, HELLO_WORLD_LANDSCAPE2);
        inspect(writer, MOVIE_TEMPLATES);
        inspect(writer, HERO1);
        writer.close();
    }

    public static void inspect(PrintWriter writer, String filename)
            throws IOException {
        PdfReader reader = new PdfReader(filename);
        PdfDocument pdfDoc = new PdfDocument(reader);
        writer.println(filename);
        writer.print("Number of pages: ");
        writer.println(pdfDoc.getNumberOfPages());
        Rectangle mediabox = pdfDoc.getDefaultPageSize();
        writer.print("Size of page 1: [");
        writer.print(mediabox.getLeft());
        writer.print(',');
        writer.print(mediabox.getBottom());
        writer.print(',');
        writer.print(mediabox.getRight());
        writer.print(',');
        writer.print(mediabox.getTop());
        writer.println("]");
        writer.print("Rotation of page 1: ");
        writer.println(pdfDoc.getFirstPage().getRotation());
        writer.print("Page size with rotation of page 1: ");
        writer.println(pdfDoc.getFirstPage().getPageSizeWithRotation());
        writer.print("Is rebuilt? ");
        writer.println(reader.hasRebuiltXref());
        writer.print("Is encrypted? ");
        writer.println(reader.isEncrypted());
        writer.println();
        writer.println();
        writer.flush();
        reader.close();
    }
}
