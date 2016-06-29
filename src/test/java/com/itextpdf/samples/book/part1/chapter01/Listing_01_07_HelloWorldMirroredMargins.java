/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter01;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class Listing_01_07_HelloWorldMirroredMargins extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter01/Listing_01_07_HelloWorldMirroredMargins.pdf";


    public static void main(String[] args) throws Exception {
        new Listing_01_07_HelloWorldMirroredMargins().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        PageSize customPageSize = PageSize.A5.clone();

        Document doc = new Document(pdfDoc, customPageSize);
        doc.setMargins(108, 72, 180, 36);
        doc.setRenderer(new DocumentRenderer(doc) {
            int currentPageNumber = 0;
            @Override
            public LayoutArea updateCurrentArea(LayoutResult overflowResult) {
                    currentPageNumber = super.updateCurrentArea(overflowResult).getPageNumber();
                    if(currentPageNumber % 2 == 0){
                        document.setMargins(108, 72, 180, 36);
                    }else{
                        document.setMargins(108, 36, 180, 72);
                    }
                    return currentArea;
            }
        });
        doc.add(new Paragraph(
                "The left margin of this odd page is 36pt (0.5 inch); " +
                        "the right margin 72pt (1 inch); " +
                        "the top margin 108pt (1.5 inch); " +
                        "the bottom margin 180pt (2.5 inch)."));
        Paragraph paragraph = new Paragraph().setTextAlignment(TextAlignment.JUSTIFIED);
        for (int i = 0; i < 60; i++) {
            paragraph.add("Hello World! Hello People! " +
                    "Hello Sky! Hello Sun! Hello Moon! Hello Stars!");
        }
        doc.add(paragraph);
        doc.add(new Paragraph(
                "The right margin of this even page is 36pt (0.5 inch); " +
                        "the left margin 72pt (1 inch)."));

        //Close document
        doc.close();
    }
}
