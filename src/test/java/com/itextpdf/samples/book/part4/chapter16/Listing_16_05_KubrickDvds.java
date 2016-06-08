/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfFileAttachmentAnnotation;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.ListItemRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

@Category(SampleTest.class)
public class Listing_16_05_KubrickDvds extends GenericTest {
    public static final String FILENAME = "Listing_16_05_KubrickDvds.pdf";
    public static final String DEST = "./target/test/resources/book/part4/chapter16/" + FILENAME;
    public static final String RESOURCE
            = "./src/test/resources/img/posters/%s.jpg";
    public static final String PATH = "./target/test/resources/book/part4/chapter16/%s";

    public static void main(String args[]) throws Exception {
        new Listing_16_05_KubrickDvds().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        FileOutputStream os = new FileOutputStream(dest);
        os.write(createPdf());
        os.flush();
        os.close();
        extractAttachments(dest);
    }

    public void extractAttachments(String src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfReader reader = new PdfReader(src);
        PdfArray array;
        PdfDictionary annot;
        PdfDictionary fs;
        PdfDictionary refs;
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            array = pdfDoc.getPage(i).getPdfObject().getAsArray(PdfName.Annots);
            if (array == null) continue;
            for (int j = 0; j < array.size(); j++) {
                annot = array.getAsDictionary(j);
                if (PdfName.FileAttachment.equals(annot.getAsName(PdfName.Subtype))) {
                    fs = annot.getAsDictionary(PdfName.FS);
                    refs = fs.getAsDictionary(PdfName.EF);
                    for (PdfName name : refs.keySet()) {
                        FileOutputStream fos
                                = new FileOutputStream(String.format(PATH, fs.getAsString(name).toString()));
                        fos.write(refs.getAsStream(name).getBytes());
                        fos.flush();
                        fos.close();
                    }
                }
            }
        }
        reader.close();
    }

    public byte[] createPdf() throws IOException, SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("This is a list of Kubrick movies available in DVD stores."));
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> movies = new TreeSet<Movie>();
        movies.addAll(PojoFactory.getMovies(connection, 1));
        movies.addAll(PojoFactory.getMovies(connection, 4));
        ListItem item;
        Text text;
        List list = new List();
        for (Movie movie : movies) {
            item = new ListItem(movie.getMovieTitle(false));
            item.setNextRenderer(new AnnotatedListItemRenderer(item,
                    String.format(RESOURCE, movie.getImdb()),
                    String.format("%s.jpg", movie.getImdb()),
                    movie.getMovieTitle(false)));
            list.add(item);
        }
        doc.add(list);

        doc.close();
        connection.close();

        return baos.toByteArray();
    }


    private class AnnotatedListItemRenderer extends ListItemRenderer {
        private String fileDisplay;
        private String filePath;
        private String fileTitle;

        public AnnotatedListItemRenderer(ListItem modelElement, String path, String display, String title) {
            super(modelElement);
            fileDisplay = display;
            filePath = path;
            fileTitle = title;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfFileSpec fs = null;
            try {
                fs = PdfFileSpec.createEmbeddedFileSpec(drawContext.getDocument(),
                        filePath, null, fileDisplay, null, null, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Rectangle rect = new Rectangle(getOccupiedAreaBBox().getRight(),
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
