/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import org.junit.experimental.categories.Category;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Category(SampleTest.class)
public class Listing_03_11_MovieTextInfo extends Listing_03_05_MovieTimeBlocks {

    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_11_MovieTextInfo.pdf";

    /** The different time slots. */
    public static String[] TIME =
            { "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
                    "00:30", "01:00", "01:30", "02:00", "02:30", "03:00",
                    "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
                    "06:30", "07:00", "07:30", "08:00", "08:30", "09:00",
                    "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
                    "00:30", "01:00" };

    public static void main(String[] args) throws Exception {
        new Listing_03_11_MovieTextInfo().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4.getHeight(), PageSize.A4.getWidth()));
        doc.setMargins(0, 0, 0, 0);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        doc.setProperty(Property.FONT, font);

        Text press = new Text("P").
                setFont(font).
                setFontSize(HEIGHT_LOCATION / 2).
                setFontColor(Color.WHITE);

        try {
            DatabaseConnection connection = new HsqldbConnection("filmfestival");
            locations = PojoFactory.getLocations(connection);
            List<Date> days = PojoFactory.getDays(connection);
            List<Screening> screenings;
            int d = 1;
            for (Date day : days) {
                PdfPage page = pdfDoc.addNewPage();
                if (d != 1)
                    doc.add(new AreaBreak());
                PdfCanvas over = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
                PdfCanvas under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

                drawTimeTable(under);
                drawTimeSlots(over);
                drawInfo(doc);
                drawDateInfo(day, d++, doc);
                screenings = PojoFactory.getScreenings(connection, day);
                for (Screening screening : screenings) {
                    drawBlock(screening, under, over);
                    drawMovieInfo(screening, doc, press);
                }
            }
            connection.close();
        }
        catch(SQLException sqle) {
            sqle.printStackTrace();
            //document.add(new Paragraph("Database error: " + sqle.getMessage()));
        }

        doc.close();
    }

    /**
     * Draws some text on every calendar sheet.
     *
     */
    protected void drawInfo(Document doc) {
        float x, y;
        x = (OFFSET_LEFT + OFFSET_LOCATION) / 2;
        y = OFFSET_BOTTOM + HEIGHT + 24;
        doc.add(new Paragraph("FOOBAR FILM FESTIVAL").setFontSize(18)
                .setFixedPosition(x, y, WIDTH).setTextAlignment(TextAlignment.CENTER));

        x = OFFSET_LOCATION + WIDTH_LOCATION / 2f - 3;
        y = OFFSET_BOTTOM;
        doc.add(new Paragraph("The Majestic").setFontSize(18)
                .setFixedPosition(x, y, HEIGHT_LOCATION * 2)
                .setRotationAngle(Math.PI / 2)
                .setTextAlignment(TextAlignment.CENTER));

        y = OFFSET_BOTTOM + HEIGHT_LOCATION * 2;
        doc.add(new Paragraph("Googolplex").setFontSize(18)
                .setFixedPosition(x, y, HEIGHT_LOCATION * 4)
                .setRotationAngle(Math.PI / 2)
                .setTextAlignment(TextAlignment.CENTER));

        y = OFFSET_BOTTOM + HEIGHT_LOCATION * 6f;
        doc.add(new Paragraph("Cinema Paradiso").setFontSize(18)
                .setFixedPosition(x, y, HEIGHT_LOCATION * 3)
                .setRotationAngle(Math.PI / 2)
                .setTextAlignment(TextAlignment.CENTER));

        x = OFFSET_LOCATION + WIDTH_LOCATION - 6;
        for (int i = 0; i < LOCATIONS; i++) {
            y = OFFSET_BOTTOM + ((8 - i) * HEIGHT_LOCATION);
            doc.add(new Paragraph(locations.get(i)).setFontSize(12)
                    .setFixedPosition(x, y, HEIGHT_LOCATION)
                    .setRotationAngle(Math.PI / 2)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        y = OFFSET_BOTTOM + HEIGHT;
        for (int i = 0; i < TIMESLOTS; i++) {
            x = OFFSET_LEFT + (i * WIDTH_TIMESLOT);
            doc.add(new Paragraph(TIME[i]).setFontSize(6)
                    .setFixedPosition(x, y, 100).setTextAlignment(TextAlignment.LEFT)
                    .setRotationAngle(Math.PI / 4));
        }
    }
    /**
     * Draws some text on every calendar sheet.
     *
     */
    protected void drawDateInfo(Date day, int d, Document doc) {
        float x, y;
        x = OFFSET_LOCATION;
        y = OFFSET_BOTTOM + HEIGHT + 12;

        Paragraph p1 = new Paragraph("Day " + d).
                setFontSize(18).
                setFixedPosition(d, x, y, 100);

        x = OFFSET_LEFT;

        Paragraph p2 = new Paragraph(day.toString()).
                setFontSize(18).
                setFixedPosition(d, x, y, 100).
                setWidth(WIDTH).
                setTextAlignment(TextAlignment.RIGHT);

        doc.add(p1).add(p2);
    }

    /**
     * Draws the info about the movie.
     */
    protected void drawMovieInfo(Screening screening, Document doc, Text press) {
        if (screening.isPress()) {
            Rectangle rect = getPosition(screening);

            Paragraph p = new Paragraph().add(press).
                    setFixedPosition(rect.getX(), rect.getY(), rect.getWidth()).
                    setHeight(rect.getHeight()).
                    setTextAlignment(TextAlignment.CENTER);
            doc.add(p);
        }
    }
}
