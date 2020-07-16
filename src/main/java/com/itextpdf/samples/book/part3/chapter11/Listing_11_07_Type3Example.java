package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfType3Font;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;

public class Listing_11_07_Type3Example {
    public static final String DEST = "./target/book/part3/chapter11/Listing_11_07_Type3Example.pdf";

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_07_Type3Example().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfType3Font t3 = PdfFontFactory.createType3Font(pdfDoc, true);

        PdfCanvas d = t3.addGlyph('D', 600, 0, 0, 600, 700);
        d.setStrokeColor(new DeviceRgb(0xFF, 0x00, 0x00));
        d.setFillColor(new DeviceGray(0.75f));
        d.setLineWidth(100);
        d.moveTo(5, 5);
        d.lineTo(300, 695);
        d.lineTo(595, 5);
        d.closePathFillStroke();

        PdfCanvas s = t3.addGlyph('S', 600, 0, 0, 600, 700);
        s.setStrokeColor(new DeviceRgb(0x00, 0x80, 0x80));
        s.setLineWidth(100);
        s.moveTo(595, 5);
        s.lineTo(5, 5);
        s.lineTo(300, 350);
        s.lineTo(5, 695);
        s.lineTo(595, 695);
        s.stroke();

        Paragraph p = new Paragraph();
        p.add("This is a String with a Type3 font that contains a fancy Delta (");
        p.add(new Text("D").setFont(t3));
        p.add(") and a custom Sigma (");
        p.add(new Text("S").setFont(t3));
        p.add(").");
        doc.add(p);

        doc.close();
    }
}
