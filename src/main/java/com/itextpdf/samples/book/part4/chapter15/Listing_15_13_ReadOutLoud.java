package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeCollection;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;

import java.io.File;
import java.io.IOException;

public class Listing_15_13_ReadOutLoud {
    public static final String DEST = "./target/book/part4/chapter15/Listing_15_13_ReadOutLoud.pdf";
    public static String RESOURCE = "./src/main/resources/img/posters/0062622.jpg";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_15_13_ReadOutLoud().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setTagged();

        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas canvas = new PdfCanvas(page);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA, PdfEncodings.CP1252, EmbeddingStrategy.PREFER_NOT_EMBEDDED);
        TrueTypeCollection coll = new TrueTypeCollection("./src/main/resources/font/ipam.ttc");
        PdfFont font2 = PdfFontFactory.createFont(coll.getFontByTccIndex(1), PdfEncodings.IDENTITY_H);

        TagTreePointer tagPointer = new TagTreePointer(pdfDoc);
        tagPointer.setPageForTagging(page);
        tagPointer.addTag(StandardRoles.DIV);

        tagPointer.addTag(StandardRoles.SPAN);
        canvas.beginText();
        canvas.moveText(36, 788);
        canvas.setFontAndSize(font, 12);
        canvas.setLeading(18);
        canvas.openTag(tagPointer.getTagReference());
        canvas.showText("These are some famous movies by Stanley Kubrick: ");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(StandardRoles.SPAN).getProperties().setExpansion("Doctor");
        canvas.openTag(tagPointer.getTagReference());
        canvas.newlineShowText("Dr.");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(StandardRoles.SPAN);
        canvas.openTag(tagPointer.getTagReference());
        canvas.showText(" Strangelove or: How I Learned to Stop Worrying and Love the Bomb.");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(StandardRoles.SPAN).getProperties().setExpansion("Eyes Wide Shut.");
        canvas.openTag(tagPointer.getTagReference());
        canvas.newlineShowText("EWS");
        canvas.closeTag();
        canvas.endText();

        tagPointer.moveToParent().addTag(StandardRoles.SPAN).getProperties().setLanguage("en-us").setAlternateDescription("2001: A Space Odyssey");
        ImageData img = ImageDataFactory.create(RESOURCE);
        canvas.openTag(tagPointer.getTagReference());
        canvas.addImageFittedIntoRectangle(img, new Rectangle(36, 640, 65, 100), false);
        canvas.closeTag();

        tagPointer.moveToParent().addTag(StandardRoles.SPAN);
        canvas.beginText();
        canvas.moveText(36, 620);
        canvas.setFontAndSize(font, 12);
        canvas.openTag(tagPointer.getTagReference());
        canvas.showText("This is a movie by Akira Kurosawa: ");
        canvas.closeTag();

        tagPointer.moveToParent().addTag(StandardRoles.SPAN).getProperties().setActualText("Seven Samurai.");
        canvas.openTag(tagPointer.getTagReference());
        canvas.setFontAndSize(font2, 12);
        canvas.showText("\u4e03\u4eba\u306e\u4f8d");
        canvas.closeTag();
        canvas.endText();


        pdfDoc.close();
    }
}
