package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_04_15_XMen {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_15_XMen.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_15_XMen().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        Image[] img = {
                new Image(ImageDataFactory.create(String.format(RESOURCE, "0120903"))),
                new Image(ImageDataFactory.create(String.format(RESOURCE, "0290334"))),
                new Image(ImageDataFactory.create(String.format(RESOURCE, "0376994"))),
                new Image(ImageDataFactory.create(String.format(RESOURCE, "0348150")))
        };
        // Creates a table with 6 columns
        Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();
        // first movie
        table.addCell(new Cell()
                .add(new Paragraph("X-Men"))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP));
        // we wrap the image in a Cell
        Cell cell = new Cell()
                .add(img[0])
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP);
        table.addCell(cell);
        // second movie
        table.addCell(new Cell()
                .add(new Paragraph("X2"))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        // we wrap the image in a Cell and let iText scale it
        cell = new Cell()
                .add(img[1].setAutoScale(true))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addCell(cell);
        // third movie
        table.addCell(new Cell()
                .add(new Paragraph("X-Men: The Last Stand"))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM));
        // we add the image with addCell()
        table.addCell(new Cell()
                .add(img[2])
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM));
        // fourth movie
        table.addCell(new Cell()
                .add(new Paragraph("Superman Returns")));
        cell = new Cell();
        // we set the widt percent of image
        img[3].setWidth(UnitValue.createPercentValue(50));
        cell
                .add(img[3])
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM);
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }
}
