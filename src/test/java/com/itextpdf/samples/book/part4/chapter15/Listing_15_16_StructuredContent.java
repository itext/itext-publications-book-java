/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Category(SampleTest.class)
public class Listing_15_16_StructuredContent extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter15/Listing_15_16_StructuredContent.pdf";
    public static String RESOURCE
            = "./src/test/resources/xml/moby.xml";

    public static void main(String args[]) throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_15_16_StructuredContent().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException, ParserConfigurationException, SAXException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        pdfDoc.setDefaultPageSize(PageSize.A5);
        pdfDoc.setTagged();

        PdfStructTreeRoot root = pdfDoc.getStructTreeRoot();
        root.getRoleMap().put(new PdfName("chapter"), PdfName.Sect);
        root.getRoleMap().put(new PdfName("title"), PdfName.H);
        root.getRoleMap().put(new PdfName("para"), PdfName.P);

        // See TaggingSamples for more information
        TagTreePointer autoTaggingPointer = pdfDoc.getTagStructureContext().getAutoTaggingPointer();
        // create a new tag, which will be a kid of the root element, and move to it
        autoTaggingPointer.addTag(new PdfName("chapter"));

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        List<PdfName> roles = new ArrayList<PdfName>();
        parser.parse(
                new InputSource(new FileInputStream(RESOURCE)),
                new Listing_15_17_StructureParser(roles));
        parser.parse(
                new InputSource(new FileInputStream(RESOURCE)),
                new Listing_15_18_ContentParser(doc, roles));
        doc.close();
    }
}
