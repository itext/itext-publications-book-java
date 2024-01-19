/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Listing_02_28_MoviePosters3 {
    public static final String DEST = "./target/book/part1/chapter02/Listing_02_28_MoviePosters3.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";

    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_28_MoviePosters3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            // Create an image
            Image img = new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb())));
            img.setHorizontalAlignment(HorizontalAlignment.LEFT);

            img.setBorder(new SolidBorder(ColorConstants.WHITE, 10));
            img.scaleToFit(1000, 72);
            doc.add(img);
            // Create text elements
            doc.add(new Paragraph(movie.getMovieTitle()).setFont(bold).setFixedLeading(18));
            doc.add(PojoToElementFactory.getCountryList(movie));
            doc.add(new Paragraph(String.format("Year: %d", movie.getYear())).setFixedLeading(18));
            doc.add(new Paragraph(
                    String.format("Duration: %d minutes", movie.getDuration())).setFixedLeading(18));
            doc.add(new Paragraph("Directors:").setFixedLeading(18));
            doc.add(PojoToElementFactory.getDirectorList(movie));
            doc.add(new Paragraph("\n").setFixedLeading(18));
        }

        doc.close();
    }
}
