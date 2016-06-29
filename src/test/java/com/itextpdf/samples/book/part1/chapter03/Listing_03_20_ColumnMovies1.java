/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.List;

@Category(SampleTest.class)
public class Listing_03_20_ColumnMovies1 extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_20_ColumnMovies1.pdf";

    public static final String RESOURCE = "src/test/resources/img/posters/%s.jpg";

    public static final Rectangle[] COLUMNS = {
            new Rectangle(36, 36, 188, 543), new Rectangle(230, 36, 188, 543),
            new Rectangle(424, 36, 188, 543) , new Rectangle(618, 36, 188, 543)
    };

    public static void main(String[] args) throws Exception {
        new Listing_03_20_ColumnMovies1().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        doc.setRenderer(new ColumnDocumentRenderer(doc, COLUMNS));

        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            PdfImageXObject imageXObject = new PdfImageXObject(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb())));
            Image img = new Image(imageXObject).
                scaleToFit(80, 1000);
            addContent(doc, movie, img);
        }

        doc.close();
    }

    public void addContent(Document doc, Movie movie, Image img) throws IOException {
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        PdfFont italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
        PdfFont normal = PdfFontFactory.createFont(FontConstants.HELVETICA);

        Div div = new Div().
                setKeepTogether(true).
                setMarginBottom(15);

        div.add(img);
        div.add(new Paragraph(movie.getTitle()).setFont(bold).setMargins(0, 0, 0, 0));
        if (movie.getOriginalTitle() != null) {
            div.add(new Paragraph(movie.getOriginalTitle()).setFont(italic).setMargins(0, 0, 0, 0));
        }
        div.add(PojoToElementFactory.getDirectorList(movie));
        div.add(new Paragraph().setMargins(0, 0, 0, 0).addAll(PojoToElementFactory.getYearPhrase(movie, bold, normal)));
        div.add(new Paragraph().setMargins(0, 0, 0, 0).addAll(PojoToElementFactory.getDurationPhrase(movie, bold, normal)));
        div.add(PojoToElementFactory.getCountryList(movie));

        doc.add(div);
    }
}
