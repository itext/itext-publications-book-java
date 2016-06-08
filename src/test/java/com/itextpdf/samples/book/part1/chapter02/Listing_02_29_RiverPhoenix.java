/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class Listing_02_29_RiverPhoenix extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_29_RiverPhoenix.pdf";

    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_29_RiverPhoenix().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        // step 4
        doc.add(new Paragraph("Movies featuring River Phoenix").setFont(bold));
        doc.add(createParagraph(
                "My favorite movie featuring River Phoenix was ", "0092005"));
        doc.add(createParagraph(
                "River Phoenix was nominated for an academy award for his role in ", "0096018"));
        doc.add(createParagraph(
                "River Phoenix played the young Indiana Jones in ", "0097576"));
        doc.add(createParagraph(
                "His best role was probably in ", "0102494"));

        doc.close();
    }


    public Paragraph createParagraph(String text, String imdb) throws MalformedURLException {
        Paragraph p = new Paragraph(text);
        Image img = new Image(ImageDataFactory
                .create(String.format("./src/test/resources/img/posters/%s.jpg", imdb)));
        img.scaleToFit(1000,72);
        img.setRotationAngle(Math.toRadians(-30));
        p.add(img);
        return p;
    }
}
