package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Listing_05_06_PressPreviews {
    public static final String DEST =
            "./target/book/part1/chapter05/Listing_05_06_PressPreviews.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_05_06_PressPreviews().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        doc.add(getTable(connection));

        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection) throws UnsupportedEncodingException, SQLException {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 5, 1})).useAllAvailableWidth();
        table.setBorder(new SolidBorder(1));
        Cell cell;

        Style defaultCellStyle = new Style().setBorder(Border.NO_BORDER).
                setPaddingBottom(5).setPaddingTop(5).
                setPaddingLeft(5).setPaddingRight(5);

        for (int i = 0; i < 2; i++) {
            List<Cell> cells = new ArrayList<>();
            cells.add(new Cell().add(new Paragraph("Location")));
            cells.add(new Cell().add(new Paragraph("Date/Time")));
            cells.add(new Cell().add(new Paragraph("Run Length")));
            cells.add(new Cell().add(new Paragraph("Title")));
            cells.add(new Cell().add(new Paragraph("Year")));

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
            cell = new Cell().add(new Paragraph(screening.getLocation()))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(String.format("%s   %2$tH:%2$tM", screening.getDate().toString(), screening.getTime())))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(String.format("%d '", movie.getDuration())))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(movie.getMovieTitle()))
                    .addStyle(defaultCellStyle);
            cell.setNextRenderer(new PressPreviewsCellRenderer(cell));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(String.valueOf(movie.getYear())))
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

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new PressPreviewsCellRenderer((Cell) modelElement);
        }
    }

}
