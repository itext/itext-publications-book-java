/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Assert;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_13_14_FixBrokenForm extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter13/Listing_13_14_FixBrokenForm.pdf";
    public static final String ORIGINAL = "./src/test/resources/pdfs/broken_form.pdf";
    public static final String[] RESULT = {
            "./target/test/resources/book/part4/chapter13/Listing_13_14_FixBrokenForm_fixed_form.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_14_FixBrokenForm_broken_form.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_14_FixBrokenForm_filled_form.pdf"
    };
    public static final String[] CMP_RESULT = {
            "./src/test/resources/book/part4/chapter13/cmp_Listing_13_14_FixBrokenForm_fixed_form.pdf",
            "./src/test/resources/book/part4/chapter13/cmp_Listing_13_14_FixBrokenForm_broken_form.pdf",
            "./src/test/resources/book/part4/chapter13/cmp_Listing_13_14_FixBrokenForm_filled_form.pdf"
    };

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        new Listing_13_14_FixBrokenForm().manipulatePdf(DEST);
    }

    @Override
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

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        String outPath;
        for (int i = 0; i < RESULT.length; i++) {
            outPath = new File(RESULT[i]).getParent();
            if (compareXml) {
                if (!compareTool.compareXmls(RESULT[i], CMP_RESULT[i])) {
                    addError("The XML structures are different.");
                }
            } else {
                if (compareRenders) {
                    addError(compareTool.compareVisually(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                    addError(compareTool.compareLinkAnnotations(dest, cmp));
                } else {
                    addError(compareTool.compareByContent(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                }
                addError(compareTool.compareDocumentInfo(RESULT[i], CMP_RESULT[i]));
            }
        }

        if (errorMessage != null) Assert.fail(errorMessage);
    }
}
