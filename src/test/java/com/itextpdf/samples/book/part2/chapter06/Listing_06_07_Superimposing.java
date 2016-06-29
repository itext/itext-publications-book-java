/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_06_07_Superimposing extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter06/Listing_06_07_Superimposing.pdf";
    public static final String SOURCE
            = "./target/test/resources/book/part2/chapter06/Listing_06_07_Superimposing_opening.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/loa.jpg";

    public static final PageSize postCard = new PageSize(283, 416);

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_07_Superimposing().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // First create the original file
        createOriginalPdf(SOURCE);

        // Initialize source document
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SOURCE));

        // Initialize result document
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(resultDoc, postCard);

        PdfCanvas canvas = new PdfCanvas(resultDoc.addNewPage());
        for (int i = 1; i <= srcDoc.getNumberOfPages(); i++) {
            PdfFormXObject layer = srcDoc.getPage(i).copyAsFormXObject(resultDoc);
            canvas.addXObject(layer, 1f, 0, 0, 1, 0, 0);
        }

        // Close documents
        doc.close();
        srcDoc.close();
    }

    public void createOriginalPdf(String filename) throws IOException {
        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc, postCard);
        doc.setMargins(30, 30, 30, 30);

        // Page 1: a rectangle
        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        under.setFillColor(new DeviceRgb(0xFF, 0xD7, 0x00));
        under.rectangle(5, 5, postCard.getWidth() - 10, postCard.getHeight() - 10);
        under.fillStroke();

        doc.add(new AreaBreak());

        // Page 2: an image
        Image img = new Image(ImageDataFactory.create(RESOURCE));
        img.setFixedPosition((postCard.getWidth() - img.getImageScaledWidth()) / 2,
                (postCard.getHeight() - img.getImageScaledHeight()) / 2);
        doc.add(img);
        doc.add(new AreaBreak());

        // Page 3: the words "Foobar Film Festival"
        Paragraph p = new Paragraph("Foobar Film Festival")
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(22)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(p);
        doc.add(new AreaBreak());

        // Page 4: the words "SOLD OUT"
        page = pdfDoc.getLastPage();
        PdfCanvas over = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
        over.saveState();
        float sinus = (float) Math.sin(Math.PI / 60);
        float cosinus = (float) Math.cos(Math.PI / 60);
        over.beginText();
        over.setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE);
        over.setLineWidth(1.5f);
        over.setStrokeColor(new DeviceRgb(0xFF, 0x00, 0x00));
        over.setFillColor(new DeviceRgb(0xFF, 0xFF, 0xFF));
        over.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 36);
        over.setTextMatrix(cosinus, sinus, -sinus, cosinus, 50, 324);
        over.showText("SOLD OUT");
        over.setTextMatrix(0, 0);
        over.endText();
        over.restoreState();

        doc.close();
    }
}
