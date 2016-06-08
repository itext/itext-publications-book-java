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
 * in the table film_movietitle.
 */
public class Movie implements Comparable<Movie> {
    
    /** The title of the movie. */
    protected String title;
    /** The original title (if different from title). */
    protected String originalTitle = null;
    /** Code used by IMDB */
    protected String imdb;
    /** The year the movie was released. */
    protected int year;
    /** The duration of the movie in minutes. */
    protected int duration;
    /** The list of directors. */
    protected List<Director> directors = new ArrayList<>();
    /** The list of countries. */
    protected List<Country> countries= new ArrayList<>();
    /** The filmfestival entry info. */
    protected Entry entry = null;
    
    /**
     * Adds a director.
     * @param director one of the directors of the movie
     */
    public void addDirector(Director director) {
        directors.add(director);
    }
    
    /**
     * Adds a country.
     * @param country  one of the countries the movie was made by.
     */
    public void addCountry(Country country) {
        countries.add(country);
    }
    
    /**
     * Returns the title in the correct form.
     * @return a title
     */
    public String getMovieTitle() {
        if (title.endsWith(", A"))
            return "A " + title.substring(0, title.length() - 3);
        if (title.endsWith(", The"))
            return "The " + title.substring(0, title.length() - 5);
        return title;
    }
    
    /**
     * Returns the title in the correct form.
     * @return a title
     */
    public String getMovieTitle(boolean prefix) {
        if (title.endsWith(", A"))
        	if (prefix)
        		return "A ";
        	else
        		return title.substring(0, title.length() - 3);
        if (title.endsWith(", The"))
        	if (prefix)
        		return "The ";
        	else
        		return title.substring(0, title.length() - 5);
        if (prefix)
        	return null;
        else
        	return title;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }
    /**
     * @param originalTitle the originalTitle to set
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    /**
     * @return the imdb
     */
    public String getImdb() {
        return imdb;
    }
    /**
     * @param imdb the imdb to set
     */
    public void setImdb(String imdb) {
        this.imdb = imdb;
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
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }
    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
    /**
     * @return the directors
     */
    public List<Director> getDirectors() {
        return directors;
    }
    /**
     * @return the countries
     */
    public List<Country> getCountries() {
        return countries;
    }
    /**
     * @return the entry
     */
    public Entry getEntry() {
        return entry;
    }
    /**
     * @param entry the entry to set
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
        if (entry.getMovie() == null)
            entry.setMovie(this);
    }

	public int compareTo(Movie o) {
		return title.compareTo(o.title);
	}
    
}
