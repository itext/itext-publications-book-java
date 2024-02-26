package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Listing_12_09_EncryptionPdf {
    public static byte[] USER = "Hello".getBytes();
    public static byte[] OWNER = "World".getBytes();
    public static final String[] RESULT = {
            "./target/book/part3/chapter12/Listing_12_09_EncryptionPdf_encryption.pdf",
            "./target/book/part3/chapter12/Listing_12_09_EncryptionPdf_encryption_decrypted.pdf",
            "./target/book/part3/chapter12/Listing_12_09_EncryptionPdf_encryption_encrypted.pdf"
    };

    public static final String DEST = RESULT[0];

    public static void main(String args[]) throws IOException {
        File file = new File(RESULT[0]);
        file.getParentFile().mkdirs();

        new Listing_12_09_EncryptionPdf().manipulatePdf(RESULT[0]);
    }

    public void manipulatePdf(String dest) throws IOException {
        createPdf(RESULT[0]);
        decryptPdf(RESULT[0], RESULT[1]);
        encryptPdf(RESULT[1], RESULT[2]);
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
}
