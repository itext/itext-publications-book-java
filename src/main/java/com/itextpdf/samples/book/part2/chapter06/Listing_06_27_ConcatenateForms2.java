/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Listing_06_27_ConcatenateForms2 {
    public static final String DATASHEET =
            "./src/main/resources/pdfs/datasheet.pdf";
    public static final String DEST =
            "./target/book/part2/chapter06/Listing_06_27_ConcatenateForms2.pdf";

    public static void main(String[] args)
            throws SQLException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_27_ConcatenateForms2().manipulatePdf(DEST);
    }

    private static byte[] renameFieldsIn(String datasheet, int i) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Create the stamper
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(datasheet), new PdfWriter(baos));
        // Get the fields
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
        // Loop over the fields
        //TODO DEVSIX-6346 Handle form fields without names more carefully
        Map<String, PdfFormField> map = form.getAllFormFields();
        Set<String> keys = new HashSet<>(map.keySet());
        for (String key : keys) {
            // rename the fields
            form.renameField(key, String.format("%s_%d", key, i));
        }
        // close the stamper
        pdfDoc.close();
        return baos.toByteArray();
    }

    public void manipulatePdf(String dest) throws SQLException, IOException {
        // Create the result document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();

        PdfPageFormCopier formCopier = new PdfPageFormCopier();

        // Create the first source document
        PdfDocument srcDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(renameFieldsIn(DATASHEET, 1))));

        srcDoc.copyPagesTo(1, 1, pdfDoc, formCopier);
        srcDoc.close();

        // Create the second source document
        srcDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(renameFieldsIn(DATASHEET, 2))));

        srcDoc.copyPagesTo(1, 1, pdfDoc, formCopier);
        srcDoc.close();

        pdfDoc.close();
    }
}
