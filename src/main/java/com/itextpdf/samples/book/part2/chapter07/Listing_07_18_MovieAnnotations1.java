package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_07_18_MovieAnnotations1 {
    public static final String DEST
            = "./target/book/part2/chapter07/Listing_07_18_MovieAnnotations1.pdf";
    /**
     * Pattern for an info String.
     */
    public static final String INFO = "Movie produced in %s; run length: %s";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_07_18_MovieAnnotations1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Paragraph p;
        for (Movie movie : PojoFactory.getMovies(connection)) {
            p = new Paragraph(movie.getMovieTitle());
            p.setNextRenderer(new AnnotatedParagraphRenderer(p, movie.getTitle(),
                    String.format(INFO, movie.getYear(), movie.getDuration())));
            doc.add(p);
            doc.add(PojoToElementFactory.getDirectorList(movie));
            doc.add(PojoToElementFactory.getCountryList(movie));
        }
        doc.close();
        connection.close();
    }


    private class AnnotatedParagraphRenderer extends ParagraphRenderer {
        private String text;
        private String contents;

        public AnnotatedParagraphRenderer(Paragraph modelElement, String text, String contents) {
            super(modelElement);
            this.text = text;
            this.contents = contents;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new AnnotatedParagraphRenderer((Paragraph) modelElement, text, contents);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = new Rectangle(getOccupiedAreaBBox().getLeft() + getOccupiedAreaBBox().getWidth() / 4,
                    getOccupiedAreaBBox().getBottom(), 10, 10);
            PdfAnnotation textAnnot = new PdfTextAnnotation(rect)
                    .setText(new PdfString(text))
                    .setContents(new PdfString(contents));
            drawContext.getDocument().getLastPage().addAnnotation(textAnnot);
        }
    }
}
