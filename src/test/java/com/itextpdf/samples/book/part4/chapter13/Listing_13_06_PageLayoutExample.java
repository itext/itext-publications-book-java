/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.book.part1.chapter02.Listing_02_07_MovieParagraphs1;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_13_06_PageLayoutExample extends Listing_02_07_MovieParagraphs1 {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutExample.pdf";
    public static String[] RESULT =  new String[]{
            "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutSingle.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutColumn.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutColumns_l.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutColumns_r.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutPages_l.pdf",
            "./target/test/resources/book/part4/chapter13/Listing_13_06_PageLayoutPages_r.pdf"
    };

    public static void main(String args[]) throws IOException, SQLException {
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

    @Override
    public void manipulatePdf(String dest) throws IOException, SQLException {
        createPdf(RESULT[0], PdfName.SinglePage);;
        createPdf(RESULT[1], PdfName.OneColumn);
        createPdf(RESULT[2], PdfName.TwoColumnLeft);
        createPdf(RESULT[3], PdfName.TwoColumnRight);
        createPdf(RESULT[4], PdfName.TwoPageLeft);
        createPdf(RESULT[5], PdfName.TwoPageRight);
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws IOException, InterruptedException {
        CompareTool compareTool = new CompareTool();
        for (int i = 0; i < 6; i++) {
            dest = RESULT[i];
            String outPath = new File(dest).getParent();
            int c = dest.lastIndexOf("/");
            String cmpFile = "./src" + dest.substring(8, c + 1) + "cmp_" +dest.substring(c + 1);
            if (compareRenders) {
                addError(compareTool.compareVisually(dest, cmpFile, outPath, differenceImagePrefix));
                addError(compareTool.compareLinkAnnotations(dest, cmpFile));
            } else {
                addError(compareTool.compareByContent(dest, cmpFile, outPath, differenceImagePrefix));
            }
            addError(compareTool.compareDocumentInfo(dest, cmpFile));
        }
    }
}


