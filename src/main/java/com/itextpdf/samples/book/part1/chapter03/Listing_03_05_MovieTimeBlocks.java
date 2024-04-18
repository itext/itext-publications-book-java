package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class Listing_03_05_MovieTimeBlocks extends Listing_03_03_MovieTimeTable {

    public static final String DEST = "./target/book/part1/chapter03/Listing_03_05_MovieTimeBlocks.pdf";

    /** The "offset time" for our calendar sheets. */
    public static final long TIME930 = Time.valueOf("09:30:00").getTime();

    /** The width of one minute. */
    public static final float MINUTE = WIDTH_TIMESLOT / 30f;

    /** A list containing all the locations. */
    protected List<String> locations;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_05_MovieTimeBlocks().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(PageSize.A4.rotate());


        try {
            DatabaseConnection connection = new HsqldbConnection("filmfestival");
            locations = PojoFactory.getLocations(connection);
            List<Date> days = PojoFactory.getDays(connection);
            List<Screening> screenings;
            for (Date day : days) {
                PdfPage page = pdfDoc.addNewPage();
                PdfCanvas over = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
                PdfCanvas under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

                drawTimeTable(under);
                drawTimeSlots(over);
                screenings = PojoFactory.getScreenings(connection, day);
                for (Screening screening : screenings) {
                    drawBlock(screening, under, over);
                }
            }
            connection.close();
        }
        catch(SQLException sqle) {
            sqle.printStackTrace();
            //document.add(new Paragraph("Database error: " + sqle.getMessage()));
        }

        pdfDoc.close();
    }

    /**
     * Draws a colored block on the time table, corresponding with
     * the screening of a specific movie.
     * @param    screening    a screening POJO, contains a movie and a category
     * @param    under    the canvas to which the block is drawn
     */
    protected void drawBlock(Screening screening, PdfCanvas under, PdfCanvas over) {
        under.saveState();


        DeviceRgb color = WebColors.getRGBColor(
                "#" + screening.getMovie().getEntry().getCategory().getColor());

        under.setFillColor(color);
        Rectangle rect = getPosition(screening);
        under.rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        under.fill();
        over.rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        over.stroke();
        under.restoreState();
    }

    /**
     * Calculates the position of a rectangle corresponding with a screening.
     * @param    screening    a screening POJO, contains a movie
     * @return    a Rectangle
     */
    protected Rectangle getPosition(Screening screening) {
        float llx, lly, width, height;
        long minutesAfter930 = (screening.getTime().getTime() - TIME930) / 60000l;
        llx = OFFSET_LEFT + (MINUTE * minutesAfter930);
        int location = locations.indexOf(screening.getLocation()) + 1;
        lly = OFFSET_BOTTOM + (LOCATIONS - location) * HEIGHT_LOCATION;
        width = MINUTE * screening.getMovie().getDuration();
        height = HEIGHT_LOCATION;
        Rectangle rect = new Rectangle(llx, lly, width, height);
        return rect;
    }
}
