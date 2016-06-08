/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_07_22_MoviePosters1 extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter07/Listing_07_22_MoviePosters1.pdf";
    /**
     * Pattern for an info String.
     */
    public static final String INFO = "Movie produced in %s; run length: %s";
    /**
     * Path to IMDB.
     */
    public static final String IMDB
            = "http://imdb.com/title/tt%s/";
    public static final String RESOURCE = "./src/test/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_07_22_MoviePosters1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));
        // Add the movie posters
        Image img;
        PdfAnnotation linkAnnotation;
        // Annotation annotation;
        float x = 11.5f;
        float y = 769.7f;
        PdfFormXObject xObject = new PdfFormXObject(new Rectangle(595, 84.2f));
        PdfCanvas xObjectCanvas = new PdfCanvas(xObject, pdfDoc);
        xObjectCanvas.rectangle(8, 8, 571, 60);
        for (float f = 8.25f; f < 581; f += 6.5f) {
            xObjectCanvas.roundRectangle(f, 8.5f, 6, 3, 1.5f);
            xObjectCanvas.roundRectangle(f, 72.5f, 6, 3, 1.5f);
        }
        xObjectCanvas.setFillColorGray(0.1f);
        xObjectCanvas.eoFill();

        PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.addNewPage());
        for (int i = 0; i < 10; i++) {
            pdfCanvas.addXObject(xObject, 0, i * 84.2f);
        }
        Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pdfDoc.getLastPage().getPageSize());
        PdfArray border = new PdfArray(new float[]{0, 0, 0});
        for (Movie movie : PojoFactory.getMovies(connection)) {
            img = new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb())));
            img.scaleToFit(1000, 60);
            img.setFixedPosition(x + (45 - img.getImageScaledWidth()) / 2, y);
            linkAnnotation = new PdfLinkAnnotation(new Rectangle(x + (45 - img.getImageScaledWidth()) / 2, y,
                    img.getImageScaledWidth(), img.getImageScaledHeight())).setBorder(border);
            linkAnnotation.setAction(PdfAction.createURI(String.format(IMDB, movie.getImdb())));
            pdfDoc.getLastPage().addAnnotation(linkAnnotation);
            canvas.add(img);
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        doc.close();
        // Close the database connection
        connection.close();
    }
}
