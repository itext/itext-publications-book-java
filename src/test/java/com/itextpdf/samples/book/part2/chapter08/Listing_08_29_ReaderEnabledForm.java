/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class Listing_08_29_ReaderEnabledForm extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter08/Listing_08_29_ReaderEnabledForm_xfa_preserved.pdf";
    public static final String RESOURCE = "./src/test/resources/pdfs/xfa_enabled.pdf";
    public static final String[] RESULT = {
            "./target/test/resources/book/part2/chapter08/Listing_08_29_ReaderEnabledForm_xfa_broken.pdf",
            "./target/test/resources/book/part2/chapter08/Listing_08_29_ReaderEnabledForm_xfa_removed.pdf",
            "./target/test/resources/book/part2/chapter08/Listing_08_29_ReaderEnabledForm_xfa_preserved.pdf"
    };

    public static final String[] CMP_RESULT = {
            "./src/test/resources/book/part2/chapter08/cmp_Listing_08_29_ReaderEnabledForm_xfa_broken.pdf",
            "./src/test/resources/book/part2/chapter08/cmp_Listing_08_29_ReaderEnabledForm_xfa_removed.pdf",
            "./src/test/resources/book/part2/chapter08/cmp_Listing_08_29_ReaderEnabledForm_xfa_preserved.pdf"
    };

    /**
     * Removes any usage rights that this PDF may have. Only Adobe can grant usage rights
     * and any PDF modification with iText will invalidate them. Invalidated usage rights may
     * confuse Acrobat and it's advisable to remove them altogether.
     */
    public void removeUsageRights(PdfDocument pdfDoc) {
        PdfDictionary perms = pdfDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.Perms);
        if (perms == null) {
            return;
        }
        perms.remove(new PdfName("UR"));
        perms.remove(PdfName.UR3);
        if (perms.size() == 0) {
            pdfDoc.getCatalog().remove(PdfName.Perms);
        }
    }

    public void manipulatePdf2(String src, String dest, boolean remove, boolean preserve) throws IOException {
        // create the reader
        PdfReader reader = new PdfReader(src);
        // create the pdfDoc
        PdfDocument pdfDoc;
        // preserve the reader enabling by creating a PDF in append mode (or not)
        if (preserve) {
            pdfDoc = new PdfDocument(reader, new PdfWriter(dest), new StampingProperties().useAppendMode());
        } else {
            pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        }
        // remove the usage rights (or not)
        if (remove) {
            removeUsageRights(pdfDoc);
        }
        // fill out the fields
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.getField("movie[0].#subform[0].title[0]").setValue("The Misfortunates");
        form.getField("movie[0].#subform[0].original[0]").setValue("De helaasheid der dingen");
        form.getField("movie[0].#subform[0].duration[0]").setValue("108");
        form.getField("movie[0].#subform[0].year[0]").setValue("2009");
        pdfDoc.close();
    }

    public static void main(String[] args) throws Exception {
        new Listing_08_29_ReaderEnabledForm().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        manipulatePdf2(RESOURCE, RESULT[0], false, false);
        manipulatePdf2(RESOURCE, RESULT[1], true, false);
        manipulatePdf2(RESOURCE, RESULT[2], false, true);
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
