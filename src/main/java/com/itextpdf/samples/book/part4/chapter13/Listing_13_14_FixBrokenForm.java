package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_13_14_FixBrokenForm {
    public static final String ORIGINAL = "./src/main/resources/pdfs/broken_form.pdf";
    public static final String[] RESULT = {
            "./target/book/part4/chapter13/Listing_13_14_FixBrokenForm_fixed_form.pdf",
            "./target/book/part4/chapter13/Listing_13_14_FixBrokenForm_broken_form.pdf",
            "./target/book/part4/chapter13/Listing_13_14_FixBrokenForm_filled_form.pdf"
    };

    public static final String DEST = RESULT[0];

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_14_FixBrokenForm().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        changePdf(ORIGINAL, RESULT[0]);
        fillData(ORIGINAL, RESULT[1]);
        fillData(RESULT[0], RESULT[2]);
    }

    public void changePdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfDictionary root = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary form = root.getAsDictionary(PdfName.AcroForm);
        PdfArray fields = form.getAsArray(PdfName.Fields);

        PdfDictionary page;
        PdfArray annots;
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            page = pdfDoc.getPage(i).getPdfObject();
            annots = page.getAsArray(PdfName.Annots);
            for (int j = 0; j < annots.size(); j++) {
                fields.add(annots.get(j).getIndirectReference());
            }
        }
        pdfDoc.close();
    }

    public void fillData(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        if (null != form.getField("title")) {
            form.getField("title").setValue("The Misfortunates");
        }
        if (null != form.getField("director")) {
            form.getField("director").setValue("Felix Van Groeningen");
        }
        if (null != form.getField("year")) {
            form.getField("year").setValue("2009");
        }
        if (null != form.getField("duration")) {
            form.getField("duration").setValue("108");
        }
        pdfDoc.close();
    }
}
