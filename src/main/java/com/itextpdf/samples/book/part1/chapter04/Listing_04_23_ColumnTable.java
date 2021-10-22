package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.RootLayoutArea;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Listing_04_23_ColumnTable {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_23_ColumnTable.pdf";
    public static final String RESOURCE =
            "./src/main/resources/img/posters/%s.jpg";

    protected PdfFont bold;
    protected Date date;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_23_ColumnTable().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD, PdfEncodings.WINANSI);

        ColumnDocumentRenderer renderer = new ColumnDocumentRenderer(doc);
        doc.setRenderer(renderer);
        List<Date> days = PojoFactory.getDays(connection);
        // Loop over the festival days
        date = days.get(0);
        for (int i = 0; i < days.size(); i++) {
            // add content to the column
            doc.add(getTable(connection, days.get(i)));

            if (days.size() - 1 != i) {
                date = days.get(i + 1);
                int currentPageNumber = renderer.getCurrentArea().getPageNumber();
                while (renderer.getCurrentArea().getPageNumber() == currentPageNumber) {
                    doc.add(new AreaBreak());
                }
            }
        }

        doc.close();
        connection.close();
    }

    public static Table getHeaderTable(Date day, int page, PdfFont font) {
        Table header = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Style style = new Style().setBackgroundColor(ColorConstants.BLACK).setFontColor(ColorConstants.WHITE).setFont(font);
        Paragraph p = new Paragraph("Foobar Film Festival").addStyle(style);
        header.addCell(new Cell().add(p));
        p = new Paragraph(day.toString()).addStyle(style);
        header.addCell(new Cell().add(p).setTextAlignment(TextAlignment.CENTER));
        p = new Paragraph(String.format("page %d", page)).addStyle(style);
        header.addCell(new Cell().add(p).setTextAlignment(TextAlignment.RIGHT));
        return header;
    }

    public Table getTable(DatabaseConnection connection, Date day)
            throws SQLException, IOException {
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1.5f, 2, 4.5f, 1}));
        Style style = new Style().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addHeaderCell(new Cell().add(new Paragraph("Location")).addStyle(style));
        table.addHeaderCell(new Cell().add(new Paragraph("Time")).addStyle(style));
        table.addHeaderCell(new Cell().add(new Paragraph("Run Length")).addStyle(style));
        table.addHeaderCell(new Cell().add(new Paragraph("Title")).addStyle(style));
        table.addHeaderCell(new Cell().add(new Paragraph("Year")).addStyle(style));
        table.addFooterCell(new Cell().add(new Paragraph("Location")).addStyle(style));
        table.addFooterCell(new Cell().add(new Paragraph("Time")).addStyle(style));
        table.addFooterCell(new Cell().add(new Paragraph("Run Length")).addStyle(style));
        table.addFooterCell(new Cell().add(new Paragraph("Title")).addStyle(style));
        table.addFooterCell(new Cell().add(new Paragraph("Year")).addStyle(style));

        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            table.addCell(String.format("%d '", movie.getDuration()));
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }

    protected class ColumnDocumentRenderer extends DocumentRenderer {
        protected int nextAreaNumber = 0;

        public ColumnDocumentRenderer(Document document) {
            super(document);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new ColumnDocumentRenderer(document);
        }

        @Override
        public LayoutArea updateCurrentArea(LayoutResult overflowResult) {
            if (nextAreaNumber % 2 == 0) {
                currentPageNumber = super.updateCurrentArea(overflowResult).getPageNumber();

                addChild(getHeaderTable(date, currentPageNumber, bold).createRendererSubTree());

                nextAreaNumber++;
                currentArea = new RootLayoutArea(currentPageNumber, new Rectangle(36, 36, 383, 450));
            } else {
                nextAreaNumber++;
                currentArea = new RootLayoutArea(currentPageNumber, new Rectangle(423, 36, 383, 450));
            }
            return currentArea;
        }
    }
}
