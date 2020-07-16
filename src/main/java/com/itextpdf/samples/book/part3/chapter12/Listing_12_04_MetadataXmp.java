package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Listing_12_04_MetadataXmp {
    public static final String METADATA_PDF
            = "./src/main/resources/pdfs/cmp_Listing_12_01_MetadataPdf.pdf";
    public static final String[] RESULT = {
            "./target/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp_metadata.pdf",
            "./target/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp_metadata_automatic.pdf",
            "./target/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp_metadata_added.pdf"
    };

    public static final String RESULT_XML = "./target/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp.xml";
    public static final String DEST = RESULT[0];

    public static void main(String args[]) throws IOException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_12_04_MetadataXmp().manipulatePdf(DEST);
    }

    public void createPdf(String dest) throws IOException, XMPException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        XMPMeta xmp = XMPMetaFactory.create();
        xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Subject, new PropertyOptions(PropertyOptions.ARRAY), "Hello World", null);
        xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Subject, "XMP & Metadata");
        xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Subject, "Metadata");
        xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Keywords, new PropertyOptions(PropertyOptions.ARRAY), "Hello World, XMP, Metadata", null);
        xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Version, new PropertyOptions(PropertyOptions.ARRAY), "1.4", null);
        pdfDoc.setXmpMetadata(xmp);

        doc.add(new Paragraph("Hello World"));

        doc.close();
    }

    public void createPdfAutomatic(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info
                .setTitle("Hello World example")
                .setSubject("This example shows how to add metadata & XMP")
                .setKeywords("Metadata, iText, step 3")
                .setCreator("My program using 'iText'")
                .setAuthor("Bruno Lowagie & Paulo Soares");

        doc.add(new Paragraph("Hello World"));
        doc.close();
    }

    public void manipulatePdf(String src, String dest) throws IOException, XMPException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest, new WriterProperties().addXmpMetadata()));
        pdfDoc.close();
    }

    /**
     * Reads the XML stream inside a PDF file into an XML file.
     *
     * @param src  A PDF file containing XMP data
     * @param dest XML file containing the XMP data extracted from the PDF
     * @throws IOException
     */
    public void readXmpMetadata(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] b = pdfDoc.getXmpMetadata();
        fos.write(b, 0, b.length);
        fos.flush();
        fos.close();
        pdfDoc.close();
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        createPdf(RESULT[0]);
        createPdfAutomatic(RESULT[1]);
        manipulatePdf(METADATA_PDF, RESULT[2]);
        readXmpMetadata(RESULT[2], RESULT_XML);
    }
}
