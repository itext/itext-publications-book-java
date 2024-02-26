package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_05_17_Hero2 extends Listing_05_15_Hero1 {
    public static final String DEST =
            "./target/book/part1/chapter05/Listing_05_17_Hero2.pdf";
    public static final String RESOURCE = "./src/main/resources/txt/hero.txt";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_05_17_Hero2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        float w = PageSize.A4.getWidth();
        float h = PageSize.A4.getHeight();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(new Rectangle(-2 * w, -2 * h, 4 * w, 4 * h)));
        Rectangle cropBox = new Rectangle(-2 * w, h, w, h);
        pdfDoc.addNewPage().setCropBox(cropBox);
        PdfFormXObject template = new PdfFormXObject(new Rectangle(pdfDoc.getDefaultPageSize().getWidth(),
                pdfDoc.getDefaultPageSize().getHeight()));
        PdfCanvas canvas = new PdfCanvas(template, pdfDoc);
        createTemplate(canvas, 4);
        float adjust;
        int i = 1;
        while (true) {
            new PdfCanvas(pdfDoc.getPage(i)).addXObjectAt(template, -2 * w, -2 * h);
            adjust = cropBox.getRight() + w;
            if (adjust > 2 * w) {
                adjust = cropBox.getBottom() - h;
                if (adjust < -2 * h)
                    break;
                cropBox = new Rectangle(-2 * w, adjust, w, cropBox.getBottom() - adjust);
            } else {
                cropBox = new Rectangle(cropBox.getRight(), cropBox.getBottom(), adjust - cropBox.getRight(),
                        cropBox.getHeight());
            }
            pdfDoc.addNewPage().setCropBox(cropBox);
            i++;
        }
        doc.close();
    }
}
