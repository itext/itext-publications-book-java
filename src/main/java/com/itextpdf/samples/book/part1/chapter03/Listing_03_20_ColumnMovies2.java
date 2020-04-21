package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.book.part1.chapter02.StarSeparator;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Listing_03_20_ColumnMovies2 {

    public static final String DEST = "./target/book/part1/chapter03/Listing_03_20_ColumnMovies2.pdf";

    public static final Rectangle[] COLUMNS = {
            new Rectangle(36, 36, 188, 543), new Rectangle(230, 36, 188, 543),
            new Rectangle(424, 36, 188, 543) , new Rectangle(618, 36, 188, 543)
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_20_ColumnMovies2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        doc.setRenderer(new ColumnDocumentRenderer(doc, COLUMNS));

        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            addContent(doc, movie);
        }

        doc.close();
    }

    public void addContent(Document doc, Movie movie) throws IOException {
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Div div = new Div().setKeepTogether(true);
        Paragraph p = new Paragraph(movie.getTitle()).setFont(bold).
            setTextAlignment(TextAlignment.CENTER).
            setMargins(0, 0, 0, 0);
        div.add(p);
        if (movie.getOriginalTitle() != null) {
            p = new Paragraph(movie.getOriginalTitle()).setFont(italic).
                    setTextAlignment(TextAlignment.RIGHT).
                    setMargins(0, 0, 0, 0);
            div.add(p);
        }
        p = new Paragraph().
                setMargins(0, 0, 0, 0).
                addAll(PojoToElementFactory.getYearPhrase(movie, bold, normal)).
                add(" ").
                addAll(PojoToElementFactory.getDurationPhrase(movie, bold, normal)).
                setTextAlignment(TextAlignment.JUSTIFIED_ALL);
        div.add(p);
        div.add(new StarSeparator());

        doc.add(div);
    }
}
