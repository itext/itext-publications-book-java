package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_10_RemoveLaunchActions {
    public static final String DEST
            = "./target/book/part4/chapter13/Listing_13_10_RemoveLaunchActions.pdf";
    public static final String LAUNCH_ACTIONS
            = "./src/main/resources/pdfs/cmp_Listing_07_10_LaunchAction.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_10_RemoveLaunchActions().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        changePdf(LAUNCH_ACTIONS, dest);
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfObject object;
        PdfDictionary action;
        for (int i = 1; i < pdfDoc.getNumberOfPdfObjects(); i++) {
            object = pdfDoc.getPdfObject(i);
            if (object instanceof PdfDictionary) {
                action = ((PdfDictionary) object).getAsDictionary(PdfName.A);
                if (action == null) continue;
                if (PdfName.Launch.equals(action.getAsName(PdfName.S))) {
                    action.remove(PdfName.F);
                    action.remove(PdfName.Win);
                    action.put(PdfName.S, PdfName.JavaScript);
                    action.put(PdfName.JS,
                            new PdfString("app.alert('Launch Application Action removed by iText');\r"));
                }
            }
        }
        pdfDoc.close();
    }
}
