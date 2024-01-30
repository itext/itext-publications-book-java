/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.RadioFormFieldBuilder;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class Listing_08_02_RadioButtons {
    public static final String DEST = "./target/book/part2/chapter08/Listing_08_02_RadioButtons.pdf";
    /**
     * Possible values of a Choice field.
     */
    public static final String[] LANGUAGES = {"English", "German", "French", "Spanish", "Dutch"};

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_08_02_RadioButtons().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_NOT_EMBEDDED);
        RadioFormFieldBuilder builder = new RadioFormFieldBuilder(pdfDoc, "language");
        PdfButtonFormField radioGroup = builder.createRadioGroup();
        radioGroup.setValue("");
        radioGroup.setFieldName("language");
        Rectangle rect = new Rectangle(40, 806, 60 - 40, 788 - 806);
        for (int page = 1; page <= LANGUAGES.length; page++) {
            pdfDoc.addNewPage();
            PdfFormAnnotation radio = (PdfFormAnnotation) builder.createRadioButton(LANGUAGES[page - 1], rect)
                    .setPage(page)
                    .setColor(new DeviceGray(0.8f));
            radioGroup.addKid(radio);
            doc.showTextAligned(new Paragraph(LANGUAGES[page - 1]).setFont(font).setFontSize(18),
                    70, 790, page, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        }
        PdfFormCreator.getAcroForm(pdfDoc, true).addField(radioGroup);
        doc.close();
    }
}
