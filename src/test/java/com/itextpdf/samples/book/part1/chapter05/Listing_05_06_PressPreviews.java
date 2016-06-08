/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_05_06_PressPreviews extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_06_PressPreviews.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_06_PressPreviews().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        doc.add(getTable(connection));

        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection) throws UnsupportedEncodingException, SQLException {
        Table table = new Table(new float[]{50, 50, 50, 100, 50});
        table.setWidthPercent(100);
        table.setBorder(new SolidBorder(1));
        Cell cell;

        Style defaultCellStyle = new Style().setBorder(Border.NO_BORDER).
                setPaddingBottom(5).setPaddingTop(5).
                setPaddingLeft(5).setPaddingRight(5);

        for (int i = 0; i < 2; i++) {
            List<Cell> cells = new ArrayList<>();
            cells.add(new Cell().add("Location"));
            cells.add(new Cell().add("Date/Time"));
            cells.add(new Cell().add("Run Length"));
            cells.add(new Cell().add("Title"));
            cells.add(new Cell().add("Year"));

            for (Cell c : cells) {
                c.addStyle(defaultCellStyle);
                c.setNextRenderer(new PressPreviewsCellRenderer(c));
                if (i == 0) {
                    table.addHeaderCell(c);
                } else {
                    table.addFooterCell(c);
                }
            }
        }

        List<Screening> screenings = PojoFactory.getPressPreviews(connection);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            cell = new Cell().add(screening.getLocation())
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(String.format("%s   %2$tH:%2$tM", screening.getDate().toString(), screening.getTime()))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(String.format("%d '", movie.getDuration()))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(movie.getMovieTitle())
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(String.valueOf(movie.getYear()))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
        }
        return table;
    }


    private class PressPreviewsCellRenderer extends CellRenderer {
        public PressPreviewsCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            drawContext.getCanvas()
                    .rectangle(rect.getLeft() + 2, rect.getBottom() + 2, rect.getWidth() - 4, rect.getHeight() - 4)
                    .stroke();
        }

        @Override
        public CellRenderer getNextRenderer() {
            return new PressPreviewsCellRenderer((Cell) modelElement);
        }
    }

}
