package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_07_20_GenericAnnotations {
    public static final String DEST = "./target/book/part2/chapter07/Listing_07_20_GenericAnnotations.pdf";
    /**
     * Possible icons.
     */
    public static final String[] ICONS = {
            "Comment", "Key", "Note", "Help", "NewParagraph", "Paragraph", "Insert"
    };

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

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

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new AnnotatedTextRenderer((Text) modelElement);
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
