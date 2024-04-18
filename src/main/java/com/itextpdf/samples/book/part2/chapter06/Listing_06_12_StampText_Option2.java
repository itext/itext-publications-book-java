package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

// Notice that this example actually is not from book
public class Listing_06_12_StampText_Option2 {

    public static final String DEST = "./target/book/part2/chapter06/Listing_06_12_StampText_Option2.pdf";
    public static final String SOURCE = "./src/main/resources/pdfs/source.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

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
