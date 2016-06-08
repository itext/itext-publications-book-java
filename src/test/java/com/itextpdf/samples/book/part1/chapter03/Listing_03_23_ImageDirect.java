/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_03_23_ImageDirect extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_23_ImageDirect.pdf";

    public static final String RESOURCE = "src/test/resources/img/loa.jpg";

    public static void main(String[] args) throws Exception {
        new Listing_03_23_ImageDirect().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document and add page
        PdfDocument pdfDoc = new PdfDocument(writer);
        Rectangle postcard = new Rectangle(283, 416);
        Document doc = new Document(pdfDoc, new PageSize(postcard.getWidth(), postcard.getHeight()));
        doc.setMargins(30, 30, 30, 30);

        Paragraph p = new Paragraph("Foobar Film Festival").setFontSize(22).setTextAlignment(TextAlignment.CENTER);
        doc.add(p);

        PdfImageXObject img = new PdfImageXObject(ImageDataFactory.create(RESOURCE));
        new PdfCanvas(pdfDoc.getLastPage()).addXObject(img, (postcard.getWidth() - img.getWidth()) / 2, (postcard.getHeight() - img.getHeight()) / 2);

        //Close document
        pdfDoc.close();
    }
}
