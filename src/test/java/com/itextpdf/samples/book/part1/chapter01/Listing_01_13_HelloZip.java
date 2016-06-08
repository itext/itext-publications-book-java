/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_01_13_HelloZip {
    public static final String DEST = "./target/test/resources/book/part1/chapter01/Listing_01_13_HelloZip.zip";

    @BeforeClass
    public static void beforeClass() throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
    }

    @Before
    public void before() throws IOException, InterruptedException {
        Listing_01_13_HelloZip helloZip = new Listing_01_13_HelloZip();
        helloZip.manipulatePdf(DEST);
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

    @Test
    public void comparePdf() throws IOException {
        ZipFile zf = new ZipFile(DEST);
        for (int i = 1; i <= 3; i++) {
            InputStream in = zf.getInputStream(zf.getEntry("hello_" + i + ".pdf"));
            //Check only whether the created files can be opened or not
            try {
                new PdfDocument(new PdfReader(in));
            } catch (IOException e) {
                Assert.fail();
            }
        }
    }
}
