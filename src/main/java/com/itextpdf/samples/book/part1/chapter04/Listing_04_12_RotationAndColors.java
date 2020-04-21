package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_04_12_RotationAndColors {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_12_RotationAndColors.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_12_RotationAndColors().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 3, 3}));
        Cell cell;
        // row 1, cell 1
        cell = new Cell().add(new Paragraph("COLOR"));
        cell.setRotationAngle(Math.toRadians(90));
        cell.setVerticalAlignment(VerticalAlignment.TOP);
        table.addCell(cell);
        // row 1, cell 2
        cell = new Cell().add(new Paragraph("red / no borders"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(ColorConstants.RED);
        table.addCell(cell);
        // row 1, cell 3
        cell = new Cell().add(new Paragraph("green / black bottom border"));
        cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 10f));
        cell.setBackgroundColor(ColorConstants.GREEN);
        table.addCell(cell);
        // row 1, cell 4
        cell = new Cell().add(new Paragraph("cyan / blue top border + padding"));
        cell.setBorderTop(new SolidBorder(ColorConstants.BLUE, 5f));
        cell.setBackgroundColor(ColorConstants.CYAN);
        table.addCell(cell);
        // row 2, cell 1
        cell = new Cell().add(new Paragraph("GRAY"));
        cell.setRotationAngle(Math.toRadians(90));
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addCell(cell);
        // row 2, cell 2
        cell = new Cell().add(new Paragraph("0.6"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(new DeviceGray(0.6f));
        table.addCell(cell);
        // row 2, cell 3
        cell = new Cell().add(new Paragraph("0.75"));
        cell.setBorder(Border.NO_BORDER);
        // cell.setGrayFill(0.75f);
        cell.setBackgroundColor(new DeviceGray(0.75f));
        table.addCell(cell);
        // row 2, cell 4
        cell = new Cell().add(new Paragraph("0.9"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(new DeviceGray(0.9f));
        table.addCell(cell);
        // row 3, cell 1
        cell = new Cell().add(new Paragraph("BORDERS"));
        cell.setRotationAngle(Math.toRadians(90));
        cell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        table.addCell(cell);
        // row 3, cell 2
        cell = new Cell().add(new Paragraph("different borders"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.RED, 16f));
        cell.setBorderBottom(new SolidBorder(ColorConstants.ORANGE, 12f));
        cell.setBorderRight(new SolidBorder(ColorConstants.YELLOW, 8f));
        cell.setBorderTop(new SolidBorder(ColorConstants.GREEN, 4f));
        table.addCell(cell);
        // row 3, cell 3
        cell = new Cell().add(new Paragraph("with correct padding"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.RED, 16f));
        cell.setBorderBottom(new SolidBorder(ColorConstants.ORANGE, 12f));
        cell.setBorderRight(new SolidBorder(ColorConstants.YELLOW, 8f));
        cell.setBorderTop(new SolidBorder(ColorConstants.GREEN, 4f));
        table.addCell(cell);
        // row 3, cell 4
        cell = new Cell().add(new Paragraph("red border"));
        cell.setBorder(new SolidBorder(ColorConstants.RED, 8f));
        table.addCell(cell);
        doc.add(table);
        doc.close();
    }
}
