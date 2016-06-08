/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_13_10_RemoveLaunchActions extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter13/Listing_13_10_RemoveLaunchActions.pdf";
    public static final String LAUNCH_ACTIONS
            = "./src/test/resources/book/part2/chapter07/cmp_Listing_07_10_LaunchAction.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        new Listing_13_10_RemoveLaunchActions().manipulatePdf(DEST);
    }

    @Override
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
