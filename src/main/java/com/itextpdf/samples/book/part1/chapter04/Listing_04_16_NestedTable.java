package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_04_16_NestedTable {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_16_NestedTable.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_16_NestedTable().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        Table nested1 = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        nested1.addCell("1.1");
        nested1.addCell("1.2");
        Table nested2 = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        nested2.addCell("12.1");
        nested2.addCell("12.2");
        for (int k = 0; k < 16; ++k) {
            if (k == 1) {
                table.addCell(nested1);
            } else if (k == 12) {
                table.addCell(nested2);
            } else {
                table.addCell("cell " + k);
            }
        }
        doc.add(table);
        doc.close();
    }
}
