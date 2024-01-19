/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Listing_06_19_FillDataSheet{
    public static final String DATASHEET
            = "./src/main/resources/pdfs/datasheet.pdf";
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_19_FillDataSheet.pdf";

    public static void main(String[] args) throws SQLException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_19_FillDataSheet().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws SQLException, IOException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // Get the movies
        PdfDocument pdfDocResult = new PdfDocument(new PdfWriter(dest));
        pdfDocResult.initializeOutlines();
        List<Movie> movies = PojoFactory.getMovies(connection);
        PdfReader reader;
        PdfDocument pdfDoc;
        ByteArrayOutputStream baos;
        PdfPageFormCopier formsCopier = new PdfPageFormCopier();
        // Fill out the data sheet form with data
        for (Movie movie : movies) {
            if (movie.getYear() < 2007)
                continue;
            baos = new ByteArrayOutputStream();
            pdfDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(baos));
            PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
            form.setGenerateAppearance(true);
            fill(form, movie);
            if (movie.getYear() == 2007)
                form.flattenFields();
            pdfDoc.close();

            pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
            PdfAcroForm newForm = PdfFormCreator.getAcroForm(pdfDoc, false);
            if (newForm != null) {
                // Rename fields so that fields on different pages do not share their value
                for (PdfFormField field : newForm.getAllFormFields().values()) {
                    if (field.getFieldName() != null) {
                        field.setFieldName(movie.getImdb() + field.getFieldName().toUnicodeString());
                    }
                }
            }
            pdfDoc.copyPagesTo(1, pdfDoc.getNumberOfPages(), pdfDocResult, formsCopier);
        }
        // Close the database connection
        connection.close();
        pdfDocResult.close();
    }

    /**
     * Fill out the fields using info from a Movie object.
     * @param form The form object
     * @param movie A movie POJO
     */
    public static void fill(PdfAcroForm form, Movie movie) {
        form.getField("title").setValue(movie.getMovieTitle());
        form.getField("director").setValue(getDirectors(movie));
        form.getField("year").setValue(String.valueOf(movie.getYear()));
        form.getField("duration").setValue(String.valueOf(movie.getDuration()));
        form.getField("category").setValue(movie.getEntry().getCategory().getKeyword());
        for (Screening screening : movie.getEntry().getScreenings()) {
            form.getField(screening.getLocation().replace('.', '_')).setValue("Yes");
        }
    }

    /**
     * Gets the directors from a Movie object,
     * and concatenates them in a String.
     * @param movie a Movie object
     * @return a String containing director names
     */
    public static String getDirectors(Movie movie) {
        List<Director> directors = movie.getDirectors();
        StringBuilder buf = new StringBuilder();
        for (Director director : directors) {
            buf.append(director.getGivenName());
            buf.append(' ');
            buf.append(director.getName());
            buf.append(',');
            buf.append(' ');
        }
        int i = buf.length();
        if (i > 0)
            buf.delete(i - 2, i);
        return buf.toString();
    }
}
