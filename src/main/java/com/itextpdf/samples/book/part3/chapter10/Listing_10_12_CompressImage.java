package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.io.IOException;

public class Listing_10_12_CompressImage {
    public static final String[] RESULT = {
            "./target/book/part3/chapter10/Listing_10_12_CompressImage.pdf",
            "./target/book/part3/chapter10/uncompressed.pdf",
            "./target/book/part3/chapter10/compressed.pdf"
    };

    public static final String DEST = RESULT[0];

    public static final String RESOURCE
            = "./src/main/resources/img/butterfly.bmp";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_10_12_CompressImage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(RESULT[1], false);
        createPdf(RESULT[2], true);
        concatenateResults(dest, new String[]{RESULT[1], RESULT[2]});
    }

    public void createPdf(String filename, boolean compress) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);
        Image img = new Image(ImageDataFactory.create(RESOURCE));
        if (compress) {
            img.getXObject().getPdfObject().setCompressionLevel(9);
        }
        doc.add(img);
        doc.close();
    }

    // Only for testing reasons
    protected void concatenateResults(String dest, String[] names) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();
        PdfDocument tempDoc;
        for (String name : names) {
            tempDoc = new PdfDocument(new PdfReader(name));
            tempDoc.copyPagesTo(1, tempDoc.getNumberOfPages(), pdfDoc);
            tempDoc.close();
        }
        pdfDoc.close();
    }
}
