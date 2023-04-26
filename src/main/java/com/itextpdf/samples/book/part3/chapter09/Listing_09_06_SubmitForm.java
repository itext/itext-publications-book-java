package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
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
        PdfButtonFormField submit1 = new PushButtonFormFieldBuilder(pdfDoc, "POST")
                .setWidgetRectangle(new Rectangle(90, 660, 50, 30)).setCaption("post").createPushButton();
        submit1.setFontSize(12);
        submit1.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.75f));
        submit1.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        submit1.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm(
                "/book/request", null,
                PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        // add the button
        form.addField(submit1);
        // create a submit button that posts the form as FDF
        PdfButtonFormField submit2 = new PushButtonFormFieldBuilder(pdfDoc, "FDF")
                .setWidgetRectangle(new Rectangle(200, 660, 50, 30)).setCaption("FDF").createPushButton();
        submit2.setFontSize(12);
        submit2.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.7f));
        submit2.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        submit2.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm(
                "/book/request", null, PdfAction.SUBMIT_EXCL_F_KEY));
        // add the button
        form.addField(submit2);
        // create a submit button that posts the form as XFDF
        PdfButtonFormField submit3 = new PushButtonFormFieldBuilder(pdfDoc, "XFDF")
                .setWidgetRectangle(new Rectangle(310, 660, 50, 30)).setCaption("XFDF").createPushButton();
        submit3.setFontSize(12);
        submit3.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.7f));
        submit3.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        submit3.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm(
                "/book/request", null, PdfAction.SUBMIT_XFDF));
        // add the button
        form.addField(submit3);
        // create a reset button
        PdfButtonFormField reset = new PushButtonFormFieldBuilder(pdfDoc, "reset")
                .setWidgetRectangle(new Rectangle(420, 660, 50, 30)).setCaption("RESET").createPushButton();
        reset.setFontSize(12);
        reset.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.7f));
        reset.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        reset.getFirstFormAnnotation().setAction(PdfAction.createResetForm(null, 0));
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
