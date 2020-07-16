package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Paragraph;

import com.lowagie.filmfestival.Movie;
import java.io.File;

public class Listing_03_18_MovieColumns3 extends Listing_03_16_MovieColumns1 {

    public static final String DEST = "./target/book/part1/chapter03/Listing_03_18_MovieColumns3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_18_MovieColumns3().manipulatePdf(DEST);
    }

    @Override
    public Paragraph createMovieInformation(Movie movie) {
        return super.createMovieInformation(movie).
                setKeepTogether(true).
                setPaddingLeft(0).
                setFirstLineIndent(0).
                setTextAlignment(TextAlignment.LEFT);
    }
}
