/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfFileAttachmentAnnotation;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_07_24_MovieAnnotations3 extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter07/Listing_07_24_MovieAnnotations3.pdf";
    /**
     * Pattern for an info String.
     */
    public static final String RESOURCE = "./src/test/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_07_24_MovieAnnotations3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        Paragraph paragraph;
        Text text;
        for (Movie movie : PojoFactory.getMovies(connection)) {
            paragraph = new Paragraph(movie.getMovieTitle());
            text = new Text("\u00a0\u00a0");
            text.setNextRenderer(new AnnotatedTextRenderer(text, String.format(RESOURCE, movie.getImdb()),
                    String.format("img_%s.jpg", movie.getImdb()), movie.getMovieTitle()));
            paragraph.add(text);
            doc.add(paragraph);
            doc.add(PojoToElementFactory.getDirectorList(movie));
            doc.add(PojoToElementFactory.getCountryList(movie));
        }
        doc.close();
        // Close the database connection
        connection.close();
    }


    private class AnnotatedTextRenderer extends TextRenderer {
        private String fileDisplay;
        private String filePath;
        private String fileTitle;

        public AnnotatedTextRenderer(Text textElement, String path, String display, String title) {
            super(textElement);
            fileDisplay = display;
            filePath = path;
            fileTitle = title;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfFileSpec fs = null;
            try {
                fs = PdfFileSpec.createEmbeddedFileSpec(drawContext.getDocument(), filePath, null, fileDisplay, null, null, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Rectangle rect = new Rectangle(getOccupiedAreaBBox().getLeft() + getOccupiedAreaBBox().getWidth() / 4,
                    getOccupiedAreaBBox().getBottom(), 10, 10);
            PdfFileAttachmentAnnotation annotation =
                    new PdfFileAttachmentAnnotation(rect, fs);
            annotation.setIconName(PdfName.Paperclip);
            annotation.setContents(fileTitle);
            annotation.put(PdfName.Name, new PdfString("Paperclip"));
            drawContext.getDocument().getLastPage().addAnnotation(annotation);
        }
    }
}
