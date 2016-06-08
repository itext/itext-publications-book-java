/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.kernel.pdf.PdfName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class Listing_15_17_StructureParser extends DefaultHandler {
    /**
     * The list of structure roles
     */
    protected List<PdfName> roles;

    /**
     * Creates a parser that will parse an XML file into a structure tree.
     */
    public Listing_15_17_StructureParser(List<PdfName> roles) {
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
        roles.add(new PdfName(qName));
    }
}
