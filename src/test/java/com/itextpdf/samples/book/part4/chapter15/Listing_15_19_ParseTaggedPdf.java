/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.utils.TaggedPdfReaderTool;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

@Category(SampleTest.class)
public class Listing_15_19_ParseTaggedPdf {
    public static final String RESULT
            = "./target/test/resources/book/part4/chapter15/Listing_15_19_ParseTaggedPdf.xml";
    public static final String CMP_RESULT
            = "./src/test/resources/book/part4/chapter15/cmp_Listing_15_19_ParseTaggedPdf.xml";
    public static final String STRUCTURED_CONTENT
            = "./src/test/resources/book/part4/chapter15/cmp_Listing_15_16_StructuredContent.pdf";

    public static void main(String args[]) throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_15_19_ParseTaggedPdf().manipulatePdf();
    }

    @Test
    public void manipulatePdf() throws IOException, SQLException, ParserConfigurationException, SAXException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(STRUCTURED_CONTENT));
        TaggedPdfReaderTool readertool = new TaggedPdfReaderTool(pdfDoc);
        readertool.convertToXml(new FileOutputStream(RESULT));
        pdfDoc.close();
        compareXML(RESULT, CMP_RESULT);
    }

    protected void compareXML(String dest, String cmp) throws IOException {
        BufferedReader destReader;
        BufferedReader cmpReader;
        String curDestStr;
        String curCmpStr;
            destReader = new BufferedReader(new InputStreamReader(new FileInputStream(RESULT)));
            cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(CMP_RESULT)));
            int row = 1;
            while ((curDestStr = destReader.readLine()) != null) {
                if ((curCmpStr = cmpReader.readLine()) == null) {
                    Assert.fail("The lengths of files are different.");
                }
                if (!curCmpStr.equals(curDestStr)) {
                    Assert.fail("The files are different on the row " + row );
                }
                row++;
            }
            if ((curCmpStr = cmpReader.readLine()) != null) {
                Assert.fail("The lengths of files are different.");
            }
    }
}
