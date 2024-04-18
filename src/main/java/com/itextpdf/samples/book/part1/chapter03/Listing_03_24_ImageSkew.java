package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

public class Listing_03_24_ImageSkew {

    public static final String DEST = "./target/book/part1/chapter03/Listing_03_24_ImageSkew.pdf";

    public static final String RESOURCE = "src/main/resources/img/loa.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_24_ImageSkew().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        ImageData img = ImageDataFactory.create(RESOURCE);
        new PdfCanvas(pdfDoc.addNewPage(new PageSize(416, 283))).
                addImageWithTransformationMatrix(img, img.getWidth(), 0, .35f * img.getHeight(),
                        .65f * img.getHeight(), 30, 30);

        //Close document
        pdfDoc.close();
    }
}
