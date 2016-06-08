/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.action.PdfTargetDictionary;
import com.itextpdf.kernel.pdf.collection.*;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.navigation.PdfNamedDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_16_09_KubrickMovies extends GenericTest {
    public static final String FILENAME = "Listing_16_09_KubrickMovies.pdf";
    public static final String DEST = "./target/test/resources/book/part4/chapter16/" + FILENAME;
    public static final String RESOURCE_FILES = "./src/test/resources/pdfs/%s.pdf";
    public static final String RESOURCE = "./src/test/resources/img/posters/%s.jpg";
    public static final float[] WIDTHS = {50, 350};

    public static void main(String args[]) throws Exception {
        new Listing_16_08_KubrickBox().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        FileOutputStream os = new FileOutputStream(DEST);
        os.write(createPdf());
        os.flush();
        os.close();
    }

    /**
     * Creates a Collection schema that can be used to organize the movies of Stanley Kubrick
     * in a collection: year, title, duration, DVD acquisition, filesize (filename is present, but hidden).
     *
     * @return a collection schema
     */
    private PdfCollectionSchema getCollectionSchema() {
        PdfCollectionSchema schema = new PdfCollectionSchema();

        PdfCollectionField size = new PdfCollectionField("File size", PdfCollectionField.SIZE);
        size.setOrder(4);
        schema.addField("SIZE", size);

        PdfCollectionField filename = new PdfCollectionField("File name", PdfCollectionField.FILENAME);
        filename.setVisibility(false);
        schema.addField("FILE", filename);

        PdfCollectionField title = new PdfCollectionField("Movie title", PdfCollectionField.TEXT);
        title.setOrder(1);
        schema.addField("TITLE", title);

        PdfCollectionField duration = new PdfCollectionField("Duration", PdfCollectionField.NUMBER);
        duration.setOrder(2);
        schema.addField("DURATION", duration);

        PdfCollectionField year = new PdfCollectionField("Year", PdfCollectionField.NUMBER);
        year.setOrder(0);
        schema.addField("YEAR", year);

        return schema;
    }

    public byte[] createPdf() throws IOException, SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("This document contains a collection of PDFs, one per Stanley Kubrick movie."));

        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.DETAILS);
        PdfCollectionSchema schema = getCollectionSchema();
        collection.setSchema(schema);
        PdfCollectionSort sort = new PdfCollectionSort("YEAR");
        sort.setSortOrder(false);
        collection.setSort(sort);
        collection.setInitialDocument("Eyes Wide Shut");
        pdfDoc.getCatalog().setCollection(collection);

        PdfFileSpec fs;
        PdfCollectionItem item;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        java.util.List<Movie> movies = PojoFactory.getMovies(connection, 1);
        connection.close();
        for (Movie movie : movies) {
            fs = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, String.format(RESOURCE_FILES, movie.getImdb()),
                    movie.getTitle(),
                    String.format("kubrick_%s.pdf", movie.getImdb()),
                    null, null, true);

            item = new PdfCollectionItem(schema);
            item.addItem("TITLE", movie.getMovieTitle(false));
            if (movie.getMovieTitle(true) != null) {
                 item.setPrefix("TITLE", movie.getMovieTitle(true));
            }
            item.addItem("DURATION", new PdfNumber(movie.getDuration()));
            item.addItem("YEAR", new PdfNumber(movie.getYear()));
            fs.setCollectionItem(item);
            pdfDoc.addFileAttachment(movie.getTitle(), fs);
        }
        doc.close();
        return baos.toByteArray();
    }

    public byte[] createMoviePage(Movie movie) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        Paragraph p = new Paragraph(movie.getMovieTitle())
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA, PdfEncodings.WINANSI, false)).setFontSize(16);
        doc.add(p);
        doc.add(new Paragraph("\n"));
        Table table = new Table(WIDTHS);
        table.addCell(new Cell().add(new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb())))
                .setAutoScale(true)));
        Cell cell = new Cell();
        cell.add(new Paragraph("Year: " + movie.getYear()));
        cell.add(new Paragraph("Duration: " + movie.getDuration()));
        table.addCell(cell);
        doc.add(table);
        PdfTargetDictionary target = new PdfTargetDictionary(PdfName.P);
        target.setTarget(new PdfTargetDictionary(PdfName.P));
        Link link = new Link("Go to original document",
                PdfAction.createGoToE(new PdfNamedDestination("movies"), false, target));
        // TODO
        doc.add(new Paragraph(link));
        doc.close();
        return baos.toByteArray();
    }
}
