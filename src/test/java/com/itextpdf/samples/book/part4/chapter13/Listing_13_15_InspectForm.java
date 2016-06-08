/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.Map;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_13_15_InspectForm extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter13/Listing_13_15_InspectForm.pdf";
    public static final String DESTTXT
            = "./target/test/resources/book/part4/chapter13/Listing_13_15_InspectForm_fieldflags.txt";
    public static final String CMPTXT
            = "./src/test/resources/book/part4/chapter13/cmp_Listing_13_15_InspectForm_fieldflags.txt";
    public static final String SUBSCRIBE
            = "./src/test/resources/pdfs/subscribe.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        new Listing_13_15_InspectForm().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(DESTTXT));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SUBSCRIBE));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        PdfDictionary dict;
        int flags;
        PdfFormField item;
        for (String key : fields.keySet()) {
            out.write(key);
            item = fields.get(key);
            dict = item.getPdfObject();
            if (null == dict.getAsNumber(PdfName.Ff)) {
                // being here means that we should inspect widget annotations, (see getMerged() in itext5)
                dict = item.getWidgets().get(0).getPdfObject();
            }
            if (null != dict.getAsNumber(PdfName.Ff)) {
                flags = dict.getAsNumber(PdfName.Ff).intValue();
                if ((flags & PdfFormField.FF_PASSWORD) > 0)
                    out.write(" -> password");
                if ((flags & PdfFormField.FF_MULTILINE) > 0)
                    out.write(" -> multiline");
            }
            out.write('\n');
        }
        out.flush();
        out.close();
        pdfDoc.close();
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        BufferedReader destReader;
        BufferedReader cmpReader;
        String curDestStr;
        String curCmpStr;
            destReader = new BufferedReader(new InputStreamReader(new FileInputStream(DESTTXT)));
            cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(CMPTXT)));
            int row = 1;
            while ((curDestStr = destReader.readLine()) != null) {
                if ((curCmpStr = cmpReader.readLine()) != null) {
                    addError("The lengths of files are different.");
                }
                if (!curCmpStr.equals(curDestStr)) {
                    addError("The files are different on the row " + row );
                }
                row++;
            }
            if ((curCmpStr = cmpReader.readLine()) != null) {
                addError("The lengths of files are different.");
            }
    }
}
