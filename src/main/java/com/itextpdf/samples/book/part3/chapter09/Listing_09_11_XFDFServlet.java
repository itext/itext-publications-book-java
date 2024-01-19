/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
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
import com.itextpdf.forms.xfdf.XfdfObject;
import com.itextpdf.forms.xfdf.XfdfObjectFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class Listing_09_11_XFDFServlet extends HttpServlet {
    /**
     * Serial version UID.
     */
    private static Logger logger = LoggerFactory.getLogger(Listing_09_11_XFDFServlet.class);

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
        PdfButtonFormField submit = new PushButtonFormFieldBuilder(pdfDoc, "submit")
                .setWidgetRectangle(new Rectangle(90, 660, 50, 30)).setCaption("POST").createPushButton();
        submit.getFirstFormAnnotation().setBackgroundColor(new DeviceGray(0.7f));
        submit.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        submit.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm(
                "/book/xfdf", null, PdfAction.SUBMIT_XFDF));
        PdfFormCreator.getAcroForm(pdfDoc, true).addField(submit, pdfDoc.getFirstPage());
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
            throws IOException {
        response.setContentType("application/pdf");
        // Create an xfdf object out of the request's input stream
        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = null;
        try {
            xfdfObject = factory.createXfdfObject(request.getInputStream());
        } catch (ParserConfigurationException e) {
            logger.error("Error while initializing xfdf parser.");
        } catch (SAXException e) {
            logger.error("Error while parsing xfdf tag structure.");
        }
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
        if (xfdfObject != null) {
            xfdfObject.mergeToPdf(pdfDoc, "subscribe.pdf");
        }
        PdfFormCreator.getAcroForm(pdfDoc, true).flattenFields();
        // close the pdfDocument
        pdfDoc.close();
        // We write the PDF bytes to the OutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
    }
}
