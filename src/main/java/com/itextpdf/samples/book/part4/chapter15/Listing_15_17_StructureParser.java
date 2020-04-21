package com.itextpdf.samples.book.part4.chapter15;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Listing_15_17_StructureParser extends DefaultHandler {
    /**
     * The list of structure roles
     */
    protected List<String> roles;

    /**
     * Creates a parser that will parse an XML file into a structure tree.
     */
    public Listing_15_17_StructureParser(List<String> roles) {
        this.roles = roles;
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if ("chapter".equals(qName)) {
            return;
        }
        roles.add(qName);
    }
}
