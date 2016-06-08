/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Listing_09_11_XFDFServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        // We get a resource from our web app
        InputStream is
                = getServletContext().getResourceAsStream("/subscribe.pdf");
        // We create a reader with the InputStream
        PdfReader reader = new PdfReader(is, null);
        // We create an OutputStream for the new PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Now we create the PDF
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(baos));
        // We add a submit button to the existing form
        PdfButtonFormField submit = PdfFormField.createPushButton(
                pdfDoc, new Rectangle(90, 660, 50, 30), "submit", "POST");
        submit.setBackgroundColor(new DeviceGray(0.7f));
        submit.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        submit.setAction(PdfAction.createSubmitForm(
                "/book/xfdf", null, PdfAction.SUBMIT_XFDF));
        PdfAcroForm.getAcroForm(pdfDoc, true).addField(submit, pdfDoc.getFirstPage());
        pdfDoc.close();
        // We write the PDF bytes to the OutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        // Create a reader that interprets the request's input stream
        // TODO DEVSIX-526
        // XfdfReader xfdf = new XfdfReader(request.getInputStream());
        // We get a resource from our web app
        InputStream is
                = getServletContext().getResourceAsStream("/subscribe.pdf");
        // We create a reader with the InputStream
        PdfReader reader = new PdfReader(is, null);
        // We create an OutputStream for the new PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Now we create the PDF
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(baos));
        // We alter the fields of the existing PDF
        // AcroFields fields = stamper.getAcroFields();
        // fields.setFields(xfdf);
        PdfAcroForm.getAcroForm(pdfDoc, true).flattenFields();
        // close the pdfDocument
        pdfDoc.close();
        // We write the PDF bytes to the OutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
    }

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 7582490560292953774L;
}
