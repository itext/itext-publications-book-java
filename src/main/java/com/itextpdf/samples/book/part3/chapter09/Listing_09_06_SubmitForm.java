package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import java.io.File;
import java.io.IOException;

public class Listing_09_06_SubmitForm {
    public static final String DEST
            = "./target/book/part3/chapter09/Listing_09_06_SubmitForm.pdf";
    public static final String SUBCRIBE
            = "./src/main/resources/pdfs/subscribe.pdf";

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SUBCRIBE), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        PdfButtonFormField submit1 = PdfFormField.createPushButton(pdfDoc, new Rectangle(90, 660, 50, 30), "POST", "post");
        submit1.setBackgroundColor(new DeviceGray(0.75f));
        submit1.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        submit1.setAction(PdfAction.createSubmitForm(
                "/book/request", null,
                PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        // add the button
        form.addField(submit1);
        // create a submit button that posts the form as FDF
        PdfButtonFormField submit2 = PdfFormField.createPushButton(
                pdfDoc, new Rectangle(200, 660, 50, 30), "FDF", "FDF");
        submit2.setBackgroundColor(new DeviceGray(0.7f));
        submit2.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        submit2.setAction(PdfAction.createSubmitForm(
                "/book/request", null, PdfAction.SUBMIT_EXCL_F_KEY));
        // add the button
        form.addField(submit2);
        // create a submit button that posts the form as XFDF
        PdfButtonFormField submit3 = PdfFormField.createPushButton(
                pdfDoc, new Rectangle(310, 660, 50, 30), "XFDF", "XFDF");
        submit3.setBackgroundColor(new DeviceGray(0.7f));
        submit3.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        submit3.setAction(PdfAction.createSubmitForm(
                "/book/request", null, PdfAction.SUBMIT_XFDF));
        // add the button
        form.addField(submit3);
        // create a reset button
        PdfButtonFormField reset = PdfFormField.createPushButton(
                pdfDoc, new Rectangle(420, 660, 50, 30), "reset", "RESET");
        reset.setBackgroundColor(new DeviceGray(0.7f));
        reset.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        reset.setAction(PdfAction.createResetForm(null, 0));
        form.addField(reset);
        // close the document
        pdfDoc.close();
    }

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_09_06_SubmitForm().manipulatePdf(DEST);
    }
}
