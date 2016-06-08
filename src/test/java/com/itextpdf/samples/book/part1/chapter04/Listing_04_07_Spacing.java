/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_04_07_Spacing extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter04/Listing_04_07_Spacing.pdf";

    private static final String longText = "Dr. iText or: How I Learned to Stop Worrying " +
            "and Love the Portable Document Format.";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_07_Spacing().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        //table.setWidthPercent(100);
        Cell cell = new Cell().add(new Paragraph(longText));
        table.addCell("default leading / spacing");
        table.addCell(cell);
        table.addCell("absolute leading: 20");

        cell = new Cell().add(new Paragraph(longText).setMultipliedLeading(0).setFixedLeading(20));
        table.addCell(cell);
        table.addCell("absolute leading: 3; relative leading: 1.2");
        cell = new Cell().add(new Paragraph(longText).setFixedLeading(3).setMultipliedLeading(1.2f));
        table.addCell(cell);
        table.addCell("absolute leading: 0; relative leading: 1.2");
        cell = new Cell().add(new Paragraph(longText).setFixedLeading(0).setMultipliedLeading(1.2f));
        table.addCell(cell);
        table.addCell("no leading at all");
        cell = new Cell().add(new Paragraph(longText).setFixedLeading(0).setMultipliedLeading(0));
        table.addCell(cell);

        cell = new Cell().add("Dr. iText or: How I Learned to Stop Worrying and Love PDF");
        table.addCell("padding 10");
        cell.setPadding(10);
        table.addCell(cell);
        table.addCell("padding 0");
        cell.setPadding(0);
        table.addCell(cell.clone(true));
        table.addCell("different padding for left, right, top and bottom");
        cell.setPaddingLeft(20);
        cell.setPaddingRight(50);
        cell.setPaddingTop(0);
        cell.setPaddingBottom(5);
        table.addCell(cell.clone(true));
        doc.add(table);

        doc.close();
    }
}
