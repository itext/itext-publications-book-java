package com.lowagie.filmfestival;

/**
 * POJO for an object that corresponds with a record
 * in the table film_director.
 */
public class Director {
    
    /** The family name of the director. */
    protected String name;
    /** The given name of the director. */
    protected String givenName;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the givenName
     */
    public String getGivenName() {
        return givenName;
    }
    /**
     * @param givenName the givenName to set
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
}
