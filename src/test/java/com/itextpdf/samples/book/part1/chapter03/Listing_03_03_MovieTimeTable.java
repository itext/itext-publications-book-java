/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_03_03_MovieTimeTable extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_03_MovieTimeTable.pdf";

    /** The number of locations on our time table. */
    public static final int LOCATIONS = 9;
    /** The number of time slots on our time table. */
    public static final int TIMESLOTS = 32;

    /** The offset to the left of our time table. */
    public static final float OFFSET_LEFT = 76;
    /** The width of our time table. */
    public static final float WIDTH = 740;
    /** The offset from the bottom of our time table. */
    public static final float OFFSET_BOTTOM = 36;
    /** The height of our time table */
    public static final float HEIGHT = 504;

    /** The offset of the location bar next to our time table. */
    public static final float OFFSET_LOCATION = 26;
    /** The width of the location bar next to our time table. */
    public static final float WIDTH_LOCATION = 48;

    /** The height of a bar showing the movies at one specific location. */
    public static final float HEIGHT_LOCATION = HEIGHT / LOCATIONS;
    /** The width of a time slot. */
    public static final float WIDTH_TIMESLOT = WIDTH / TIMESLOTS;

    public static void main(String[] args) throws Exception {
        new Listing_03_03_MovieTimeTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

        PdfPage page = pdfDoc.addNewPage();
        drawTimeTable(new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc));
        drawTimeSlots(new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc));

        pdfDoc.close();
    }

    /**
     * Draws the time table for a day at the film festival.
     * @param canvas a canvas to which the time table has to be drawn.
     */
    protected void drawTimeTable(PdfCanvas canvas) {
        canvas.saveState().
                setLineWidth(1.2f);

        float llx, lly, urx, ury;
        llx = OFFSET_LEFT;
        lly = OFFSET_BOTTOM;
        urx = OFFSET_LEFT + WIDTH;
        ury = OFFSET_BOTTOM + HEIGHT;
        canvas.moveTo(llx, lly).
                lineTo(urx, lly).
                lineTo(urx, ury).
                lineTo(llx, ury).
                closePath().
                stroke();

        llx = OFFSET_LOCATION;
        lly = OFFSET_BOTTOM;
        urx = OFFSET_LOCATION + WIDTH_LOCATION;
        ury = OFFSET_BOTTOM + HEIGHT;
        canvas.moveTo(llx, lly).
                lineTo(urx, lly).
                lineTo(urx, ury).
                lineTo(llx, ury).
                closePathStroke();

        canvas.setLineWidth(1).
                moveTo(OFFSET_LOCATION + WIDTH_LOCATION / 2, OFFSET_BOTTOM).
                lineTo(OFFSET_LOCATION + WIDTH_LOCATION / 2, OFFSET_BOTTOM + HEIGHT);

        float y;
        for (int i = 1; i < LOCATIONS; i++) {
            y = OFFSET_BOTTOM + (i * HEIGHT_LOCATION);
            if (i == 2 || i == 6) {
                canvas.moveTo(OFFSET_LOCATION, y);
                canvas.lineTo(OFFSET_LOCATION + WIDTH_LOCATION, y);
            }
            else {
                canvas.moveTo(OFFSET_LOCATION + WIDTH_LOCATION / 2, y);
                canvas.lineTo(OFFSET_LOCATION + WIDTH_LOCATION, y);
            }
            canvas.moveTo(OFFSET_LEFT, y);
            canvas.lineTo(OFFSET_LEFT + WIDTH, y);
        }
        canvas.stroke();

        canvas.restoreState();
    }

    /**
     * Draws the time slots for a day at the film festival.
     * @param canvas the canvas to which the time table has to be drawn.
     */
    protected void drawTimeSlots(PdfCanvas canvas) {
        canvas.saveState();
        float x;
        for (int i = 1; i < TIMESLOTS; i++) {
            x = OFFSET_LEFT + (i * WIDTH_TIMESLOT);
            canvas.moveTo(x, OFFSET_BOTTOM);
            canvas.lineTo(x, OFFSET_BOTTOM + HEIGHT);
        }
        canvas.setLineWidth(0.3f);
        canvas.setStrokeColor(DeviceGray.GRAY);
        canvas.setLineDash(3, 1);
        canvas.stroke();
        canvas.restoreState();
    }
}
