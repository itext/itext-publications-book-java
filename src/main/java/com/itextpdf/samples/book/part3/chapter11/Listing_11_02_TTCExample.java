package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeCollection;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;

public class Listing_11_02_TTCExample {
    public static final String DEST
            = "./target/book/part3/chapter11/Listing_11_02_TTCExample.pdf";
    // Notice that we'va changed windows MS Gothic to IPA Gothic so the results in comparison with itext5 are different
    public static final String FONT
            = "./src/main/resources/font/ipam.ttc";
    // public static final String FONT = "c:/windows/fonts/msgothic.ttc";

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_02_TTCExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        // License file is loaded because open type font is used and typography module is in classpath:
        // typography module is utilized and requires license.
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font;
        TrueTypeCollection coll = new TrueTypeCollection(FONT);
        for (int i = 0; i < coll.getTTCSize(); i++) {
            font = PdfFontFactory.createFont(coll.getFontByTccIndex(i), PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);
            doc.add(new Paragraph("font " + i + ": " + coll.getFontByTccIndex(i).getFontNames().getFontName())
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("Rash\u00f4mon")
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("Directed by Akira Kurosawa")
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("\u7f85\u751f\u9580")
                    .setFont(font).setFontSize(12));
            doc.add(new Paragraph("\n"));
        }
        doc.close();
    }
}
