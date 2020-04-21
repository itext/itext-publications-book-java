package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.Map;

public class Listing_13_15_InspectForm {
    public static final String DEST
            = "./target/book/part4/chapter13/Listing_13_15_InspectForm_fieldflags.txt";

    public static final String SUBSCRIBE
            = "./src/main/resources/pdfs/subscribe.pdf";

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_15_InspectForm().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dest));
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
}
