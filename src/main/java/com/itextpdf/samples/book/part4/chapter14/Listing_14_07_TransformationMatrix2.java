package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class Listing_14_07_TransformationMatrix2 {
    public static final String DEST
            = "./target/book/part4/chapter14/Listing_14_07_TransformationMatrix2.pdf";
    public static final String RESOURCE
            = "./src/main/resources/pdfs/logo.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_14_07_TransformationMatrix2().manipulatePdf(DEST);
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
        canvas.addXObjectAt(xObject, 0, 0)
                .addXObjectWithTransformationMatrix(xObject, 0.5f, 0, 0, 0.5f, -595, 0)
                .addXObjectWithTransformationMatrix(xObject, 0.5f, 0, 0, 0.5f, -297.5f, 297.5f)
                .addXObjectWithTransformationMatrix(xObject, 1, 0, 0.4f, 1, -750, -650)
                .addXObjectWithTransformationMatrix(xObject, 0, -1, -1, 0, 650, 0)
                .addXObjectWithTransformationMatrix(xObject, 0, -0.2f, -0.5f, 0, 350, 0);

        pdfDoc.close();
        srcDoc.close();
    }
}
