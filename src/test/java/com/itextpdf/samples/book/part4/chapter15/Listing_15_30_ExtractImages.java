/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;


@Category(SampleTest.class)
public class Listing_15_30_ExtractImages {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter15/Img%s.%s";
    public static final String IMAGE_TYPES
            = "./src/test/resources/book/part3/chapter10/cmp_Listing_10_09_ImageTypes.pdf";

    @Test
    public void manipulatePdf() throws IOException, SQLException, ParserConfigurationException, SAXException {
        new File(DEST).getParentFile().mkdirs();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(IMAGE_TYPES), new PdfWriter(new ByteArrayOutputStream()));
        IEventListener listener = new Listing_15_31_MyImageRenderListener(DEST);
        PdfCanvasProcessor parser = new PdfCanvasProcessor(listener);
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            parser.processPageContent(pdfDoc.getPage(i));
        }
        pdfDoc.close();
    }
}
