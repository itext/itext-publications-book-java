/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;

public class Listing_04_21_PdfCalendar {
    public static final String DEST = "./target/book/part1/chapter04/Listing_04_21_PdfCalendar.pdf";
    /**
     * The year for which we want to create a calendar
     */
    public static final int YEAR = 2011;
    /**
     * The language code for the calendar
     */
    public static final String LANGUAGE = "en";
    /**
     * Collection with special days
     */
    public static Properties specialDays = new Properties();
    /**
     * Collection with the description of the images
     */

    public static final String RESOURCE = "./src/main/resources/calendar/%tm.jpg";
    public static final String SPECIAL = "./src/main/resources/calendar/%d.txt";
    public static final String CONTENT = "./src/main/resources/calendar/content.txt";

    public static Properties content = new Properties();

    protected PdfFont normal;
    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_21_PdfCalendar().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        Locale locale = new Locale(LANGUAGE);
        createPdf(dest, locale, YEAR);
    }

    public void createPdf(String dest, Locale locale, int year) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        // fonts
        normal = PdfFontFactory.createFont(/*"c://windows/fonts/arial.ttf"*/"./src/main/resources/font/FreeSans.ttf", PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        bold = PdfFontFactory.createFont(/*"c://windows/fonts/arialbd.ttf"*/"./src/main/resources/font/FreeSans.ttf", PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);

        // collections
        specialDays.load(new FileInputStream(String.format(SPECIAL, YEAR)));
        content.load(new FileInputStream(CONTENT));

        Table table;
        Calendar calendar;
        // Loop over the months
        for (int month = 0; month < 12; month++) {
            calendar = new GregorianCalendar(year, month, 1);
            // draw the background
            drawImageAndText(calendar, doc);
            // create a table with 7 columns
            table = new Table(UnitValue.createPercentArray(7)).useAllAvailableWidth();
            table.setWidth(504);
            // add the name of the month
            table.addCell(getMonthCell(calendar, locale));
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = 1;
            int position = 2;
            // add empty cells
            while (position != calendar.get(Calendar.DAY_OF_WEEK)) {
                position = (position % 7) + 1;
                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(ColorConstants.WHITE));
            }
            // add cells for each day
            while (day <= daysInMonth) {
                calendar = new GregorianCalendar(year, month, day++);
                position = (position % 7) + 1;
                table.addCell(getDayCell(calendar, locale));
            }
            // add empty cells
            while (position != 2) {
                position = (position % 7) + 1;
                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(ColorConstants.WHITE));
            }
            doc.add(table.setFixedPosition(169, 18, 504));
            if (11 != month) {
                doc.add(new AreaBreak());
            }
        }
        doc.close();
    }

    /**
     * Draws the image of the month to the calendar.
     *
     * @param calendar the month (to know which picture to use)
     * @param doc      the document model
     * @throws IOException
     */
    public void drawImageAndText(Calendar calendar, Document doc) throws IOException {
        // get the image
        Image img = new Image(ImageDataFactory.create(String.format(RESOURCE, calendar)));
        img.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        img.setFixedPosition(
                (PageSize.A4.getHeight() - img.getImageScaledWidth()) / 2,
                (PageSize.A4.getWidth() - img.getImageScaledHeight()) / 2);
        doc.add(img);
        // add metadata
        Paragraph p = new Paragraph(String.format("%s - \u00a9 Katharine Osborne",
                content.getProperty(String.format("%tm.jpg", calendar))))
                .setFont(normal)
                .setFontColor(new DeviceCmyk(0, 0, 0, 50))
                .setFontSize(8);
        doc.showTextAligned(p, 5, 5, calendar.get(Calendar.MONTH) + 1,
                TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        p = new Paragraph("Calendar generated using iText - example for the book iText in Action 2nd Edition")
                .setFont(normal)
                .setFontColor(new DeviceCmyk(0, 0, 0, 50))
                .setFontSize(8);
        doc.showTextAligned(p, 839, 5, calendar.get(Calendar.MONTH) + 1,
                TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
    }

    /**
     * Creates a PdfPCell with the name of the month
     *
     * @param calendar a date
     * @param locale   a locale
     * @return a PdfPCell with rowspan 7, containing the name of the month
     */
    public Cell getMonthCell(Calendar calendar, Locale locale) {
        Cell cell = new Cell(1, 7);
        cell.setBackgroundColor(ColorConstants.WHITE);
        Paragraph p = new Paragraph(String.format(locale, "%1$tB %1$tY", calendar)).setFont(bold).setFontSize(14);
        p.setTextAlignment(TextAlignment.CENTER);
        cell.add(p);
        return cell;
    }

    /**
     * Creates a PdfPCell for a specific day
     *
     * @param calendar a date
     * @param locale   a locale
     * @return a PdfPCell
     */
    public Cell getDayCell(Calendar calendar, Locale locale) {
        Cell cell = new Cell();
        cell.setPadding(3);
        // set the background color, based on the type of day
        if (isSunday(calendar)) {
            cell.setBackgroundColor(ColorConstants.GRAY);
        } else if (isSpecialDay(calendar)) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        } else {
            cell.setBackgroundColor(ColorConstants.WHITE);
        }
        // set the content in the language of the locale
        Text text = new Text(String.format(locale, "%1$ta", calendar)).setFont(normal).setFontSize(8);
        text.setTextRise(8);
        // a paragraph with the day
        Paragraph p = new Paragraph(text);
        // a separator
        p.addTabStops(new TabStop(100, TabAlignment.RIGHT));
        p.add(new Tab());
        // and the number of the day
        p.add(new Text(String.format(locale, "%1$te", calendar)).setFont(normal).setFontSize(16));
        cell.add(p);
        return cell;
    }

    /**
     * Returns true for Sundays.
     *
     * @param calendar a date
     * @return true for Sundays
     */
    public boolean isSunday(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the date was found in a list with special days (holidays).
     *
     * @param calendar a date
     * @return true for holidays
     */
    public boolean isSpecialDay(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        if (specialDays.containsKey(String.format("%1$tm%1$td", calendar)))
            return true;
        return false;
    }
}
