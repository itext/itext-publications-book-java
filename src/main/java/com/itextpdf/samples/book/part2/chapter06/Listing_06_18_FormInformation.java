/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.forms.PdfAcroForm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class Listing_06_18_FormInformation {
    public static final String DATASHEET
            = "./src/main/resources/pdfs/datasheet.pdf";
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_18_FormInformation.txt";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_18_FormInformation().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        // Create a writer for the report file
        PrintWriter writer = new PrintWriter(new FileOutputStream(dest));
        // Create a reader to extract info
        PdfReader reader = new PdfReader(DATASHEET);
        PdfDocument pdfDoc = new PdfDocument(reader);
        // Get the fields from the reader (read-only!!!)
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
        // Loop over the fields and get info about them
        Set<String> fields = form.getAllFormFields().keySet();
        for (String key : fields) {
            writer.print(key + ": ");
            PdfName type = form.getField(key).getFormType();
            if (0 == PdfName.Btn.compareTo(type)) {
                writer.println("Button");
            } else if (0 == PdfName.Ch.compareTo(type)) {
                writer.println("Choicebox");
            } else if (0 == PdfName.Sig.compareTo(type)) {
                writer.println("Signature");
            } else if (0 == PdfName.Tx.compareTo(type)) {
                writer.println("Text");
            }else {
                writer.println("?");
            }
        }
        // Get possible values for field "CP_1"
        writer.println("Possible values for CP_1:");
        String[] states = form.getField("CP_1").getAppearanceStates();
        for (int i = 0; i < states.length; i++) {
            writer.print(" - ");
            writer.println(states[i]);
        }
        // Get possible values for field "category"
        writer.println("Possible values for category:");
        states = form.getField("category").getAppearanceStates();
        for (int i = 0; i < states.length - 1; i++) {
            writer.print(states[i]);
            writer.print(", ");
        }
         writer.println(states[states.length - 1]);

        // flush and close the report file
        writer.flush();
        writer.close();
        reader.close();
    }
}
