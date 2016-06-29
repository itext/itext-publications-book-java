/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_03_24_ImageSkew extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_24_ImageSkew.pdf";

    public static final String RESOURCE = "src/test/resources/img/loa.jpg";

    public static void main(String[] args) throws Exception {
        new Listing_03_24_ImageSkew().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        ImageData img = ImageDataFactory.create(RESOURCE);
        new PdfCanvas(pdfDoc.addNewPage(new PageSize(416, 283))).
                addImage(img, img.getWidth(), 0, .35f * img.getHeight(),
                        .65f * img.getHeight(), 30, 30);

        //Close document
        pdfDoc.close();
    }
}
