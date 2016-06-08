/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.samples.GenericTest;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_02_10_MovieChain extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part1/chapter02/Listing_02_10_MovieChain.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_02_10_MovieChain().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException, SQLException, UnsupportedEncodingException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> kubrick = PojoFactory.getMovies(connection, 1);
        connection.close();

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(240, 240));
        doc.setMargins(10, 10, 10, 10);
        // create a long Stringbuffer with movie titles
        StringBuffer buf1 = new StringBuffer();
        for (Movie movie : kubrick) {
            // replace spaces with non-breaking spaces
            buf1.append(movie.getMovieTitle().replace(' ', '\u00a0'));
            // use pipe as separator
            buf1.append('|');
        }
        Text chunk1 = new Text(buf1.toString());
        // wrap the chunk in a paragraph and add it to the document
        Paragraph paragraph = new Paragraph("A:\u00a0");
        paragraph.add(chunk1);
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        doc.add(paragraph);

        // define the pipe character as split character
        chunk1.setSplitCharacters(new PipeSplitCharacter());
        // wrap the chunk in a second paragraph and add it
        paragraph = new Paragraph("B:\u00a0");
        paragraph.add(chunk1);
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        doc.add(paragraph);

        // create a new StringBuffer with movie titles
        StringBuffer buf2 = new StringBuffer();
        for (Movie movie : kubrick) {
            buf2.append(movie.getMovieTitle());
            buf2.append('|');
        }
        // Create a second chunk
        Text chunk2 = new Text(buf2.toString());
        // wrap the chunk in a paragraph and add it to the document
        paragraph = new Paragraph("C:\u00a0");
        paragraph.add(chunk2);
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        doc.add(paragraph);

        // go to a new page
        doc.add(new AreaBreak());

        chunk2.setHyphenation(new HyphenationConfig("en", "US", 2, 2));
        // wrap the second chunk in a second paragraph and add it
        paragraph = new Paragraph("D:\u00a0");
        paragraph.add(chunk2);
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        doc.add(paragraph);

        // go to a new page
        doc.add(new AreaBreak());

        // wrap the second chunk in a third paragraph and add it
        paragraph = new Paragraph("E:\u00a0");
        paragraph.add(chunk2);
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        paragraph.setSpacingRatio(1);
        doc.add(paragraph);

        //Close document
        doc.close();
    }

}
