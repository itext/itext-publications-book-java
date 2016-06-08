/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

@Category(SampleTest.class)
public class Listing_15_20_ParsingHelloWorld extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part4/chapter15/Listing_15_20_ParsingHelloWorld.pdf";
    public static final String[] TEXT = {
            "./target/test/resources/book/part4/chapter15/Listing_15_20_ParsingHelloWorld1.txt",
            "./target/test/resources/book/part4/chapter15/Listing_15_20_ParsingHelloWorld2.txt",
            "./target/test/resources/book/part4/chapter15/Listing_15_20_ParsingHelloWorld3.txt"
    };
    public static final String[] CMP_TEXT = {
            "./src/test/resources/book/part4/chapter15/cmp_Listing_15_20_ParsingHelloWorld1.txt",
            "./src/test/resources/book/part4/chapter15/cmp_Listing_15_20_ParsingHelloWorld2.txt",
            "./src/test/resources/book/part4/chapter15/cmp_Listing_15_20_ParsingHelloWorld3.txt"
    };
    public static final String HELLO_WORLD =
            "./src/test/resources/book/part1/chapter01/cmp_Listing_01_01_HelloWorld.pdf";

    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.beginText();
        canvas.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12);
        canvas.moveText(88.66f, 367);
        canvas.showText("ld");
        canvas.moveText(-22f, 0);
        canvas.showText("Wor");
        canvas.moveText(-15.33f, 0);
        canvas.showText("llo");
        canvas.moveText(-15.33f, 0);
        canvas.showText("He");
        canvas.endText();
        // we also add text in a form XObject
        PdfFormXObject xObject = new PdfFormXObject(new Rectangle(250, 25));
        PdfCanvas xObjectCanvas = new PdfCanvas(xObject, pdfDoc);
        xObjectCanvas.beginText();
        xObjectCanvas.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12);
        xObjectCanvas.moveText(0, 7);
        xObjectCanvas.showText("Hello People");
        xObjectCanvas.endText();

        canvas.addXObject(xObject, 36, 343);
        pdfDoc.close();
    }

    public void parsePdf(String src, String dest) throws IOException {
        PdfReader reader = new PdfReader(src);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        // we can inspect the syntax of the imported page
        byte[] streamBytes = pdfDoc.getFirstPage().getContentBytes();

        PdfTokenizer tokenizer = new PdfTokenizer(new RandomAccessFileOrArray(
                new RandomAccessSourceFactory().createSource(streamBytes)));
        PrintWriter out = new PrintWriter(new FileOutputStream(dest));
        while (tokenizer.nextToken()) {
            if (tokenizer.getTokenType() == PdfTokenizer.TokenType.String) {
                out.println(tokenizer.getStringValue());
            }
        }
        out.flush();
        out.close();
        reader.close();
    }

    public void extractText(String src, String dest) throws IOException {
        PrintWriter out = new PrintWriter(new FileOutputStream(dest));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        IEventListener listener = new Listing_15_24_MyTextRenderListener(out);
        PdfCanvasProcessor processor = new PdfCanvasProcessor(listener);
        processor.processPageContent(pdfDoc.getFirstPage());
        out.flush();
        out.close();
        pdfDoc.close();
    }
    public static void main(String args[]) throws IOException, SQLException, ParserConfigurationException, SAXException {
        new Listing_15_20_ParsingHelloWorld().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException, ParserConfigurationException, SAXException {
        createPdf(DEST);
        parsePdf(HELLO_WORLD, TEXT[0]);
        parsePdf(DEST, TEXT[1]);
        extractText(DEST, TEXT[2]);
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        BufferedReader destReader;
        BufferedReader cmpReader;
        String curDestStr;
        String curCmpStr;
        for (int i = 0; i < TEXT.length; i++) {
            destReader = new BufferedReader(new InputStreamReader(new FileInputStream(TEXT[i])));
            cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(CMP_TEXT[i])));
            int row = 1;
            while ((curDestStr = destReader.readLine()) != null) {
                if ((curCmpStr = cmpReader.readLine()) == null) {
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
    }
}
