package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.lowagie.filmfestival.Screening;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listing_04_17_NestedTables {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_17_NestedTables.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";

    public Map<String, ImageData> images = new HashMap<>();

    protected PdfFont bold;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_17_NestedTables().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD); // 12

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
    }


    public Table getTable(DatabaseConnection connection, Date day)
            throws SQLException, MalformedURLException {
        // Create a table with only one column
        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        // add the cell with the date
        Cell cell = new Cell().add(new Paragraph(day.toString()).setFontColor(ColorConstants.WHITE));
        cell.setBackgroundColor(ColorConstants.BLACK);
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
        // add the movies as nested tables
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        for (Screening screening : screenings) {
            table.addCell(new Cell().add(getTable(screening)));
        }
        return table;
    }

    private Table getTable(Screening screening) throws MalformedURLException {
        // Create a table with 4 columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 5, 10, 10})).useAllAvailableWidth();
        // Get the movie
        Movie movie = screening.getMovie();
        // A cell with the title as a nested table spanning the complete row
        Cell cell = new Cell(1, 4);
        // nesting is done with addElement() in this example
        cell.add(fullTitle(screening));
        cell.setBorder(Border.NO_BORDER);

        DeviceRgb color = WebColors.getRGBColor(
                "#" + screening.getMovie().getEntry().getCategory().getColor());
        cell.setBackgroundColor(color);
        table.addCell(cell);
        // empty cell
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        // cell with the movie poster
        cell = new Cell().add(getImage(movie.getImdb()));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        // cell with the list of directors
        cell = new Cell();
        cell.add(PojoToElementFactory.getDirectorList(movie));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        // cell with the list of countries
        cell = new Cell();
        cell.add(PojoToElementFactory.getCountryList(movie));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        return table;
    }

    private Table fullTitle(Screening screening) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 15, 2})).useAllAvailableWidth();
        // cell 1: location and time
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(ColorConstants.WHITE);
        String s = String.format("%s \u2013 %2$tH:%2$tM",
                screening.getLocation(), screening.getTime().getTime());
        cell.add(new Paragraph(s).setTextAlignment(TextAlignment.CENTER));
        table.addCell(cell);
        // cell 2: English and original title
        Movie movie = screening.getMovie();
        Paragraph p = new Paragraph();
        p.add(new Text(movie.getMovieTitle()).setFont(bold));
        p.setFixedLeading(16);
        if (movie.getOriginalTitle() != null) {
            p.add(new Text(" (" + movie.getOriginalTitle() + ")"));
        }
        cell = new Cell();
        cell.add(p);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        // cell 3 duration
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setBackgroundColor(ColorConstants.WHITE);
        p = new Paragraph(String.format("%d'", movie.getDuration()));
        p.setTextAlignment(TextAlignment.CENTER);
        cell.add(p);
        table.addCell(cell);
        return table;
    }

    public Image getImage(String imdb) throws MalformedURLException {
        ImageData img = images.get(imdb);
        if (img == null) {
            img = ImageDataFactory.create(String.format(RESOURCE, imdb));
            images.put(imdb, img);
        }
        return new Image(img).scaleToFit(80, 72);
    }
}
