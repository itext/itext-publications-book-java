package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class Listing_02_29_RiverPhoenix {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_29_RiverPhoenix.pdf";

    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_29_RiverPhoenix().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

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
                .create(String.format("./src/main/resources/img/posters/%s.jpg", imdb)));
        img.scaleToFit(1000,72);
        img.setRotationAngle(Math.toRadians(-30));
        p.add(img);
        return p;
    }
}
