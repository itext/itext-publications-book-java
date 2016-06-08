/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

@Category(SampleTest.class)
public class Listing_15_27_ExtractPageContentSorted2 {
    public static final String DEST
            = "./samples/target/test/resources/book/part4/chapter15/Listing_15_27_ExtractPageContentSorted2.txt";
    public static final String PREFACE
            = "./samples/src/test/resources/pdfs/preface.pdf";

    public static void main(String args[])
            throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_15_27_ExtractPageContentSorted2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest)
            throws IOException, SQLException, ParserConfigurationException, SAXException {
        parsePdf(PREFACE, dest);
    }

    public void parsePdf(String src, String txt) throws IOException {
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(new ByteArrayOutputStream()));
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            out.println(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i), new LocationTextExtractionStrategy()));
        }
        out.flush();
        out.close();
        pdfDoc.close();
    }
}
