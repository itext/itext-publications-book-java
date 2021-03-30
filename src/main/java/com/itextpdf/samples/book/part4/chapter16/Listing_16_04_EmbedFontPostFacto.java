package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Listing_16_04_EmbedFontPostFacto {
    public static final String[] RESULT = {
            "./target/book/part4/chapter16/Listing_16_04_EmbedFontPostFacto_with_font.pdf",
            "./target/book/part4/chapter16/Listing_16_04_EmbedFontPostFacto_without_font.pdf"
    };

    public static final String DEST = RESULT[0];

    public static String FONT
            = "./src/main/resources/font/wds011402.ttf";
    public static String FONTNAME
            = "WaltDisneyScriptv4.1";

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_16_04_EmbedFontPostFacto().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        createPdf(RESULT[1]);
        changePdf(RESULT[1], dest);
    }

    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FONT, "", EmbeddingStrategy.PREFER_NOT_EMBEDDED);
        font.setSubset(false);
        doc.add(new Paragraph("iText in Action").setFont(font).setFontSize(60));
        doc.close();
    }

    public void changePdf(String src, String dest) throws IOException {
        // the font file
        RandomAccessFile raf = new RandomAccessFile(FONT, "r");
        byte fontfile[] = new byte[(int) raf.length()];
        raf.readFully(fontfile);
        raf.close();
        // create a new stream for the font file
        PdfStream stream = new PdfStream(fontfile);
        stream.setCompressionLevel(CompressionConstants.DEFAULT_COMPRESSION);
        stream.put(new PdfName("Length1"), new PdfNumber(fontfile.length));
        // create a reader object
        PdfReader reader = new PdfReader(src);
        PdfObject object;
        PdfDictionary font;
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        PdfName fontname = new PdfName(FONTNAME);
        for (int i = 0; i < pdfDoc.getNumberOfPdfObjects(); i++) {
            object = pdfDoc.getPdfObject(i);
            if (object == null || !object.isDictionary())
                continue;
            font = (PdfDictionary) object;
            if (PdfName.FontDescriptor.equals(font.get(PdfName.Type))
                    && fontname.equals(font.get(PdfName.FontName))) {
                font.put(PdfName.FontFile, stream);
            }
        }
        pdfDoc.close();
    }
}
