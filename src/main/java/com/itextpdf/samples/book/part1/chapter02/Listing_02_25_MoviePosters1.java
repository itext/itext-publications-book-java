package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Listing_02_25_MoviePosters1 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_25_MoviePosters1.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_25_MoviePosters1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Rectangle rect = new Rectangle(0, 806, 36, 842);

        new PdfCanvas(pdfDoc.addNewPage())
                .saveState()
                .setStrokeColor(ColorConstants.RED)
                .setFillColor(ColorConstants.RED)
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
