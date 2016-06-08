/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_07_20_GenericAnnotations extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter07/Listing_07_20_GenericAnnotations.pdf";
    /**
     * Possible icons.
     */
    public static final String[] ICONS = {
            "Comment", "Key", "Note", "Help", "NewParagraph", "Paragraph", "Insert"
    };

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_07_20_GenericAnnotations().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph();
        Text text;
        for (int i = 0; i < ICONS.length; i++) {
            text = new Text(ICONS[i]);
            text.setNextRenderer(new AnnotatedTextRenderer(text));
            p.add(text);
            p.add(new Text("           "));
        }
        doc.add(p);
        // step 5
        doc.close();
    }


    private class AnnotatedTextRenderer extends TextRenderer {
        protected String text;

        public AnnotatedTextRenderer(Text textElement) {
            super(textElement);
            text = textElement.getText();
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);

            Rectangle rect = getOccupiedAreaBBox();
            PdfAnnotation annotation = new PdfTextAnnotation(
                    new Rectangle(
                            rect.getRight() + 10, rect.getBottom(),
                            rect.getWidth() + 20, rect.getHeight()));
            annotation.setTitle(new PdfString("Text annotation"));
            annotation.put(PdfName.Subtype, PdfName.Text);
            annotation.put(PdfName.Open, PdfBoolean.FALSE);
            annotation.put(PdfName.Contents,
                    new PdfString(String.format("Icon: %s", text)));
            annotation.put(PdfName.Name, new PdfName(text));
            drawContext.getDocument().getLastPage().addAnnotation(annotation);
        }
    }
}
