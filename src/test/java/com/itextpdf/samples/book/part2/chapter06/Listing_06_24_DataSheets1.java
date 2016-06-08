/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Category(SampleTest.class)
public class Listing_06_24_DataSheets1 extends Listing_06_19_FillDataSheet {
    public static final String DEST = "./target/test/resources/book/part2/chapter06/Listing_06_24_DataSheets1.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_06_24_DataSheets1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws SQLException, IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(dest));
        PdfDocument pdfDocResult = new PdfDocument(writer);
        pdfDocResult.initializeOutlines();
        addDataSheet(pdfDocResult);
    }

    public void addDataSheet(PdfDocument pdfDocResult) throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        PdfReader reader;

        ByteArrayOutputStream baos;

        // Loop over all the movies and fill out the data sheet
        for (Movie movie : movies) {
            reader = new PdfReader(DATASHEET);
            baos = new ByteArrayOutputStream();
            PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(baos));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            form.setGenerateAppearance(true);
            fill(form, movie);
            form.flattenFields();

            pdfDoc.close();
            pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
            pdfDoc.copyPagesTo(1, pdfDoc.getNumberOfPages(), pdfDocResult);
        }
        // Close the database connection
        pdfDocResult.close();
        connection.close();
    }
}
