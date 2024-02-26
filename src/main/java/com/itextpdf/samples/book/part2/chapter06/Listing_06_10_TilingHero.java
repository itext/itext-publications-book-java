package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_06_10_TilingHero {
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_10_TilingHero.pdf";
    public static final String SOURCE
            = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_10_TilingHero().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfReader reader = new PdfReader(SOURCE);
        PdfDocument srcDoc = new PdfDocument(reader);
        Rectangle pageSize = srcDoc.getFirstPage(). getPageSizeWithRotation();

        PdfWriter writer = new PdfWriter(DEST);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(pageSize));
        pdfDoc.addNewPage();

        // adding the same page 16 times with a different offset
        float x, y;
        PdfFormXObject page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
        for (int i = 0; i < 16; i++) {
            x = -pageSize.getWidth() * (i % 4);
            y = pageSize.getHeight() * (i / 4 - 3);
            new PdfCanvas(pdfDoc.getLastPage())
                    .addXObjectWithTransformationMatrix(page, 4, 0, 0, 4, x, y);
            if (15 != i) {
                doc.add(new AreaBreak());
            }
        }

        doc.close();
        srcDoc.close();
    }
}
