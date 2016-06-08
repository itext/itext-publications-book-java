/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.filmfestival;

/**
 * POJO for an object that corresponds with a record
 * in the table festival_category.
 */
public class Category {
    
    /** The name of the category. */
    protected String name;
    /** A short keyword for the category. */
    protected String keyword;
    /** The color code of the category. */
    protected String color;
    /** The parent category (if any). */
    protected Category parent = null;
    
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
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }
    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    /**
     * @return the parent
     */
    public Category getParent() {
        return parent;
    }
    /**
     * @param parent the parent to set
     */
    public void setParent(Category parent) {
        this.parent = parent;
    }
    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }
    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
}
