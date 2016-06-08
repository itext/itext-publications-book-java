/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_04_05_TableAlignment extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter04/Listing_04_05_TableAlignment.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_05_TableAlignment().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        Table table = createFirstTable();
        table.setWidthPercent(50);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        doc.add(table);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(table);
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        doc.add(table);

        doc.close();
    }

    public static Table createFirstTable() {
        // a table with three columns
        Table table = new Table(3);
        // the cell object
        Cell cell;
        // we add a cell with colspan 3
        cell = new Cell(1, 3).add("Cell with colspan 3");
        table.addCell(cell);
        // now we add a cell with rowspan 2
        cell = new Cell(2, 1).add("Cell with rowspan 2");
        table.addCell(cell);
        // we add the four remaining cells with addCell()
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
}
