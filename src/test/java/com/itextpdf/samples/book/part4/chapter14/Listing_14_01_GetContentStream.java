/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_14_01_GetContentStream extends GenericTest {
    public static final String RESULT1
            = "./target/test/resources/book/part4/chapter14/Listing_14_01_GetContentStream1.txt";
    public static final String RESULT2
            = "./target/test/resources/book/part4/chapter14/Listing_14_01_GetContentStream2.txt";
    public static final String CMP_RESULT1
            = "./src/test/resources/book/part4/chapter14/cmp_Listing_14_01_GetContentStream1.txt";
    public static final String CMP_RESULT2
            = "./src/test/resources/book/part4/chapter14/cmp_Listing_14_01_GetContentStream2.txt";
    public static final String HELLO_WORLD
            = "./src/test/resources/book/part1/chapter01/cmp_Listing_01_01_HelloWorld.pdf";
    public static final String HERO
            = "./src/test/resources/book/part1/chapter05/cmp_Listing_05_15_Hero1.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_14_01_GetContentStream().manipulatePdf(RESULT1);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException {
        new File(dest).getParentFile().mkdirs();
        readContent(HELLO_WORLD, RESULT1);
        readContent(HERO, RESULT2);
    }

    public void readContent(String src, String result) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        FileOutputStream out = new FileOutputStream(result);
        out.write(pdfDoc.getFirstPage().getContentBytes());
        out.flush();
        out.close();
        pdfDoc.close();
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        // For the first content stream
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
                addError("The files are different on the row " + row);
            }
            row++;
        }
        if ((curCmpStr = cmpReader.readLine()) != null) {
            addError("The lengths of files are different.");
        }

        // For the second content stream
        destReader = new BufferedReader(new InputStreamReader(new FileInputStream(RESULT2)));
        cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(CMP_RESULT2)));
        row = 1;
        while ((curDestStr = destReader.readLine()) != null) {
            if ((curCmpStr = cmpReader.readLine()) != null) {
                addError("The lengths of files are different.");
            }
            if (!curCmpStr.equals(curDestStr)) {
                addError("The files are different on the row " + row);
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
        return RESULT1;
    }

    @Override
    protected String getCmpPdf() {
        // dummy
        return CMP_RESULT1;
    }
}
