/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(SampleTest.class)
public class Listing_08_15_Subscribe extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter08/Listing_08_15_Subscribe.pdf";
    public static final String SRC = "./target/test/resources/book/part2/chapter08/subscribe.pdf";

    private String name = "Bruno Lowagie";
    private String login = "blowagie";

    public static void main(String[] args) throws Exception {
        Listing_08_15_Subscribe subscribe = new Listing_08_15_Subscribe();
        subscribe.createPdf(SRC);
        subscribe.name = "Bruno Lowagie";
        subscribe.login = "blowagie";
        subscribe.manipulatePdf(String.format(DEST, 1));
    }

    protected void manipulatePdf(String dest) throws Exception {
        createPdf(SRC);

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.removeField("personal.password");
        form.getField("personal.name").setValue(name);
        form.getField("personal.loginname").setValue(login);
        form.renameField("personal.reason", "motivation");
        form.getField("personal.loginname").setReadOnly(true);
        form.partialFormFlattening("personal.name");
        form.flattenFields();

        //Close document
        pdfDoc.close();
    }

    protected void createPdf(String filename) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);

        PdfFormField personal = PdfFormField.createEmptyField(pdfDoc);
        personal.setFieldName("personal");
        Table table = new Table(3);
        Cell cell;

        table.addCell(new Cell().add(new Paragraph("Your name:")));
        cell = new Cell(1, 2);
        PdfTextFormField field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "name", "");
        field.setBorderWidth(0);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        table.addCell(new Cell().add(new Paragraph("Login:")));
        cell = new Cell();
        field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "loginname", "");
        field.setBorderWidth(0);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        cell = new Cell();
        field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "password", "");
        field.setBorderWidth(0);
        field.setPassword(true);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        table.addCell(new Cell().add(new Paragraph("Your motivation:")));
        cell = new Cell(1, 2);
        cell.setHeight(60);
        field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "reason", "");
        field.setBorderWidth(0);
        field.setMultiline(true);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);

        doc.add(table);

        PdfAcroForm.getAcroForm(pdfDoc, true).addField(personal);

        doc.close();
    }
}
