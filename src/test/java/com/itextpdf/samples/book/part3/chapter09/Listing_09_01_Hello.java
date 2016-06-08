/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class Listing_09_01_Hello extends HttpServlet {
    /**
     * A simple Hello World Servlet.
     *
     * @see HttpServlet#doGet(
     *HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Hello World"));
        doc.add(new Paragraph(new Date().toString()));
        doc.close();
    }

    /**
     * A serial version uid
     */
    private static final long serialVersionUID = 4262544639420765610L;
}
