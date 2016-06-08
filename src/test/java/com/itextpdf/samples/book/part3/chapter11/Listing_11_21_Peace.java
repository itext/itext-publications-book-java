/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.samples.GenericTest;


import org.junit.Ignore;

@Ignore("Font selector not implemented")
public class Listing_11_21_Peace extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part3/chapter11/Listing_11_21_Peace.pdf";
    /** Paths to and encodings of fonts we're going to use in this example */
    public static String[][] FONTS = {
            {"c:/windows/fonts/arialuni.ttf", PdfEncodings.IDENTITY_H},
            {"resources/fonts/abserif4_5.ttf", PdfEncodings.IDENTITY_H},
            {"resources/fonts/damase.ttf", PdfEncodings.IDENTITY_H},
            {"resources/fonts/fsex2p00_public.ttf", PdfEncodings.IDENTITY_H}
    };
    private static final String RESOURCE = "src/test/resources/xml/peace.xml";

    public static void main(String[] args) throws Exception {
        new Listing_11_21_Peace().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {

    }
}
