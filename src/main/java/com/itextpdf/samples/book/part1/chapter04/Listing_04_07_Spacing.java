/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_04_07_Spacing {
    public static final String DEST = "./target/book/part1/chapter04/Listing_04_07_Spacing.pdf";

    private static final String longText = "Dr. iText or: How I Learned to Stop Worrying " +
            "and Love the Portable Document Format.";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_07_Spacing().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
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

        cell = new Cell().add(new Paragraph("Dr. iText or: How I Learned to Stop Worrying and Love PDF"));
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
