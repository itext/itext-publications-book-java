/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import org.junit.experimental.categories.Category;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Category(SampleTest.class)
public class Listing_05_03_RunLengthEvent extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_03_RunLengthEvent.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_03_RunLengthEvent().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        List<Date> days = PojoFactory.getDays(connection);
        int d = 1;
        for (Date day : days) {
            if (1 != d) {
                doc.add(new AreaBreak());
            }
            doc.add(getTable(connection, day));
            d++;
        }
        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection, Date day) throws UnsupportedEncodingException, SQLException {
        Table table = new Table(new float[]{2, 1, 2, 5, 1});
        table.setWidthPercent(100);
        table.addHeaderCell(new Cell(1, 5)
                .add(day.toString())
                .setPadding(3)
                .setBackgroundColor(Color.RED)
                .setTextAlignment(TextAlignment.CENTER));
        Style style = new Style();
        style
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(TextAlignment.LEFT)
                .setPaddingLeft(3)
                .setPaddingRight(3)
                .setPaddingTop(3)
                .setPaddingBottom(3);
        table.addHeaderCell(new Cell()
                .add("Location")
                .addStyle(style));
        table.addHeaderCell(new Cell()
                .add("Time")
                .addStyle(style));
        table.addHeaderCell(new Cell()
                .add("Run Length")
                .addStyle(style));
        table.addHeaderCell(new Cell()
                .add("Title")
                .addStyle(style));
        table.addHeaderCell(new Cell()
                .add("Year")
                .addStyle(style));
        table.addFooterCell(new Cell()
                .add("Location")
                .addStyle(style));
        table.addFooterCell(new Cell()
                .add("Time")
                .addStyle(style));
        table.addFooterCell(new Cell()
                .add("Run Length")
                .addStyle(style));
        table.addFooterCell(new Cell()
                .add("Title")
                .addStyle(style));
        table.addFooterCell(new Cell()
                .add("Year")
                .addStyle(style));
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        Cell runLength;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            runLength = new Cell();
            runLength.setNextRenderer(new FilmCellRenderer(runLength, movie.getDuration(), false));
            runLength.add(String.format("%d '", movie.getDuration()));
            if (screening.isPress()) {
                runLength.setNextRenderer(new FilmCellRenderer(runLength, movie.getDuration(), true));
            }
            table.addCell(runLength);
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }


    private class FilmCellRenderer extends CellRenderer {
        private int duration;
        private boolean isPressPreview;

        public FilmCellRenderer(Cell modelElement, int duration, boolean isPressPreview) {
            super(modelElement);
            this.duration = duration;
            this.isPressPreview = isPressPreview;
        }

        @Override
        public void drawBackground(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.saveState();
            if (duration < 90) {
                canvas.setFillColor(new DeviceRgb(0x7C, 0xFC, 0x00));
            } else if (duration > 120) {
                canvas.setFillColor(new DeviceRgb(0x8B, 0x00, 0x00));
            } else {
                canvas.setFillColor(new DeviceRgb(0xFF, 0xA5, 0x00));
            }
            Rectangle rect = getOccupiedAreaBBox();
            canvas.rectangle(rect.getLeft(), rect.getBottom(),
                    rect.getWidth() * duration / 240, rect.getHeight());
            canvas.fill();
            canvas.restoreState();
            super.drawBackground(drawContext);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            if (isPressPreview) {
                PdfCanvas canvas = drawContext.getCanvas();
                canvas.beginText();
                try {
                    canvas.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Rectangle rect = getOccupiedAreaBBox();
                canvas.moveText(rect.getLeft() + rect.getWidth() / 4, rect.getBottom() + 4.5f);
                canvas.showText("PRESS PREVIEW");
                canvas.endText();
            }
        }

        @Override
        public CellRenderer getNextRenderer() {
            return new FilmCellRenderer(getModelElement(), duration, isPressPreview);
        }
    }
}

