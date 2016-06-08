/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
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

import org.junit.Ignore;

@Ignore
public class Listing_09_10_FDFServlet extends HttpServlet {

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
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        // We add a submit button to the existing form
        PdfButtonFormField submit = PdfFormField.createPushButton(
                pdfDoc, new Rectangle(90, 660, 50, 30), "submit", "POST");
        submit.setBackgroundColor(new DeviceGray(0.7f));
        submit.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        submit.setAction(PdfAction.createSubmitForm("/book/fdf", null, 0));
        form.addField(submit);
        // We add an extra field that can be used to upload a file
        PdfTextFormField upload = PdfFormField.createText(
                pdfDoc, new Rectangle(160, 660, 310, 30), "image", "image");
        upload.setFileSelect(true);
        upload.setBackgroundColor(new DeviceGray(0.9f));
        upload.setAdditionalAction(PdfName.U,
                PdfAction.createJavaScript(
                        "this.getField('image').browseForFileToSubmit();"
                                + "this.getField('submit').setFocus();"));
        form.addField(upload);
        // Close the document
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
        response.setHeader("Content-Disposition",
                "inline; filename=\"your.pdf\"");
        // Create a reader that interprets the request's input stream
        // TODO DEVSIX-159
        // FdfReader fdf = new FdfReader(request.getInputStream());
        // We get a resource from our web app
        InputStream is = getServletContext().getResourceAsStream("/subscribe.pdf");
        // We create a reader with the InputStream
        PdfReader reader = new PdfReader(is, null);
        // We create an OutputStream for the new PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Now we create the PDF
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(baos));
        // We alter the fields of the existing PDF
        // AcroFields fields = stamper.getAcroFields();
        // fields.setFields(fdf);
        PdfAcroForm.getAcroForm(pdfDoc, true).flattenFields();
        // Gets the image from the FDF file
        // try {
        // Image img = Image.getInstance(fdf.getAttachedFile("image"));
        // img.scaleToFit(100, 100);
        // img.setAbsolutePosition(90, 590);
        // stamper.getOverContent(1).addImage(img);
        // } catch (IOException ioe) {
        //    new Canvas(new PdfCanvas(pdfDoc.getFirstPage()), pdfDoc, pdfDoc.getFirstPage().getPageSize())
        //            .showTextAligned("No image posted!", 90, 660, Property.TextAlignment.LEFT);
        //}
        // close the stamper
        pdfDoc.close();
        // fdf.close();
        // We write the PDF bytes to the OutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 2157128985625139848L;
}
