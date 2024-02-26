package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.utils.TaggedPdfReaderTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;

import org.xml.sax.SAXException;

public class Listing_15_19_ParseTaggedPdf {
    public static final String DEST
            = "./target/book/part4/chapter15/Listing_15_19_ParseTaggedPdf.xml";

    public static final String STRUCTURED_CONTENT
            = "./src/main/resources/pdfs/cmp_Listing_15_16_StructuredContent.pdf";

    public static void main(String args[]) throws IOException, SQLException, ParserConfigurationException, SAXException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_15_19_ParseTaggedPdf().manipulatePdf();
    }

    public void manipulatePdf() throws IOException, SQLException, ParserConfigurationException, SAXException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(STRUCTURED_CONTENT));
        TaggedPdfReaderTool readertool = new TaggedPdfReaderTool(pdfDoc);
        readertool.convertToXml(new FileOutputStream(DEST));
        pdfDoc.close();
    }
}
