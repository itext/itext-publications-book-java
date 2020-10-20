package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

public class Listing_03_23_ImageDirect {

    public static final String DEST = "./target/book/part1/chapter03/Listing_03_23_ImageDirect.pdf";

    public static final String RESOURCE = "src/main/resources/img/loa.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_23_ImageDirect().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Rectangle postcard = new Rectangle(283, 416);
        Document doc = new Document(pdfDoc, new PageSize(postcard.getWidth(), postcard.getHeight()));
        doc.setMargins(30, 30, 30, 30);

        Paragraph p = new Paragraph("Foobar Film Festival").setFontSize(22).setTextAlignment(TextAlignment.CENTER);
        doc.add(p);

        PdfImageXObject img = new PdfImageXObject(ImageDataFactory.create(RESOURCE));
        new PdfCanvas(pdfDoc.getLastPage()).addXObjectAt(img, (postcard.getWidth() - img.getWidth()) / 2, (postcard.getHeight() - img.getHeight()) / 2);

        //Close document
        pdfDoc.close();
    }
}
