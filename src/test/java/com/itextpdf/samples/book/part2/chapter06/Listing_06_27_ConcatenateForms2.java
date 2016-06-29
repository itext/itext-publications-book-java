/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Category(SampleTest.class)
public class Listing_06_27_ConcatenateForms2 extends GenericTest {
    public static final String DATASHEET =
            "./src/test/resources/pdfs/datasheet.pdf";
    public static final String DEST =
            "./target/test/resources/book/part2/chapter06/Listing_06_27_ConcatenateForms2.pdf";

    public static void main(String[] args)
            throws SQLException, IOException {
        new Listing_06_27_ConcatenateForms2().manipulatePdf(DEST);
    }

    private static byte[] renameFieldsIn(String datasheet, int i) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Create the stamper
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(datasheet), new PdfWriter(baos));
        // Get the fields
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        // Loop over the fields
        Map<String, PdfFormField> map = form.getFormFields();
        Set<String> keys = new HashSet<>(map.keySet());
        for (String key : keys) {
            // rename the fields
            form.renameField(key, String.format("%s_%d", key, i));
        }
        // close the stamper
        pdfDoc.close();
        return baos.toByteArray();
    }

    @Override
    public void manipulatePdf(String dest) throws SQLException, IOException {
        // Create the result document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();

        // Create the first source document
        PdfDocument srcDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(renameFieldsIn(DATASHEET, 1))));

        srcDoc.copyPagesTo(1, 1, pdfDoc, new PdfPageFormCopier());
        srcDoc.close();

        // Create the second source document
        srcDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(renameFieldsIn(DATASHEET, 2))));

        srcDoc.copyPagesTo(1, 1, pdfDoc, new PdfPageFormCopier());
        srcDoc.close();

        pdfDoc.close();
    }
}
