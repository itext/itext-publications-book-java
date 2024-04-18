package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Listing_10_08_RawImage {
    public static final String DEST = "./target/book/part3/chapter10/Listing_10_08_RawImage.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_10_08_RawImage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        // Image in color space DeviceGray
        byte gradient[] = new byte[256];
        for (int i = 0; i < 256; i++)
            gradient[i] = (byte) i;
        Image img1 = new Image(ImageDataFactory.create(256, 1, 1, 8, gradient, null));
        img1.scaleAbsolute(256, 50);
        doc.add(img1);
        // Image in color space RGB
        byte cgradient[] = new byte[256 * 3];
        for (int i = 0; i < 256; i++) {
            cgradient[i * 3] = (byte) (255 - i);
            cgradient[i * 3 + 1] = (byte) (255 - i);
            cgradient[i * 3 + 2] = (byte) i;
        }
        Image img2 = new Image(ImageDataFactory.create(256, 1, 3, 8, cgradient, null));
        img2.scaleAbsolute(256, 50);
        doc.add(img2);
        Image img3 = new Image(ImageDataFactory.create(16, 16, 3, 8, cgradient, null));
        img3.scaleAbsolute(64, 64);
        doc.add(img3);

        doc.close();
    }
}
