package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class Listing_10_13_CompressAwt {
    public static final String DEST = "./target/book/part3/chapter10/Listing_10_13_CompressAwt.pdf";
    public static final String RESOURCE
            = "./src/main/resources/img/hitchcock.png";
    public static final String[] RESULT = {
            "./target/book/part3/chapter10/Listing_10_13_CompressAwt_hitchcock100.pdf",
            "./target/book/part3/chapter10/Listing_10_13_CompressAwt_hitchcock20.pdf",
            "./target/book/part3/chapter10/Listing_10_13_CompressAwt_hitchcock10.pdf"
    };

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_10_13_CompressAwt().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(RESULT[0], 1);
        createPdf(RESULT[1], 0.2f);
        createPdf(RESULT[2], 0.1f);
    }

    public void createPdf(String dest, float quality) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(200, 280));
        BufferedImage bi = ImageIO.read(new FileInputStream(RESOURCE));
        ByteArrayOutputStream outputStream;
        // TODO DEVSIX-580
        try {
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(quality);

            outputStream = new ByteArrayOutputStream();
            jpgWriter.setOutput(new MemoryCacheImageOutputStream((outputStream)));
            IIOImage outputImage = new IIOImage(bi, null, null);

            jpgWriter.write(null, outputImage, jpgWriteParam);
            jpgWriter.dispose();
            outputStream.flush();
        } catch (Exception e) {
            throw new PdfException(e);
        }
        Image img = new Image(ImageDataFactory.create(outputStream.toByteArray()));
        img.setFixedPosition(15, 15);

        doc.add(img);
        doc.close();
    }
}
