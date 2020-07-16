package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import com.itextpdf.layout.property.UnitValue;
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

        PdfFormField personal = PdfFormField.createEmptyField(pdfDoc);
        personal.setFieldName("personal");
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Cell cell;

        table.addCell("Your name:");
        cell = new Cell(1, 2);
        PdfTextFormField field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "name");
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        table.addCell("Login:");
        cell = new Cell();
        field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "loginname");
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        cell = new Cell();
        field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "password");
        field.setFieldFlag(PdfFormField.FF_PASSWORD);
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        table.addCell("Your motivation:");
        cell = new Cell(1, 2);
        cell.setHeight(60);
        field = PdfFormField.createText(pdfDoc, new Rectangle(0, 0), "reason");
        field.setMultiline(true);
        field.setFontSize(12);
        personal.addKid(field);
        cell.setNextRenderer(new Listing_08_14_ChildFieldEvent(field, 1, cell));
        table.addCell(cell);
        doc.add(table);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.addField(personal);

        PdfButtonFormField button1 = PdfFormField.createPushButton(pdfDoc, new Rectangle(90, 660, 50, 30), "post", "POST");
        button1.setBackgroundColor(new DeviceGray(0.7f));
        button1.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        button1.setAction(PdfAction.createSubmitForm("/book/request", null,
                PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        form.addField(button1);

        doc.close();
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
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
