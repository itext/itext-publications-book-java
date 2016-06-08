/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

@Category(SampleTest.class)
public class Listing_06_01_PageInformation extends GenericTest {
    public static final String RESULT
            = "./target/test/resources/book/part2/chapter06/Listing_06_01_PageInformation.txt";
    public static final String CMP_RESULT
            = "./src/test/resources/book/part2/chapter06/cmp_Listing_06_01_PageInformation.txt";

    public static final String HELLO_WORLD_LANDSCAPE1
            = "./src/test/resources/book/part1/chapter01/cmp_Listing_01_05_HelloWorldLandscape1.pdf";
    public static final String HELLO_WORLD_LANDSCAPE2
            = "./src/test/resources/book/part1/chapter01/cmp_Listing_01_06_HelloWorldLandscape2.pdf";
    public static final String MOVIE_TEMPLATES
            = "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";
    public static final String HERO1
            = "./src/test/resources/book/part1/chapter05/cmp_Listing_05_15_Hero1.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_01_PageInformation().manipulatePdf(RESULT);
    }

    @Test(timeout = 120000)
    public void test() throws IOException {
        LOGGER.info("Starting test " + getClass().getName() + ".");
        manipulatePdf(RESULT);
        System.out.println(RESULT + "\n" + CMP_RESULT);
        comparePdf(RESULT, CMP_RESULT);
        LOGGER.info("Test complete.");
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

    protected void comparePdf(String dest, String cmp) throws IOException {
        BufferedReader destReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest)));
        BufferedReader cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(cmp)));
        String curDestStr;
        String curCmpStr;
        int row = 1;
        while ((curDestStr = destReader.readLine()) != null) {
            assertNotNull("The lengths of files are different.", curCmpStr = cmpReader.readLine());
            assertEquals("The files are different on the row " + row, curCmpStr, curDestStr);
            row++;
        }
        assertNull("The lengths of files are different.", curCmpStr = cmpReader.readLine());
        destReader.close();
        cmpReader.close();
    }

}
