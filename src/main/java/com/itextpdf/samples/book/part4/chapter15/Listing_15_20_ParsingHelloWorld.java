package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.constants.StandardFonts;
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

import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import org.xml.sax.SAXException;

public class Listing_15_20_ParsingHelloWorld {
    public static final String DEST =
            "./target/book/part4/chapter15/Listing_15_20_ParsingHelloWorld.pdf";
    public static final String[] TEXT = {
            "./target/book/part4/chapter15/Listing_15_20_ParsingHelloWorld1.txt",
            "./target/book/part4/chapter15/Listing_15_20_ParsingHelloWorld2.txt",
            "./target/book/part4/chapter15/Listing_15_20_ParsingHelloWorld3.txt"
    };

    public static final String HELLO_WORLD =
            "./src/main/resources/pdfs/cmp_Listing_01_01_HelloWorld.pdf";

    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.beginText();
        canvas.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 12);
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
        xObjectCanvas.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 12);
        xObjectCanvas.moveText(0, 7);
        xObjectCanvas.showText("Hello People");
        xObjectCanvas.endText();

        canvas.addXObjectAt(xObject, 36, 343);
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
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_15_20_ParsingHelloWorld().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException, ParserConfigurationException, SAXException {
        createPdf(DEST);
        parsePdf(HELLO_WORLD, TEXT[0]);
        parsePdf(DEST, TEXT[1]);
        extractText(DEST, TEXT[2]);
    }
}
