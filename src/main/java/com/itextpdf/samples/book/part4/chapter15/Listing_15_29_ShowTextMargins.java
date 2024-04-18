package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextMarginFinder;

import java.io.File;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_15_29_ShowTextMargins {
    public static final String DEST
            = "./target/book/part4/chapter15/Listing_15_29_ShowTextMargins.pdf";
    public static final String PREFACE
            = "./src/main/resources/pdfs/preface.pdf";

    public static void main(String args[])
            throws IOException, SQLException, ParserConfigurationException, SAXException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_15_29_ShowTextMargins().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest)
            throws IOException, SQLException, ParserConfigurationException, SAXException {
        addMarginRectangle(PREFACE, dest);
    }

    /**
     * Parses a PDF and ads a rectangle showing the text margin.
     *
     * @param src  the source PDF
     * @param dest the resulting PDF
     */
    public void addMarginRectangle(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        TextMarginFinder finder;
        PdfCanvasProcessor parser;
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            finder  = new TextMarginFinder();
            parser = new PdfCanvasProcessor(finder);
            parser.processPageContent(pdfDoc.getPage(i));
            PdfCanvas cb = new PdfCanvas(pdfDoc.getPage(i));
            cb.rectangle(finder.getTextRectangle().getLeft(), finder.getTextRectangle().getBottom(),
                    finder.getTextRectangle().getWidth(), finder.getTextRectangle().getHeight());
            cb.stroke();
        }
        pdfDoc.close();
    }
}
