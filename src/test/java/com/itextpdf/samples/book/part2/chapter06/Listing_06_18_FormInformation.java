/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_06_18_FormInformation extends GenericTest {
    public static final String DATASHEET
            = "./src/test/resources/pdfs/datasheet.pdf";
    public static final String RESULT
            = "./target/test/resources/book/part2/chapter06/Listing_06_18_FormInformation.txt";
    public static final String CMP_RESULT
            = "./src/test/resources/book/part2/chapter06/cmp_Listing_06_18_FormInformation.txt";

    public static void main(String args[]) throws IOException {
        new Listing_06_18_FormInformation().manipulatePdf(RESULT);
    }

    public void manipulatePdf(String dest) throws IOException {
        // Create a writer for the report file
        PrintWriter writer = new PrintWriter(new FileOutputStream(RESULT));
        // Create a reader to extract info
        PdfReader reader = new PdfReader(DATASHEET);
        PdfDocument pdfDoc = new PdfDocument(reader);
        // Get the fields from the reader (read-only!!!)
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        // Loop over the fields and get info about them
        Set<String> fields = form.getFormFields().keySet();
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

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        //super.comparePdf(dest, cmp);
        BufferedReader destReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest)));
        BufferedReader cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(cmp)));
        String curDestStr;
        String curCmpStr;
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

    @Override
    protected String getDest() {
        // dummy
        return RESULT;
    }

    @Override
    protected String getCmpPdf() {
        // dummy
        return CMP_RESULT;
    }
}
