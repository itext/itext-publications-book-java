package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import java.io.File;

public class Listing_08_11_TextFieldActions {
    public static final String DEST = "./target/book/part2/chapter08/Listing_08_11_TextFieldActions.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_08_11_TextFieldActions().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        pdfDoc.addNewPage();

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.put(PdfName.NeedAppearances, new PdfBoolean(true));

        PdfFormField date = PdfFormField.createText(pdfDoc, new Rectangle(36, 780, 90, 26)).
            setFieldName("date").
            setBorderColor(new DeviceGray(0.2f)).
            setAdditionalAction(PdfName.V, PdfAction.createJavaScript(
                    "AFDate_FormatEx( 'dd-mm-yyyy' );"));

        form.addField(date);

        PdfFormField name = PdfFormField.createText(pdfDoc, new Rectangle(130, 780, 126, 26)).
                setFieldName("name").
                setBorderColor(new DeviceGray(0.2f)).
                setAdditionalAction(PdfName.Fo, PdfAction.createJavaScript("app.alert('name field got the focus');")).
                setAdditionalAction(PdfName.Bl, PdfAction.createJavaScript("app.alert('name lost the focus');")).
                setAdditionalAction(PdfName.K, PdfAction.createJavaScript("event.change = event.change.toUpperCase();"));

        form.addField(name);

        //Close document
        pdfDoc.close();
    }
}
