/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.ITextTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.experimental.categories.Category;

/**
 * Due to import control restrictions by the governments of a few countries,
 * the encryption libraries shipped by default with the Java SDK restrict the
 * length, and as a result the strength, of encryption keys. Be aware that in
 * this sample by using {@link ITextTest#removeCryptographyRestrictions()} we
 * remove cryptography restrictions via reflection for testing purposes.
 * <br/>
 * For more conventional way of solving this problem you need to replace the
 * default security JARs in your Java installation with the Java Cryptography
 * Extension (JCE) Unlimited Strength Jurisdiction Policy Files. These JARs
 * are available for download from http://java.oracle.com/ in eligible countries.
 */
@Category(SampleTest.class)
public class Listing_12_11_EncryptWithCertificate extends GenericTest {
    public static String RESULT1
            = "./target/test/resources/book/part3/chapter12/Listing_12_11_EncryptWithCertificate_certificate_encryption.pdf";
    public static String RESULT2
            = "./target/test/resources/book/part3/chapter12/Listing_12_11_EncryptWithCertificate_certificate_decrypted.pdf";
    public static String RESULT3
            = "./target/test/resources/book/part3/chapter12/Listing_12_11_EncryptWithCertificate_certificate_encrypted.pdf";
    /**
     * A properties file that is PRIVATE.
     * You should make your own properties file and adapt this line.
     */
    public static String PATH
            = "./src/test/resources/encryption/key.properties";
    // public static String PATH = "c:/users/ars18wrw/key.properties";

    public static Properties properties = new Properties();

    public static void main(String args[]) throws IOException, GeneralSecurityException {
        new Listing_12_11_EncryptWithCertificate().manipulatePdf(RESULT1);
    }

    public void manipulatePdf(String dest) throws IOException, GeneralSecurityException {
        new File(RESULT1).getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());
        properties.load(new FileInputStream(PATH));
        createPdf(RESULT1);
        decryptPdf(RESULT1, RESULT2);
        encryptPdf(RESULT2, RESULT3);
    }

    public void createPdf(String dest) throws IOException, CertificateException {
        Certificate cert1 = getPublicCertificate("./src/test/resources/encryption/foobar.cer");
        Certificate cert2 = getPublicCertificate(properties.getProperty("PUBLIC"));
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setPublicKeyEncryption(new Certificate[]{cert1, cert2},
                new int[]{EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ALLOW_COPY}, EncryptionConstants.ENCRYPTION_AES_128));
        Document doc = new Document(new PdfDocument(writer));
        doc.add(new Paragraph("Hello World!"));
        doc.close();
    }

    /**
     * Gets a public certificate from a certificate file.
     *
     * @param path the path to the certificate
     * @return a Certificate object
     * @throws IOException
     * @throws CertificateException
     */
    public Certificate getPublicCertificate(String path)
            throws IOException, CertificateException {
        FileInputStream is = new FileInputStream(path);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        return cert;
    }

    /**
     * Gets a private key from a KeyStore.
     *
     * @return a PrivateKey object
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public PrivateKey getPrivateKey() throws GeneralSecurityException, IOException {
        String path = "./src/test/resources/encryption/.keystore";
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(path), "f00b4r".toCharArray());
        PrivateKey pk = (PrivateKey) ks.getKey("foobar", "f1lmf3st".toCharArray());
        return pk;
    }

    /**
     * Decrypts a PDF that was encrypted using a certificate
     *
     * @param src  The encrypted PDF
     * @param dest The decrypted PDF
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void decryptPdf(String src, String dest)
            throws IOException, GeneralSecurityException {
        ReaderProperties readerProperties = new ReaderProperties()
                .setPublicKeySecurityParams(getPublicCertificate("./src/test/resources/encryption/foobar.cer"), getPrivateKey(), "BC", null);
        PdfReader reader = new PdfReader(new RandomAccessSourceFactory().createBestSource(src),
                readerProperties);
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        pdfDoc.close();
        reader.close();
    }

    /**
     * Encrypts a PDF using a public certificate.
     *
     * @param src  The original PDF document
     * @param dest The encrypted PDF document
     * @throws IOException
     * @throws CertificateException
     */
    public void encryptPdf(String src, String dest)
            throws IOException, CertificateException {
        PdfReader reader = new PdfReader(src);
        Certificate cert = getPublicCertificate("./src/test/resources/encryption/foobar.cer");
        PdfWriter writer = new PdfWriter(dest, new WriterProperties()
                .setPublicKeyEncryption(new Certificate[]{cert},
                        new int[]{EncryptionConstants.ALLOW_PRINTING}, EncryptionConstants.ENCRYPTION_AES_128));
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        pdfDoc.close();
        reader.close();
    }

    @Override
    protected String getDest() {
        return RESULT2;
    }

    @Override
    protected void beforeManipulatePdf() {
        super.beforeManipulatePdf();
        ITextTest.removeCryptographyRestrictions();
    }

    @Override
    protected void afterManipulatePdf() {
        super.afterManipulatePdf();
        ITextTest.restoreCryptographyRestrictions();
    }
}
