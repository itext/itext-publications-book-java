package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

// IMPORTANT: this sample is not relevant anymore
public class Listing_05_14_NewPage {
    public static final String DEST =
            "./target/book/part1/chapter05/Listing_05_14_NewPage.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_05_14_NewPage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());
        doc.add(new Paragraph("This page will NOT be followed by a blank page!"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("This page will be followed by a blank page!"));
        doc.add(new AreaBreak());
        doc.add(new AreaBreak());
        doc.add(new Paragraph("The previous page was a blank page!"));
        doc.close();
    }
}
