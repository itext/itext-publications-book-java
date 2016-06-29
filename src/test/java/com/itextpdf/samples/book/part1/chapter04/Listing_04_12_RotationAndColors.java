/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
// TODO DEVSIX-574
public class Listing_04_12_RotationAndColors extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter04/Listing_04_12_RotationAndColors.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_04_12_RotationAndColors().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        Table table = new Table(new float[]{1, 3, 3, 3});
        table.setWidthPercent(100);
        Cell cell;
        // row 1, cell 1
        cell = new Cell().add("COLOR");
        cell.setRotationAngle(Math.toRadians(90));
        cell.setVerticalAlignment(VerticalAlignment.TOP);
        table.addCell(cell);
        // row 1, cell 2
        cell = new Cell().add("red / no borders");
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(Color.RED);
        table.addCell(cell);
        // row 1, cell 3
        cell = new Cell().add("green / black bottom border");
        cell.setBorderBottom(new SolidBorder(Color.BLACK, 10f));
        cell.setBackgroundColor(Color.GREEN);
        table.addCell(cell);
        // row 1, cell 4
        cell = new Cell().add("cyan / blue top border + padding");
        cell.setBorderTop(new SolidBorder(Color.BLUE, 5f));
        cell.setBackgroundColor(Color.CYAN);
        table.addCell(cell);
        // row 2, cell 1
        cell = new Cell().add("GRAY");
        cell.setRotationAngle(Math.toRadians(90));
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addCell(cell);
        // row 2, cell 2
        cell = new Cell().add("0.6");
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(new DeviceGray(0.6f));
        table.addCell(cell);
        // row 2, cell 3
        cell = new Cell().add("0.75");
        cell.setBorder(Border.NO_BORDER);
        // cell.setGrayFill(0.75f);
        cell.setBackgroundColor(new DeviceGray(0.75f));
        table.addCell(cell);
        // row 2, cell 4
        cell = new Cell().add("0.9");
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(new DeviceGray(0.9f));
        table.addCell(cell);
        // row 3, cell 1
        cell = new Cell().add("BORDERS");
        cell.setRotationAngle(Math.toRadians(90));
        cell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        table.addCell(cell);
        // row 3, cell 2
        cell = new Cell().add("different borders");
        cell.setBorderLeft(new SolidBorder(Color.RED, 16f));
        cell.setBorderBottom(new SolidBorder(Color.ORANGE, 12f));
        cell.setBorderRight(new SolidBorder(Color.YELLOW, 8f));
        cell.setBorderTop(new SolidBorder(Color.GREEN, 4f));
        table.addCell(cell);
        // row 3, cell 3
        cell = new Cell().add("with correct padding");
        cell.setBorderLeft(new SolidBorder(Color.RED, 16f));
        cell.setBorderBottom(new SolidBorder(Color.ORANGE, 12f));
        cell.setBorderRight(new SolidBorder(Color.YELLOW, 8f));
        cell.setBorderTop(new SolidBorder(Color.GREEN, 4f));
        table.addCell(cell);
        // row 3, cell 4
        cell = new Cell().add("red border");
        cell.setBorder(new SolidBorder(Color.RED, 8f));
        table.addCell(cell);
        doc.add(table);
        doc.close();
    }
}

