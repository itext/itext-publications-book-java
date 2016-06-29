/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

// Notice that this example actually is not from book
@Category(SampleTest.class)
public class Listing_06_12_StampText_Option2 extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part2/chapter06/Listing_06_12_StampText_Option2.pdf";
    public static final String SOURCE = "./src/test/resources/pdfs/source.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_06_12_StampText_Option2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize reader
        PdfReader reader = new PdfReader(SOURCE);

        //Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        Document doc = new Document(pdfDoc);

        //Add paragraph to a fixed position
        Paragraph p = new Paragraph("Hello people!").setFixedPosition(7, 36, 540, 300);
        doc.add(p);

        //close document
        doc.close();
    }
}
