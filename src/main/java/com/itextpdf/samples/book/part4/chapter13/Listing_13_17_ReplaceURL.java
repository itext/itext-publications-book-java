/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.forms.fields.NonTerminalFormFieldBuilder;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.forms.fields.TextFormFieldBuilder;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.book.part2.chapter08.Listing_08_14_ChildFieldEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_17_ReplaceURL {
    public static final String[] RESULT = {
            "./target/book/part4/chapter13/Listing_13_17_ReplaceURL_submit1.pdf",
            "./target/book/part4/chapter13/Listing_13_17_ReplaceURL_submit2.pdf"
    };

    public static final String DEST = RESULT[1];

    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFormField personal =
                new NonTerminalFormFieldBuilder(pdfDoc, "personal").createNonTerminalFormField();
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Cell cell;

        table.addCell("Your name:");
        cell = new Cell(1, 2);
        PdfTextFormField field = new TextFormFieldBuilder(pdfDoc, "name")
                .setWidgetRectangle(new Rectangle(0, 0)).createText();
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        table.addCell("Login:");
        cell = new Cell();
        field = new TextFormFieldBuilder(pdfDoc, "loginname")
                .setWidgetRectangle(new Rectangle(0, 0)).createText();
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        cell = new Cell();
        field = new TextFormFieldBuilder(pdfDoc, "password")
                .setWidgetRectangle(new Rectangle(0, 0)).createText();
        field.setFieldFlag(PdfFormField.FF_PASSWORD);
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        table.addCell("Your motivation:");
        cell = new Cell(1, 2);
        cell.setHeight(60);
        field = new TextFormFieldBuilder(pdfDoc, "reason")
                .setWidgetRectangle(new Rectangle(0, 0)).createText();
        field.setMultiline(true);
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        doc.add(table);
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
        form.addField(personal);

        PdfButtonFormField button1 = new PushButtonFormFieldBuilder(pdfDoc, "post")
                .setWidgetRectangle(new Rectangle(90, 660, 50, 30)).setCaption("POST").createPushButton();
        button1.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.7f));
        button1.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        button1.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm("/book/request", null,
                PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        form.addField(button1);

        doc.close();
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
        PdfFormField field = form.getField("post");
        PdfDictionary action = field.getPdfObject().getAsDictionary(PdfName.A);
        PdfDictionary f = action.getAsDictionary(PdfName.F);
        f.put(PdfName.F, new PdfString("http://itextpdf.com:8080/book/request"));
        pdfDoc.close();
    }

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_17_ReplaceURL().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        createPdf(RESULT[0]);
        changePdf(RESULT[0], RESULT[1]);
    }
}
