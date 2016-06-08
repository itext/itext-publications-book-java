/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

// IMPORTANT: this sample is not relevant anymore
@Category(SampleTest.class)
public class Listing_05_14_NewPage extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_14_NewPage.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_14_NewPage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());
        doc.add(new Paragraph("This page will NOT be followed by a blank page!"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("This page will be followed by a blank page!"));
        doc.add(new AreaBreak());
        doc.add(new AreaBreak());
        doc.add(new Paragraph("The previous page was a blank page!"));
        doc.close();
    }
}


