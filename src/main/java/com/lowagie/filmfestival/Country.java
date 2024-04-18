package com.lowagie.filmfestival;

/**
 * POJO for an object that corresponds with a record
 * in the table film_country.
 */
public class Country {
    /** The name of a country. */
    protected String country;

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
