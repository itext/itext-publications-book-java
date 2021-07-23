package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.Separation;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import com.itextpdf.kernel.pdf.function.PdfFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class Listing_10_02_SeparationColor {
    public static final String DEST = "./target/book/part3/chapter10/Listing_10_02_SeparationColor.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_10_02_SeparationColor().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfSpecialCs.Separation rgbColorSpace = createRgbColorSpace(0.39216f, 0.58431f, 0.92941f);
        PdfSpecialCs.Separation grayColorSpace = createGrayColorSpace(.9f);
        PdfSpecialCs.Separation cmykColorSpace = createCmykColorSpace(.3f, .9f, .3f, .1f);

        colorRectangle(canvas, new Separation(grayColorSpace, 0.5f), 36, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .1f), 90, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .2f), 144, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .3f), 198, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .4f), 252, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .5f), 306, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .6f), 360, 770, 36, 36);
        colorRectangle(canvas, new Separation(rgbColorSpace, .7f), 416, 770, 36, 36);
        colorRectangle(canvas, new Separation(cmykColorSpace,.25f), 470, 770, 36, 36);

        canvas.setFillColor(new Separation(grayColorSpace, 0.5f));
        canvas.rectangle(36, 716, 36, 36);
        canvas.fillStroke();
        canvas.setFillColor(new Separation(grayColorSpace,0.9f));
        canvas.rectangle(90, 716, 36, 36);
        canvas.fillStroke();
        canvas.setFillColor(new Separation(rgbColorSpace, 0.5f));
        canvas.rectangle(144, 716, 36, 36);
        canvas.fillStroke();
        canvas.setFillColor(new Separation(rgbColorSpace, 0.9f));
        canvas.rectangle(198, 716, 36, 36);
        canvas.fillStroke();
        canvas.setFillColor(new Separation(cmykColorSpace, 0.5f));
        canvas.rectangle(252, 716, 36, 36);
        canvas.fillStroke();
        canvas.setFillColor(new Separation(cmykColorSpace, 0.9f));
        canvas.rectangle(306, 716, 36, 36);
        canvas.fillStroke();

//        // step 5
        pdfDoc.close();
    }

    private PdfSpecialCs.Separation createGrayColorSpace(float gray) {
        float[] c1 = new float[]{gray};
        float[] c0 = new float[]{1};
        PdfFunction f = new PdfFunction.Type2(new PdfArray(new float[]{0, 1}), null, new PdfArray(c0), new PdfArray(c1), new PdfNumber(1));
        PdfSpecialCs.Separation cs = new PdfSpecialCs.Separation("iTextSpotColorGray", new DeviceGray().getColorSpace(), f);
        return cs;
    }

    private PdfSpecialCs.Separation createRgbColorSpace(float red, float green, float blue) {
        float[] c0 = new float[]{1, 1, 1};
        float[] c1 = new float[]{red, green, blue};
        PdfFunction f = new PdfFunction.Type2(new PdfArray(new float[]{0, 1}), null, new PdfArray(c0), new PdfArray(c1), new PdfNumber(1));
        PdfSpecialCs.Separation cs = new PdfSpecialCs.Separation("iTextSpotColorRGB", new DeviceRgb().getColorSpace(), f);

        return cs;
    }

    private PdfSpecialCs.Separation createCmykColorSpace(float c, float m, float y, float k) {
        float[] c0 = new float[]{0, 0, 0, 0};
        float[] c1 = new float[]{c, m, y, k};
        PdfFunction f = new PdfFunction.Type2(new PdfArray(new float[]{0, 1}), null, new PdfArray(c0), new PdfArray(c1), new PdfNumber(1));
        PdfSpecialCs.Separation cs = new PdfSpecialCs.Separation("iTextSpotColorCMYK", new DeviceCmyk(c, m, y, k).getColorSpace(), f);

        return cs;
    }

    private void colorRectangle(PdfCanvas canvas,
                               Color color, float x, float y, float width, float height) {
        canvas.saveState();
        canvas.setFillColor(color);
        canvas.rectangle(x, y, width, height);
        canvas.fillStroke();
        canvas.restoreState();
    }
}
