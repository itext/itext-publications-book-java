package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Listing_16_02_ResizeImage {
    public static final String DEST = "./target/book/part4/chapter16/Listing_16_02_ResizeImage.pdf";
    public static float FACTOR = 0.5f;
    public static final String SPECIAL_ID = "./src/main/resources/pdfs/cmp_Listing_16_01_SpecialId.pdf";

    private static final String PRE_GENERATED_RESIZED_IMAGE = "./src/main/resources/pdfs/resizedImage.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        boolean isLoadPreGeneratedImage = false;
        new Listing_16_02_ResizeImage().manipulatePdf(DEST, isLoadPreGeneratedImage);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        manipulatePdf(dest, true);
    }

    /**
     * Different JDK versions have different Color Management Modules, which result in different bytes
     * content for images. That's why for testing purposes we use pre-generated image.
     */
    public void manipulatePdf(String dest, boolean isLoadPreGeneratedImage) throws IOException, SQLException {
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
                byte[] imgBytes;
                if (isLoadPreGeneratedImage) {
                    imgBytes = loadPreGeneratedImage();
                } else {
                    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
                    Graphics2D g = img.createGraphics();
                    g.drawRenderedImage(bi, at);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(img, "JPG", baos);
                    imgBytes = baos.toByteArray();
                }
                stream.clear();
                stream.setData(imgBytes, false);
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

    private byte[] loadPreGeneratedImage() throws IOException {
        return Files.readAllBytes(Paths.get(PRE_GENERATED_RESIZED_IMAGE));
    }
}
