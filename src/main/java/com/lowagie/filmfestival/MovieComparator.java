/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.filmfestival;

import java.util.Comparator;

/**
 * Compares movies based on their title or the year, they're made in.
 */
public class MovieComparator implements Comparator<Movie> {

    /** Type for movies have to be sorted by title. */
    public static int BY_TITLE = 0;
    /** Type for movies have to be sorted by production year. */
    public static int BY_YEAR = 1;
    
    /** The type; can be BY_TITLE or BY_YEAR. */
    protected int type;
    
    /**
     * Creates an instance of the MovieComparator.
     * @param type defines how the movies will be sorted;
     * can be BY_TITLE or BY_YEAR
     */
    public MovieComparator(int type) {
        this.type = type;
    }

    /**
     * @see Comparator#compare(Object, Object)
     */
    public int compare(Movie m1, Movie m2) {
        if (type == BY_YEAR) {
            int c = m1.getYear() - m2.getYear();
            if (c != 0)
                return c;
        }
        return m1.getTitle().compareTo(m2.getTitle());
    }

}