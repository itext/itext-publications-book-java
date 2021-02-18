package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.List;

public class Listing_15_18_ContentParser extends DefaultHandler {
    protected StringBuffer buf = new StringBuffer();
    protected Document doc;
    protected List<String> roles;
    protected PdfFont font;
    protected String role;

    public Listing_15_18_ContentParser(Document doc, List<String> roles)
            throws IOException {
        this.doc = doc;
        this.roles = roles;

        font = PdfFontFactory.createFont(/*"c:/windows/fonts/arial.ttf"*/"./src/main/resources/font/FreeSans.ttf",
                PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        for (int i = start; i < start + length; i++) {
            if (ch[i] == '\n')
                buf.append(' ');
            else
                buf.append(ch[i]);
        }
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if ("chapter".equals(qName)) {
            return;
        }
        role = roles.get(0);
        roles.remove(0);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("chapter".equals(qName)) {
            return;
        }
        String s = buf.toString().trim();
        buf = new StringBuffer();
        if (s.length() > 0) {
            Paragraph p = new Paragraph(s).setFont(font);
            p.getAccessibilityProperties().setRole(role);
            p.setTextAlignment(TextAlignment.JUSTIFIED);
            doc.add(p);
        }
    }
}
