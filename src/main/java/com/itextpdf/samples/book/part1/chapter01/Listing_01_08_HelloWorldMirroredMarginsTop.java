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
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class Listing_01_08_HelloWorldMirroredMarginsTop {
    public static final String DEST =
            "./target/book/part1/chapter01/Listing_01_08_HelloWorldMirroredMarginsTop.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_01_08_HelloWorldMirroredMarginsTop().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        PageSize customPageSize = (PageSize) PageSize.A5.clone();
        Document doc = new Document(pdfDoc, customPageSize);
        doc.setMargins(108, 72, 180, 36);
        doc.setRenderer(new DocumentRenderer(doc) {
            int currentPageNumber = 0;

            @Override
            public LayoutArea updateCurrentArea(LayoutResult overflowResult) {
                currentPageNumber = super.updateCurrentArea(overflowResult).getPageNumber();
                if (currentPageNumber % 2 == 0) {
                    document.setMargins(108, 72, 180, 36);
                } else {
                    document.setMargins(180, 72, 108, 36);
                }
                return currentArea;
            }

            // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
            // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
            // renderer will be created
            @Override
            public IRenderer getNextRenderer() {
                return new DocumentRenderer(document);
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
                "The top margin 180pt (2.5 inch)."));

        //Close document
        doc.close();
    }
}
