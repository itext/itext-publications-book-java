/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;


@Category(SampleTest.class)
// The example has no sense because in itext7 the height of table and rows is not set until rendering
public class Listing_04_10_TableHeight extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter04/Listing_04_10_TableHeight.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_10_TableHeight().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = createFirstTable();
        doc.add(table);
        table = createFirstTable();
        table.setWidth(50);
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
