/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_06_06_Layers extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter06/Listing_06_06_Layers.pdf";
    public static final String SOURCE
            = "./target/test/resources/book/part2/chapter06/Listing_06_06_Layers_orig.pdf";
    public static final String RESOURCE
            = "./src/test/resources/img/loa.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_06_Layers().manipulatePdf(DEST);
    }

    /**
     * Draws a rectangle
     *
     * @param canvas the canvas
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public static void drawRectangle(PdfCanvas canvas, float width, float height) {
        canvas.saveState();
        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(0.6f);
        canvas.setExtGState(state);
        canvas.setFillColor(new DeviceRgb(0xFF, 0xFF, 0xFF));
        canvas.setLineWidth(3);
        canvas.rectangle(0, 0, width, height);
        canvas.fillStroke();
        canvas.restoreState();
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // First create the original file
        createOriginalPdf(SOURCE);

        // Initialize source document
        PdfReader reader = new PdfReader(SOURCE);
        PdfDocument srcDoc = new PdfDocument(reader);

        // Initialize result document
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument resultDoc = new PdfDocument(writer);
        Document doc = new Document(resultDoc, new PageSize(PageSize.A5).rotate());

        PdfFont font = PdfFontFactory.createFont(FontConstants.ZAPFDINGBATS, true);
        PdfCanvas canvas = new PdfCanvas(resultDoc.addNewPage());
        for (int i = 1; i <= srcDoc.getNumberOfPages(); i++) {
            PdfFormXObject layer = srcDoc.getPage(i).copyAsFormXObject(resultDoc);
            canvas.addXObject(layer, 1f, 0, 0.4f, 0.4f, 72, 50 * i);
            canvas.beginText();
            canvas.setFontAndSize(font, 20);
            canvas.moveText(496, 150 + 50 * i);
            canvas.showText(String.valueOf((char) (181 + i)));
            canvas.endText();
            canvas.stroke();
        }

        // Close documents
        doc.close();
        srcDoc.close();
    }

    public void createOriginalPdf(String filename) throws IOException {
        // No POST_CARD constant in itext7
        PageSize postCard = new PageSize(283, 416);

        // Initialize document
        FileOutputStream fos = new FileOutputStream(filename);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, postCard);
        doc.setMargins(30, 30, 30, 30);

        // Page 1: a rectangle
        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        drawRectangle(under, postCard.getWidth(), postCard.getHeight());
        under.setFillColor(new DeviceRgb(0xFF, 0xD7, 0x00));
        under.rectangle(5, 5, postCard.getWidth() - 10, postCard.getHeight() - 10);
        under.fill();
        doc.add(new AreaBreak());

        // Page 2: an image
        page = pdfDoc.getLastPage();
        under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        drawRectangle(under, postCard.getWidth(), postCard.getHeight());
        Image img = new Image(ImageDataFactory.create(RESOURCE));
        img.setFixedPosition((postCard.getWidth() - img.getImageScaledWidth()) / 2,
                (postCard.getHeight() - img.getImageScaledHeight()) / 2);
        doc.add(img);
        doc.add(new AreaBreak());

        // Page 3: the words "Foobar Film Festival"
        page = pdfDoc.getLastPage();
        under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        drawRectangle(under, postCard.getWidth(), postCard.getHeight());
        Paragraph p = new Paragraph("Foobar Film Festival")
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(22)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(p);
        doc.add(new AreaBreak());

        // Page 4: the words "SOLD OUT"
        page = pdfDoc.getLastPage();
        under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        drawRectangle(under, postCard.getWidth(), postCard.getHeight());

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
