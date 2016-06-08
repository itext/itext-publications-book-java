/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_16_01_SpecialId extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_01_SpecialId.pdf";
    public static String RESOURCE = "./src/test/resources/img/bruno.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_16_01_SpecialId().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(new Rectangle(400, 300)));
        PdfImageXObject xObject = new PdfImageXObject(ImageDataFactory.create(RESOURCE));
        xObject.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.addXObject(xObject, 0, 0);
        doc.close();
    }
}
