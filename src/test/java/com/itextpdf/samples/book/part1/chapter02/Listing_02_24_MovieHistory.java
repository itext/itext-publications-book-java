/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.element.*;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import com.lowagie.filmfestival.PojoToElementFactory;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_02_24_MovieHistory extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_24_MovieHistory.pdf";

    public List<BlockElement> titles = new ArrayList<>();

    public static final String[] EPOCH =
            {"Forties", "Fifties", "Sixties", "Seventies", "Eighties",
                    "Nineties", "Twenty-first Century"};

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_24_MovieHistory().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> movies =
                new TreeSet<>(new MovieComparator(MovieComparator.BY_YEAR));
        movies.addAll(PojoFactory.getMovies(connection));
        int epoch = -1;
        int currentYear = 0;
        Div firstLevelParagraph = null;
        Div secondLevelParagraph = null;
        Div thirdLevelParagraph = null;

        String firstLevelTitle = null;
        String secondLevelTitle = null;
        String thirdLevelTitle = null;

        PdfOutline rootOutLine = pdfDoc.getOutlines(false);
        PdfOutline firstLevel = null;
        PdfOutline secondLevel = null;
        PdfOutline thirdLevel = null;
        int firstLevelOrder = 0;
        int secondLevelOrder = 0;
        int thirdLevelOrder = 0;

        for (Movie movie : movies) {
            // add the chapter if we're in a new epoch
            if (epoch < (movie.getYear() - 1940) / 10) {
                epoch = (movie.getYear() - 1940) / 10;
                if (null != firstLevelParagraph) {
                    doc.add(firstLevelParagraph);
                    doc.add(new AreaBreak());
                }
                // chapter
                firstLevelOrder++;
                secondLevelOrder = 0;
                firstLevelTitle = firstLevelOrder + " " + EPOCH[epoch];
                firstLevelParagraph = new Div().add(new Paragraph(firstLevelTitle).setFont(font).setFontSize(24).setBold());
                firstLevelParagraph.setProperty(Property.DESTINATION, firstLevelTitle);
                firstLevel = rootOutLine.addOutline(firstLevelTitle);
                firstLevel.addDestination(PdfDestination.makeDestination(new PdfString(firstLevelTitle)));
                titles.add(new Paragraph(firstLevelTitle).setFontSize(10));
            }
            // switch to a new year
            if (currentYear < movie.getYear()) {
                currentYear = movie.getYear();
                // section
                secondLevelOrder++;
                thirdLevelOrder = 0;
                secondLevelTitle = firstLevelOrder + ". " + secondLevelOrder + " " +
                        String.format("The year %d", movie.getYear());
                secondLevelParagraph = new Div().add(new Paragraph(secondLevelTitle).setFont(font).setFontSize(18));
                secondLevelParagraph.setProperty(Property.DESTINATION, secondLevelTitle);
                secondLevel = firstLevel.addOutline(secondLevelTitle);
                secondLevel.addDestination(PdfDestination.makeDestination(new PdfString(secondLevelTitle)));

                secondLevelParagraph.add(new Paragraph(
                        String.format("Movies from the year %d:", movie.getYear())).setMarginLeft(10));

                titles.add(new Paragraph(secondLevelTitle)
                        .setFont(font)
                        .setFontSize(10)
                        .setMarginLeft(10));

                firstLevelParagraph.add(secondLevelParagraph);

            }
            // subsection
            thirdLevelOrder++;
            thirdLevelTitle = thirdLevelOrder + " " + movie.getMovieTitle();
            thirdLevelParagraph = new Div().add(new Paragraph(thirdLevelTitle).setFont(font).setFontSize(14).setMarginLeft(20));
            thirdLevelParagraph.setProperty(Property.DESTINATION, thirdLevelTitle);
            thirdLevel = secondLevel.addOutline(thirdLevelTitle);
            thirdLevel.addDestination(PdfDestination.makeDestination(new PdfString(thirdLevelTitle)));


            thirdLevelParagraph.add(new Paragraph("Duration: " + movie.getDuration()).setFont(bold).setMarginLeft(20));
            thirdLevelParagraph.add(new Paragraph("Director(s):").setFont(bold).setMarginLeft(20));
            thirdLevelParagraph.add(PojoToElementFactory.getDirectorList(movie).setMarginLeft(20));
            thirdLevelParagraph.add(new Paragraph("Countries:").setFont(bold).setMarginLeft(20));
            thirdLevelParagraph.add(PojoToElementFactory.getCountryList(movie).setMarginLeft(20));

            titles.add(new Paragraph(thirdLevelTitle)
                    .setFont(font)
                    .setMarginLeft(20)
                    .setFontSize(10));


            secondLevelParagraph.add(thirdLevelParagraph);
        }
        doc.add(firstLevelParagraph);

        doc.close();
        connection.close();
    }
}
