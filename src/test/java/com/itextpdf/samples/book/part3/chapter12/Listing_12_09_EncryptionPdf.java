/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_12_09_EncryptionPdf extends GenericTest {
    public static byte[] USER = "Hello".getBytes();
    public static byte[] OWNER = "World".getBytes();
    public static final String RESULT1
            = "./target/test/resources/book/part3/chapter12/Listing_12_09_EncryptionPdf_encryption.pdf";
    public static final String RESULT2
            = "./target/test/resources/book/part3/chapter12/Listing_12_09_EncryptionPdf_encryption_decrypted.pdf";
    public static final String RESULT3
            = "./target/test/resources/book/part3/chapter12/Listing_12_09_EncryptionPdf_encryption_encrypted.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_12_09_EncryptionPdf().manipulatePdf(RESULT1);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(RESULT1);
        decryptPdf(RESULT1, RESULT2);
        encryptPdf(RESULT2, RESULT3);
    }

    public void createPdf(String dest) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties()
                .setStandardEncryption(USER, OWNER, EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.STANDARD_ENCRYPTION_128));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Hello World"));
        doc.close();
    }

    public void decryptPdf(String src, String dest) throws IOException {
        PdfReader reader = new PdfReader(src, new ReaderProperties().setPassword(OWNER));
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        pdfDoc.close();
        reader.close();
    }

    public void encryptPdf(String src, String dest) throws IOException {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties()
                .setStandardEncryption(USER, OWNER,
                        EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), writer);
        pdfDoc.close();
        writer.close();
    }

    // Override the comparison method in order to pass owner passwords
    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        if (cmp == null || cmp.length() == 0) return;
        CompareTool compareTool = new CompareTool();
        String outPath = new File(dest).getParent();
        new File(outPath).mkdirs();
        if (compareXml) {
            if (!compareTool.compareXmls(dest, cmp)) {
                addError("The XML structures are different.");
            }
        } else {
            if (compareRenders) {
                addError(compareTool.compareVisually(dest, cmp, outPath, differenceImagePrefix));
                addError(compareTool.compareLinkAnnotations(dest, cmp));
            } else {
                addError(compareTool.compareByContent(dest, cmp, outPath, differenceImagePrefix, OWNER, OWNER));
            }
            addError(compareTool.compareDocumentInfo(dest, cmp, OWNER, OWNER));
        }

        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    // only for GenericTest running
    @Override
    protected String getDest() {
        File file = new File(RESULT1);
        file.getParentFile().mkdirs();
        return RESULT1;
    }
}
