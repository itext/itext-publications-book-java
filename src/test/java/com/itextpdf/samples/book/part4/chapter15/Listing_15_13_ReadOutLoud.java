/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeCollection;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_15_13_ReadOutLoud extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter15/Listing_15_13_ReadOutLoud.pdf";
    public static String RESOURCE = "./src/test/resources/img/posters/0062622.jpg";

    public static void main(String args[]) throws IOException {
        new Listing_15_13_ReadOutLoud().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setTagged();

        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas canvas = new PdfCanvas(page);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA, PdfEncodings.CP1252, false);
        TrueTypeCollection coll = new TrueTypeCollection("./src/test/resources/font/ipam.ttc");
        PdfFont font2 = PdfFontFactory.createFont(coll.getFontByTccIndex(1), PdfEncodings.IDENTITY_H);

        TagTreePointer tagPointer = new TagTreePointer(pdfDoc);
        tagPointer.setPageForTagging(page);
        tagPointer.addTag(PdfName.Div);

        tagPointer.addTag(PdfName.Span);
        canvas.beginText();
        canvas.moveText(36, 788);
        canvas.setFontAndSize(font, 12);
        canvas.setLeading(18);
        canvas.openTag(tagPointer.getTagReference());
        canvas.showText("These are some famous movies by Stanley Kubrick: ");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(PdfName.Span).getProperties().setExpansion("Doctor");
        canvas.openTag(tagPointer.getTagReference());
        canvas.newlineShowText("Dr.");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(PdfName.Span);
        canvas.openTag(tagPointer.getTagReference());
        canvas.showText(" Strangelove or: How I Learned to Stop Worrying and Love the Bomb.");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(PdfName.Span).getProperties().setExpansion("Eyes Wide Shut.");
        canvas.openTag(tagPointer.getTagReference());
        canvas.newlineShowText("EWS");
        canvas.closeTag();
        canvas.endText();

        tagPointer.moveToParent().addTag(PdfName.Span).getProperties().setLanguage("en-us").setAlternateDescription("2001: A Space Odyssey");
        ImageData img = ImageDataFactory.create(RESOURCE);
        canvas.openTag(tagPointer.getTagReference());
        canvas.addImage(img, 36, 640, 100, false, false);
        canvas.closeTag();

        tagPointer.moveToParent().addTag(PdfName.Span);
        canvas.beginText();
        canvas.moveText(36, 620);
        canvas.setFontAndSize(font, 12);
        canvas.openTag(tagPointer.getTagReference());
        canvas.showText("This is a movie by Akira Kurosawa: ");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(PdfName.Span).getProperties().setActualText("Seven Samurai.");
        canvas.openTag(tagPointer.getTagReference());
        canvas.setFontAndSize(font2, 12);
        canvas.showText("\u4e03\u4eba\u306e\u4f8d");
        canvas.closeTag();
        canvas.endText();


        pdfDoc.close();
    }
}
