/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_14_07_TransformationMatrix2 extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter14/Listing_14_07_TransformationMatrix2.pdf";
    public static final String RESOURCE
            = "./src/test/resources/pdfs/logo.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_14_06_TransformationMatrix1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(new PageSize(new Rectangle(-595, -842, 595 * 2, 842 * 2)));

        // draw the coordinate system
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.moveTo(-595, 0)
                .lineTo(595, 0)
                .moveTo(0, -842)
                .lineTo(0, 842)
                .stroke();

        // read the PDF with the logo
        PdfDocument srcDoc = new PdfDocument(new PdfReader(RESOURCE));
        PdfPage curPage = srcDoc.getPage(1);
        PdfFormXObject xObject = curPage.copyAsFormXObject(pdfDoc);
        // add it at different positions using different transformations
        canvas.addXObject(xObject, 0, 0)
                .addXObject(xObject, 0.5f, 0, 0, 0.5f, -595, 0)
                .addXObject(xObject, 0.5f, 0, 0, 0.5f, -297.5f, 297.5f)
                .addXObject(xObject, 1, 0, 0.4f, 1, -750, -650)
                .addXObject(xObject, 0, -1, -1, 0, 650, 0)
                .addXObject(xObject, 0, -0.2f, -0.5f, 0, 350, 0);

        pdfDoc.close();
        srcDoc.close();
    }
}
