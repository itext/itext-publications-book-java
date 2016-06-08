/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_02_25_MoviePosters1 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_25_MoviePosters1.pdf";
    public static final String RESOURCE = "./src/test/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_25_MoviePosters1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        Rectangle rect = new Rectangle(0, 806, 36, 842);

        new PdfCanvas(pdfDoc.addNewPage())
                .saveState()
                .setStrokeColor(Color.RED)
                .setFillColor(Color.RED)
                .rectangle(rect)
                .fillStroke()
                .restoreState();
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            doc.add(new Paragraph(movie.getMovieTitle()));
            // Add an image
            doc.add(new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb()))));
        }

        doc.close();
    }
}
