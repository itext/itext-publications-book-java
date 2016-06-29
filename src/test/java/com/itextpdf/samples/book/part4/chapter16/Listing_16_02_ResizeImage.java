/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_16_02_ResizeImage extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_02_ResizeImage.pdf";
    public static float FACTOR = 0.5f;
    public static final String SPECIAL_ID = "./src/test/resources/book/part4/chapter16/cmp_Listing_16_01_SpecialId.pdf";
    public static void main(String args[]) throws IOException, SQLException {
        new Listing_16_02_ResizeImage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfName key = new PdfName("ITXT_SpecialId");
        PdfName value = new PdfName("123456789");
        // Read the file
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SPECIAL_ID), new PdfWriter(dest));
        int n = pdfDoc.getNumberOfPdfObjects();
        PdfObject object;
        PdfStream stream;
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {
            object = pdfDoc.getPdfObject(i);
            if (object == null || !object.isStream())
                continue;
            stream = (PdfStream) object;
            if (value.equals(stream.get(key))) {
                PdfImageXObject imageXObject = new PdfImageXObject(stream);
                BufferedImage bi = imageXObject.getBufferedImage();
                if (bi == null) continue;
                int width = (int)(bi.getWidth() * FACTOR);
                int height = (int)(bi.getHeight() * FACTOR);
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                ImageIO.write(img, "JPG", imgBytes);
                stream.clear();
                stream.setData(imgBytes.toByteArray(), false);
                stream.put(PdfName.Type, PdfName.XObject);
                stream.put(PdfName.Subtype, PdfName.Image);
                stream.put(key, value);
                stream.put(PdfName.Filter, PdfName.DCTDecode);
                stream.put(PdfName.Width, new PdfNumber(width));
                stream.put(PdfName.Height, new PdfNumber(height));
                stream.put(PdfName.BitsPerComponent, new PdfNumber(8));
                stream.put(PdfName.ColorSpace, PdfName.DeviceRGB);
            }
        }
        pdfDoc.close();
    }
}
