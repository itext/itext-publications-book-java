package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.geom.PageSize;
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

public class Listing_04_11_CellHeights {
    public static final String DEST = "./target/book/part1/chapter04/Listing_04_11_CellHeights.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_11_CellHeights().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A5).rotate());

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        // a long phrase
        Paragraph p = new Paragraph(
                "Dr. iText or: How I Learned to Stop Worrying and Love PDF.");
        Cell cell = new Cell().add(p);
        // the prhase is wrapped
        table.addCell("wrap");
        table.addCell(cell);
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
        table.addCell(cell.clone(true).setHeight(36));
        // The minimum height is exceeded
        table.addCell("minimum height");
        cell = new Cell().add(new Paragraph("Dr. iText"));
        table.addCell(cell.clone(true).setMinHeight(36));
        table.addCell("extend last row");
        table.addCell(cell.clone(true));
        doc.add(table);
        doc.close();
    }
}
