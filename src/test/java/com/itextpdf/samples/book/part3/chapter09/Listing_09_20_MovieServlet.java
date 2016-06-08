/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Listing_09_20_MovieServlet extends HttpServlet {

    /**
     * Reads an XML file and serves it as PDF to the browser.
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
            Document doc = new Document(pdfDoc);
            InputStream is = getServletContext().getResourceAsStream("/movies.xml");
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(is), new Listing_09_19_XmlHandler(doc));
            doc.close();
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -6015985931115167175L;

}
