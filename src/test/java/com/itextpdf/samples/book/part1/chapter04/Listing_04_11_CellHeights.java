/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_04_11_CellHeights extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter04/Listing_04_11_CellHeights.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_11_CellHeights().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A5).rotate());

        Table table = new Table(2);
        // a long phrase
        Paragraph p = new Paragraph(
                "Dr. iText or: How I Learned to Stop Worrying and Love PDF.");
        Cell cell = new Cell().add(p);
        // the prhase is wrapped
        table.addCell("wrap");
        table.addCell(cell);
        table.addCell(new Cell().add(p));
        // a long phrase with newlines
        p = new Paragraph(
                "Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        cell = new Cell().add(p);
        // the phrase fits the fixed height
        table.addCell("fixed height (more than sufficient)");
        cell.setHeight(72f);
        table.addCell(cell);
        // the phrase doesn't fit the fixed height
        table.addCell("fixed height (not sufficient)");
        cell.setHeight(36f);
        table.addCell(cell.clone(true));
        // The minimum height is exceeded
        table.addCell("minimum height");
        cell = new Cell().add(new Paragraph("Dr. iText"));
        // TODO DEVSIX-522
        cell.setHeight(36f);
        table.addCell(cell);
        table.addCell("extend last row");
        table.addCell(cell.clone(true));
        doc.add(table);
        doc.close();
    }
}

