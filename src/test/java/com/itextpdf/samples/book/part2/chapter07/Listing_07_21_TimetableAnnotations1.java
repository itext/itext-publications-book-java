/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.color.WebColors;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.TimeZone;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class Listing_07_21_TimetableAnnotations1 extends GenericTest {
    /**
     * The number of locations on our time table.
     */
    public static final int LOCATIONS = 9;
    /**
     * The number of time slots on our time table.
     */
    public static final int TIMESLOTS = 32;
    /**
     * The "offset time" for our calendar sheets.
     */
    public static final long TIME930 = 30600000l;
    /**
     * The offset to the left of our time table.
     */
    public static final float OFFSET_LEFT = 76;
    /**
     * The width of our time table.
     */
    public static final float WIDTH = 740;
    /**
     * The width of a time slot.
     */
    public static final float WIDTH_TIMESLOT = WIDTH / TIMESLOTS;
    /**
     * The width of one minute.
     */
    public static final float MINUTE = WIDTH_TIMESLOT / 30f;
    /**
     * The offset from the bottom of our time table.
     */
    public static final float OFFSET_BOTTOM = 36;
    /**
     * The height of our time table
     */
    public static final float HEIGHT = 504;
    /**
     * The height of a bar showing the movies at one specific location.
     */
    public static final float HEIGHT_LOCATION = HEIGHT / LOCATIONS;

    /*static {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Brussels"));
    }*/

    public static final String DEST
            = "./target/test/resources/book/part2/chapter07/Listing_07_21_TimetableAnnotations1.pdf";

    /**
     * A pattern for an info string.
     */
    public static final String INFO = "Movie produced in %s; run length: %s";

    /**
     * A list containing all the locations.
     */
    protected List<String> locations;

    protected String[] arguments;

    public static final String MOVIE_TEMPLATES = "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    public static TimeZone CURRENT_USER_TIME_ZONE;


    protected void beforeManipulatePdf() {
        CURRENT_USER_TIME_ZONE = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Brussels"));
    }

    protected void afterManipulatePdf() {
        TimeZone.setDefault(CURRENT_USER_TIME_ZONE);
    }

    public static void main(String args[]) throws IOException, SQLException {
        Listing_07_21_TimetableAnnotations1 application = new Listing_07_21_TimetableAnnotations1();
        application.arguments = args;
        application.beforeManipulatePdf();
        application.manipulatePdf(DEST);
        application.afterManipulatePdf();
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Listing_03_29_MovieTemplates.main(arguments);
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        locations = PojoFactory.getLocations(connection);
        // Create a reader
        PdfReader reader = new PdfReader(MOVIE_TEMPLATES);
        // Create a stamper
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        // Add the annotations
        int page = 1;
        Rectangle rect;
        PdfAnnotation annotation;
        Movie movie;
        for (Date day : PojoFactory.getDays(connection)) {
            for (Screening screening : PojoFactory.getScreenings(connection, day)) {
                movie = screening.getMovie();
                rect = getPosition(screening);
                annotation = new PdfTextAnnotation(rect);
                annotation.setTitle(new PdfString(movie.getMovieTitle()));
                annotation.setContents(String.format(INFO, movie.getYear(), movie.getDuration()));
                // for the right image
                annotation.setName(new PdfString("Help"));
                annotation.put(PdfName.Name, new PdfString("Help"));
                DeviceRgb baseColor = WebColors.getRGBColor(
                        "#" + movie.getEntry().getCategory().getColor());
                annotation.setColor(baseColor);
                pdfDoc.getPage(page).addAnnotation(annotation);
            }
            page++;
        }
        // Close the stamper
        pdfDoc.close();
        reader.close();
        // Close the database connection
        connection.close();
    }

    /**
     * Calculates the position of a rectangle corresponding with a screening.
     *
     * @param screening a screening POJO, contains a movie
     * @return a Rectangle
     */
    protected Rectangle getPosition(Screening screening) {
        float llx, lly, urx, ury;
        long minutesAfter930 = (screening.getTime().getTime() - TIME930) / 60000l;
        llx = OFFSET_LEFT + (MINUTE * minutesAfter930);
        int location = locations.indexOf(screening.getLocation()) + 1;
        lly = OFFSET_BOTTOM + (LOCATIONS - location) * HEIGHT_LOCATION;
        urx = llx + MINUTE * screening.getMovie().getDuration();
        ury = lly + HEIGHT_LOCATION;
        Rectangle rect = new Rectangle(llx, lly, urx - llx, ury - lly);
        return rect;
    }
}
