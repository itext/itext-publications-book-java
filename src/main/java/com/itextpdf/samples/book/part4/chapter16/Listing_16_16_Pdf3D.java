package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.Pdf3DAnnotation;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.FileInputStream;

public class Listing_16_16_Pdf3D {
    public static final String DEST = "./target/book/part4/chapter16/Listing_16_16_Pdf3D.pdf";
    public static String RESOURCE = "./src/main/resources/img/teapot.u3d";

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_16_16_Pdf3D().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        Document doc = new Document(pdfDoc);
        Rectangle rect = new Rectangle(100, 400, 400, 400);

        PdfStream stream3D = new PdfStream(pdfDoc, new FileInputStream(RESOURCE));
        stream3D.put(PdfName.Type, new PdfName("3D"));
        stream3D.put(PdfName.Subtype, new PdfName("U3D"));
        stream3D.setCompressionLevel(CompressionConstants.DEFAULT_COMPRESSION);
        stream3D.flush();

        PdfDictionary dict3D = new PdfDictionary();
        dict3D.put(PdfName.Type, new PdfName("3DView"));
        dict3D.put(new PdfName("XN"), new PdfString("Default"));
        dict3D.put(new PdfName("IN"), new PdfString("Unnamed"));
        dict3D.put(new PdfName("MS"), PdfName.M);
        dict3D.put(new PdfName("C2W"),
                new PdfArray(new float[]{1, 0, 0, 0, 0, -1, 0, 1, 0, 3, -235, 28}));
        dict3D.put(PdfName.CO, new PdfNumber(235));

        Pdf3DAnnotation annot = new Pdf3DAnnotation(rect, stream3D);
        annot.setContents(new PdfString("3D Model"));
        annot.setDefaultInitialView(dict3D);
        pdfDoc.addNewPage().addAnnotation(annot);
        doc.close();
    }
}
