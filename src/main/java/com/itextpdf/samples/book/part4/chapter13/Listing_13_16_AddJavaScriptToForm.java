package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_16_AddJavaScriptToForm {
    public static final String[] RESULT = {
            "./target/book/part4/chapter13/Listing_13_16_AddJavaScriptToForm.pdf",
            "./target/book/part4/chapter13/Listing_13_16_AddJavaScriptToForm_form_without_js.pdf"
    };

    public static final String DEST = RESULT[0];

    public static final String RESOURCE
            = "./src/main/resources/js/extra.js";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_16_AddJavaScriptToForm().manipulatePdf(DEST);
    }

    public void createPdf(String dest) throws IOException{
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());

        canvas.beginText();
        canvas.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 12);
        canvas.moveText(36, 770);
        canvas.showText("Married?");
        canvas.moveText(22, -20) ;// 58, 750);
        canvas.showText("YES");
        canvas.moveText(44, 0); //102, 750);
        canvas.showText("NO");
        canvas.moveText(-66, -20); // 36, 730);
        canvas.showText("Name partner?");
        canvas.endText();

        // create a radio button field
        PdfButtonFormField married = PdfFormField.createRadioGroup(pdfDoc, "married", "Yes");
        Rectangle rectYes = new Rectangle(40, 744, 16, 22);
        PdfFormField yes = PdfFormField.createRadioButton(pdfDoc, rectYes, married, "Yes");
        Rectangle rectNo = new Rectangle(84, 744, 16, 22);
        PdfFormField no = PdfFormField.createRadioButton(pdfDoc, rectNo, married, "No");
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.addField(married);
        // create a text field
        Rectangle rect = new Rectangle(40, 710, 160, 16);
        PdfTextFormField partner = PdfFormField.createText(pdfDoc, rect, "partner", "partner");
        partner.setBorderColor(ColorConstants.DARK_GRAY);
        partner.setBorderWidth(0.5f);
        form.addField(partner);

        pdfDoc.close();
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        // Add JavaScript
        pdfDoc.getCatalog().setOpenAction(PdfAction.createJavaScript(readFileToString(RESOURCE).replace("\r\n", "\n")));
        // get the form fields
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        PdfFormField fd = form.getField("married");
        // Get the PDF dictionary of the YES radio button and add an additional action
        fd.getWidgets().get(0).setAdditionalAction(PdfName.Fo, PdfAction.createJavaScript("setReadOnly(false);"));
        // Get the PDF dictionary of the NO radio button and add an additional action
        fd.getWidgets().get(1).setAdditionalAction(PdfName.Fo, PdfAction.createJavaScript("setReadOnly(true);"));
        PdfButtonFormField button = PdfFormField.createPushButton(pdfDoc, new Rectangle(40, 690, 160, 20), "submit", "validate and submit");
        button.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        button.setAction(PdfAction.createJavaScript("validate();"));
        form.addField(button);
        // close the document
        pdfDoc.close();
    }

    protected static String readFileToString(String path) throws IOException {
        File file = new File(path);
        byte[] jsBytes = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(jsBytes);
        return new String(jsBytes);
    }


    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        createPdf(RESULT[1]);
        changePdf(RESULT[1], dest);
    }
}
