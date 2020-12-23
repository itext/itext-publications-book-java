package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfTransparencyGroup;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;

public class Listing_10_22_TransparentOverlay {
    public static final String DEST
            = "./target/book/part3/chapter10/Listing_10_22_TransparentOverlay.pdf";
    public static final String RESOURCE
            = "./src/main/resources/img/bruno_ingeborg.jpg";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_10_22_TransparentOverlay().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(850, 600));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        ImageData img = ImageDataFactory.create(RESOURCE);
        com.itextpdf.layout.element.Image imgModel = new com.itextpdf.layout.element.Image(img);
        float w = imgModel.getImageScaledWidth();
        float h = imgModel.getImageScaledHeight();
        canvas.ellipse(1, 1, 848, 598);
        canvas.clip();
        canvas.endPath();
        canvas.addImageWithTransformationMatrix(img, w, 0, 0, h, 0, -600);

        PdfFormXObject xObject2 = new PdfFormXObject(new Rectangle(850, 600));
        PdfCanvas xObject2Canvas = new PdfCanvas(xObject2, pdfDoc);
        PdfTransparencyGroup transGroup = new PdfTransparencyGroup();
        transGroup.put(PdfName.CS, PdfName.DeviceGray);
        transGroup.setIsolated(true);
        transGroup.setKnockout(false);
        xObject2.setGroup(transGroup);

        // Add transparent ellipses to the template
        int gradationStep = 30;
        float[] gradationRatioList = new float[gradationStep];
        for(int i = 0; i < gradationStep; i++) {
            gradationRatioList[i] = 1 - (float)Math.sin(Math.toRadians(90.0f / gradationStep * (i + 1)));
        }
        for(int i = 1; i < gradationStep + 1; i++) {
            xObject2Canvas.setLineWidth(5 * (gradationStep + 1 - i));
            xObject2Canvas.setStrokeColorGray(gradationRatioList[gradationStep - i]);
            xObject2Canvas.ellipse(0, 0, 850, 600);
            xObject2Canvas.stroke();
        }

        // Create an image mask for the direct content
        PdfDictionary maskDict = new PdfDictionary();
        maskDict.put(PdfName.Type, PdfName.Mask);
        maskDict.put(PdfName.S, PdfName.Luminosity);
        maskDict.put(new PdfName("G"), xObject2.getPdfObject());
        PdfExtGState gState = new PdfExtGState();
        gState.put(PdfName.SMask, maskDict);
        canvas.setExtGState(gState);

        canvas.addXObjectAt(xObject2, 0, 0);

        doc.close();
    }
}
