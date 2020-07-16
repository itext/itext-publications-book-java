package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.book.part1.chapter02.Listing_02_07_MovieParagraphs1;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Listing_13_06_PageLayoutExample extends Listing_02_07_MovieParagraphs1 {
    public static final String[] RESULT = new String[] {
            "./target/book/part4/chapter13/Listing_13_06_PageLayoutSingle.pdf",
            "./target/book/part4/chapter13/Listing_13_06_PageLayoutColumn.pdf",
            "./target/book/part4/chapter13/Listing_13_06_PageLayoutColumns_l.pdf",
            "./target/book/part4/chapter13/Listing_13_06_PageLayoutColumns_r.pdf",
            "./target/book/part4/chapter13/Listing_13_06_PageLayoutPages_l.pdf",
            "./target/book/part4/chapter13/Listing_13_06_PageLayoutPages_r.pdf"
    };

    public static final String DEST = RESULT[0];

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_06_PageLayoutExample().manipulatePdf(DEST);
    }

    /**
     * Creates a PDF with information about the movies
     *
     * @param dest             the name of the PDF file that will be created.
     * @param pageLayoutMode   the layout mode of the page
     * @throws IOException
     * @throws SQLException
     */
    public void createPdf(String dest, PdfName pageLayoutMode) throws IOException, SQLException {
        // Create a database connection
        createFonts();
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_5)));
        Document doc = new Document(pdfDoc);
        pdfDoc.getCatalog().setPageLayout(pageLayoutMode);

        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            Paragraph p = createMovieInformation(movie);
            p.setTextAlignment(TextAlignment.JUSTIFIED);
            p.setMarginLeft(18);
            p.setFirstLineIndent(-18);
            doc.add(p);
        }

        doc.close();
        connection.close();
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        createPdf(RESULT[0], PdfName.SinglePage);;
        createPdf(RESULT[1], PdfName.OneColumn);
        createPdf(RESULT[2], PdfName.TwoColumnLeft);
        createPdf(RESULT[3], PdfName.TwoColumnRight);
        createPdf(RESULT[4], PdfName.TwoPageLeft);
        createPdf(RESULT[5], PdfName.TwoPageRight);
    }
}
