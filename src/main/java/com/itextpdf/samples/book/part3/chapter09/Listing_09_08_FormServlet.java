/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class Listing_09_08_FormServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/pdf");
        // We get a resource from our web app
        InputStream is = getServletContext().getResourceAsStream("/subscribe.pdf");
        // We create an OutputStream for the new PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Now we create the PDF
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(is, null), new PdfWriter(baos));
        // We add a submit button to the existing form
        PdfButtonFormField submit = new PushButtonFormFieldBuilder(pdfDoc, "submit")
                .setWidgetRectangle(new Rectangle(90, 660, 50, 30)).setCaption("POST").createPushButton();
        submit.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.7f));
        submit.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        submit.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm(
                "/book/form", null, PdfAction.SUBMIT_HTML_FORMAT));
        PdfFormCreator.getAcroForm(pdfDoc, true).addField(submit);
        // We write the PDF bytes to the OutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/pdf");
        // We get a resource from our web app
        InputStream is
                = getServletContext().getResourceAsStream("/subscribe.pdf");
        // We create an OutputStream for the new PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Now we create the PDF
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(is, null), new PdfWriter(baos));
        // We alter the fields of the existing PDF
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
        form.getField("password").setFieldFlag(PdfFormField.FF_PASSWORD, false);
        Set<String> parameters = form.getAllFormFields().keySet();
        for (String parameter : parameters) {
            form.getField(parameter).setValue(request.getParameter(parameter));
        }
        form.flattenFields();
        pdfDoc.close();
        // We write the PDF bytes to the OutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
    }

    /**
     * A serial version UID
     */
}
