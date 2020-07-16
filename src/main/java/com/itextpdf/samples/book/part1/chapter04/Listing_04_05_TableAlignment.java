package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_04_05_TableAlignment {
    public static final String DEST = "./target/book/part1/chapter04/Listing_04_05_TableAlignment.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_05_TableAlignment().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = createFirstTable();
        table.setWidth(UnitValue.createPercentValue(50));
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
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        // the cell object
        Cell cell;
        // we add a cell with colspan 3
        cell = new Cell(1, 3).add(new Paragraph("Cell with colspan 3"));
        table.addCell(cell);
        // now we add a cell with rowspan 2
        cell = new Cell(2, 1).add(new Paragraph("Cell with rowspan 2"));
        table.addCell(cell);
        // we add the four remaining cells with addCell()
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
}
