/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

@Category(SampleTest.class)
public class Listing_03_26_MoviePosters extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_26_MoviePosters.pdf";

    public static final String RESOURCE = "src/test/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_03_26_MoviePosters().manipulatePdf(DEST);
    }

    public  void manipulatePdf(String dest) throws FileNotFoundException, UnsupportedEncodingException, SQLException, MalformedURLException {
        //Initialize document and add page
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfPage page = pdfDoc.addNewPage();

        //Initialize form XObject and write to it
        PdfFormXObject xObj = new PdfFormXObject(new Rectangle(8, 8, 579, 68));
        PdfCanvas celluloid = new PdfCanvas(xObj, pdfDoc);
        celluloid.rectangle(8, 8, 579, 68);
        for (float f = 8.25f; f < 581; f += 6.5f) {
            celluloid.roundRectangle(f, 8.5f, 6, 3, 1.5f).roundRectangle(f, 72.5f, 6, 3, 1.5f);
        }
        celluloid.setFillColor(new DeviceGray(0.1f)).eoFill();
        celluloid.release();

        //Flush xObj to output file
        xObj.flush();

        //Add XObjects to page canvas
        PdfCanvas canvas = new PdfCanvas(page);
        for (int i = 0; i < 10; i++) {
            canvas.addXObject(xObj, 0, i * 84.2f);
        }
        canvas.release();

        //Add XObjects to page canvas
        canvas = new PdfCanvas(page = pdfDoc.addNewPage());
        for (int i = 0; i < 10; i++) {
            canvas.addXObject(xObj, 0, i * 84.2f);
        }

        // Get the movies from the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);

        // Loop over the movies and add images
        float x = 11.5f;
        float y = 769.7f;
        for (Movie movie : movies) {
            ImageData image = ImageDataFactory.create(String.format(RESOURCE, movie.getImdb()));
            PdfImageXObject img = new PdfImageXObject(image);
            float scaleY = 60 / img.getHeight();
            canvas.addImage(image, img.getWidth() * scaleY, 0, 0, 60, x + (45 - image.getWidth() * scaleY) / 2, y, false);
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        canvas.release();

        canvas = new PdfCanvas(page = pdfDoc.addNewPage());
        // Add the template using a different CTM
        canvas.addXObject(xObj, 0.8f, 0, 0.35f, 0.65f, 0, 600);

        Document document = new Document(pdfDoc);

        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(xObj, 0, 480).setPageNumber(3);
        document.add(image);

        image.setRotationAngle(Math.PI / 6).
                scale(.8f, .8f).
                setFixedPosition(3, 30, 500);
        document.add(image);

        image.setRotationAngle(Math.PI / 2).
                setFixedPosition(3, 200, 300);
        document.add(image);

        //Close document
        document.close();
    }


}
