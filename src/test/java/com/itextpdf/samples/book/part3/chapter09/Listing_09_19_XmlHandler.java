/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import java.util.EmptyStackException;
import java.util.Stack;

import org.junit.Ignore;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Listing_09_19_XmlHandler extends DefaultHandler {
    /**
     * The Document to which the content is written.
     */
    protected Document document;

    /**
     * This is a <CODE>Stack</CODE> of objects, waiting to be added to the
     * document.
     */
    protected Stack<IElement> stack = new Stack<IElement>();

    /**
     * This is the current chunk to which characters can be added.
     */
    protected Text currentChunk = null;

    /**
     * Stores the year when it's encountered in the XML.
     */
    protected String year = null;
    /**
     * Stores the duration when it's encountered in the XML.
     */
    protected String duration = null;
    /**
     * Stores the imdb ID when it's encountered in the XML.
     */
    protected String imdb = null;

    /**
     * Creates a handler for an iText Document.
     *
     * @param document the document to which content needs to be added.
     */
    public Listing_09_19_XmlHandler(Document document) {
        this.document = document;
    }

    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length);
        if (content.trim().length() == 0)
            return;
        if (currentChunk == null) {
            currentChunk = new Text(content.trim());
        } else {
            (currentChunk).setText((currentChunk).getText() + " " + content.trim());
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        try {
            if ("directors".equals(qName) || "countries".equals(qName)) {
                stack.push(new List());
            } else if ("director".equals(qName)
                    || "country".equals(qName)) {
                stack.push(new ListItem());
            } else if ("movie".equals(qName)) {
                flushStack();
                Div p = new Div();
                p.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
                stack.push(p);
                year = attributes.getValue("year");
                duration = attributes.getValue("duration");
                imdb = attributes.getValue("imdb");
            } else if ("original".equals(qName)) {
                stack.push(new Div().add(new Paragraph("Original title: ")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        try {
            updateStack();
            if ("directors".equals(qName)) {
                flushStack();
                Div p = new Div().add(new Paragraph(
                        String.format("Year: %s; duration: %s; ", year, duration)));
                Link link = new Link("link to IMDB", PdfAction.createURI(
                        String.format("http://www.imdb.com/title/tt%s/", imdb)));
                p.add(new Paragraph(link));
                stack.push(p);
            } else if ("countries".equals(qName) || "title".equals(qName)) {
                flushStack();
            } else if ("original".equals(qName)
                    || "movie".equals(qName)) {
                currentChunk = new Text("");
                updateStack();
            } else if ("director".equals(qName)
                    || "country".equals(qName)) {
                ListItem listItem = (ListItem) stack.pop();
                List list = (List) stack.pop();
                list.add(listItem);
                stack.push(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If the currentChunk is not null, it is forwarded to the stack and made
     * null.
     */
    private void updateStack() {
        if (currentChunk != null) {
            IElement current;
            try {
                current = stack.pop();
                if (!(current instanceof Div) || !((Div) current).isEmpty())
                    if (current instanceof Div) {
                        ((Div) current).add(new Paragraph(" "));
                    } else if (current instanceof List) {
                        ((List) current).add(" ");
                    }
            } catch (EmptyStackException ese) {
                current = new Paragraph();
            }
            if (current instanceof Div) {
                ((Div) current).add(new Paragraph(currentChunk));
            } else if (current instanceof ListItem) {
                ((ListItem) current).add(new Paragraph(currentChunk));
            } else if (current instanceof List) {
                ((List) current).add(new ListItem(currentChunk.getText()));
            }
            stack.push(current);
            currentChunk = null;
        }
    }

    /**
     * Flushes the stack, adding all objects in it to the document.
     */
    private void flushStack() {
        try {
            while (stack.size() > 0) {
                IElement element = stack.pop();
                try {
                    IElement previous = stack.pop();
                    if (previous instanceof Div) {
                        ((Div)previous).add(((List)element));
                    } else if (previous instanceof ListItem) {
                        ((ListItem) previous).add((BlockElement) element);
                    }
                    stack.push(previous);
                } catch (EmptyStackException es) {
                    document.add((BlockElement) element);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
