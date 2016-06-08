/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter12;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Assert;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Category(SampleTest.class)
public class Listing_12_04_MetadataXmp extends GenericTest {
    public static final String METADATA_PDF
            = "./src/test/resources/book/part3/chapter12/cmp_Listing_12_01_MetadataPdf.pdf";
    public static final String[] RESULTS = {
            "./target/test/resources/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp_metadata.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp_metadata_automatic.pdf",
            "./target/test/resources/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp_metadata_added.pdf"
    };
    public static final String[] CMP_RESULTS = {
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_04_MetadataXmp_xmp_metadata.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_04_MetadataXmp_xmp_metadata_automatic.pdf",
            "./src/test/resources/book/part3/chapter12/cmp_Listing_12_04_MetadataXmp_xmp_metadata_added.pdf"
    };
    public static final String RESULT_XML = "./target/test/resources/book/part3/chapter12/Listing_12_04_MetadataXmp_xmp.xml";
    public static final String DEST = RESULTS[0];

    public static void main(String args[]) throws IOException, XMPException {
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
        createPdf(RESULTS[0]);
        createPdfAutomatic(RESULTS[1]);
        manipulatePdf(METADATA_PDF, RESULTS[2]);
        readXmpMetadata(RESULTS[2], RESULT_XML);
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        String outPath;
        for (int i = 0; i < RESULTS.length; i++) {
            outPath = new File(RESULTS[i]).getParent();
            if (compareXml) {
                if (!compareTool.compareXmls(RESULTS[i], CMP_RESULTS[i])) {
                    addError("The XML structures are different.");
                }
            } else {
                if (compareRenders) {
                    addError(compareTool.compareVisually(RESULTS[i], CMP_RESULTS[i], outPath, differenceImagePrefix));
                    addError(compareTool.compareLinkAnnotations(dest, cmp));
                } else {
                    addError(compareTool.compareByContent(RESULTS[i], CMP_RESULTS[i], outPath, differenceImagePrefix));
                }
                addError(compareTool.compareDocumentInfo(RESULTS[i], CMP_RESULTS[i]));
            }
        }
        if (errorMessage != null) Assert.fail(errorMessage);
    }
}
