package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_07_26_ButtonsActions {
    public static final String DEST = "./target/book/part2/chapter07/Listing_07_26_ButtonsActions.pdf";

    public static final String MOVIE_TEMPLATES = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        Listing_07_26_ButtonsActions application = new Listing_07_26_ButtonsActions();
        application.arguments = args;
        application.manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES), new PdfWriter(DEST));
        PdfButtonFormField saveAs =
                PdfFormField.createPushButton(pdfDoc, new Rectangle(636, 10, 80, 20), "Save", "Save");
        saveAs.setBorderColor(ColorConstants.BLACK);
        saveAs.setBorderWidth(1);
        saveAs.setColor(ColorConstants.RED);
        PdfAnnotation saveAsButton = saveAs.getWidgets().get(0);
        saveAs.setAction(PdfAction.createJavaScript("app.execMenuItem('SaveAs')"));

        PdfButtonFormField mail =
                PdfFormField.createPushButton(pdfDoc, new Rectangle(736, 10, 80, 20), "Mail", "Mail");
        mail.setBorderColor(ColorConstants.BLACK);
        mail.setBorderWidth(1);
        mail.setColor(ColorConstants.RED);
        PdfWidgetAnnotation mailButton = mail.getWidgets().get(0);
        mailButton.setAction(PdfAction.createJavaScript("app.execMenuItem('AcroSendMail:SendMail')"));
        // Add the annotations to every page of the document
        for (int page = 1; page <= pdfDoc.getNumberOfPages(); page++) {
            pdfDoc.getPage(page).addAnnotation(saveAsButton);
            pdfDoc.getPage(page).addAnnotation(mailButton);
        }
        pdfDoc.close();
    }
}
