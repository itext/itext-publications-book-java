/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.filmfestival;

import java.sql.Date;
import java.sql.Time;

/**
 * POJO for an object that corresponds with a record
 * in the table festival_screening.
 */
public class Screening {
    /** The date of the screening. */
    protected Date date;
    /** The time of the screening. */
    protected Time time;
    /** The location of the screening. */
    protected String location;
    /** Is this a screening for the press only? */
    protected boolean press;
    /** The movie that will be screened. */
    protected Movie movie = null;
    
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * @return the time
     */
    public Time getTime() {
        return time;
    }
    /**
     * @param time the time to set
     */
    public void setTime(Time time) {
        this.time = time;
    }
    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }
    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * @return the press
     */
    public boolean isPress() {
        return press;
    }
    /**
     * @param press the press to set
     */
    public void setPress(boolean press) {
        this.press = press;
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
    }
}
