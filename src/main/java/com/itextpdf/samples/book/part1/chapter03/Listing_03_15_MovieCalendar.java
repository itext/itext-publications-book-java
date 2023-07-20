/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import com.lowagie.filmfestival.Screening;
import java.io.File;

public class Listing_03_15_MovieCalendar extends Listing_03_11_MovieTextInfo {

    public static final String DEST = "./target/book/part1/chapter03/Listing_03_15_MovieCalendar.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_03_15_MovieCalendar().manipulatePdf(DEST);
    }

    @Override
    protected void drawMovieInfo(Screening screening, Document doc, Text press) {
        super.drawMovieInfo(screening, doc, press);
        Rectangle rect = getPosition(screening);

        Paragraph p = new Paragraph().add(screening.getMovie().getMovieTitle()).
                setFixedPosition(rect.getX(), rect.getY(), rect.getWidth()).
                setHeight(rect.getHeight()).
                setTextAlignment(TextAlignment.CENTER);
        doc.add(p);
    }
}
