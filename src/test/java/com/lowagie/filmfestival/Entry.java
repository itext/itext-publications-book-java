/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.filmfestival;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO for an object that corresponds with a record
 * in the table festival_entry.
 */
public class Entry {
    
    /** The festival year. */
    protected int year;
    /** The movie. */
    protected Movie movie;
    /** The category. */
    protected Category category;
    /** The screenings. */
    protected List<Screening> screenings = new ArrayList<>();
    
    /**
     * Adds a screening to this entry.
     */
    public void addScreening(Screening screening) {
        screenings.add(screening);
    }
    
    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }
    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }
    /**
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }
    /**
     * @param movie the movie to set
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
        if (movie.getEntry() == null)
            movie.setEntry(this);
    }
    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }
    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }
    /**
     * @return the screenings
     */
    public List<Screening> getScreenings() {
        return screenings;
    }
}
