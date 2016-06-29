/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_05_18_Hero3 extends Listing_05_15_Hero1 {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_18_Hero3.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_18_Hero3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Rectangle art = new Rectangle(50, 50, 495, 742);
        pdfDoc.addNewPage().setArtBox(art);

        PdfFormXObject template = new PdfFormXObject(pdfDoc.getDefaultPageSize());
        PdfCanvas canvas = new PdfCanvas(template, pdfDoc);
        createTemplate(canvas, 1);
        canvas.stroke();
        new PdfCanvas(pdfDoc.getFirstPage()).addXObject(template, 0, 0);

        doc.close();
    }
}

