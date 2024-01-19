/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part3.chapter09;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Servlet implementation class ShowData
 */
public class Listing_09_07_ShowData extends HttpServlet {
    /**
     * Show keys and values passed to the query string with GET
     * as plain text.
     *
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Enumeration<String> parameters = request.getParameterNames();
        String parameter;
        while (parameters.hasMoreElements()) {
            parameter = parameters.nextElement();
            out.println(
                    String.format("%s: %s", parameter, request.getParameter(parameter)));
        }
    }

    /**
     * Shows the stream passed to the server with POST as plain text.
     *
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        OutputStream os = response.getOutputStream();
        InputStream is = request.getInputStream();
        byte[] b = new byte[256];
        int read;
        while ((read = is.read(b)) != -1) {
            os.write(b, 0, read);
        }
    }

    /**
     * Serial version UID
     */
}
