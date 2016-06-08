/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter09;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Listing_09_09_CreateFDF extends HttpServlet {
    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/vnd.adobe.fdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"subscribe.fdf\"");
        // TODO DEVSIX-159
        // FdfWriter fdf = new FdfWriter();
        // fdf.setFieldAsString("personal.name", request.getParameter("name"));
        //fdf.setFieldAsString("personal.loginname", request.getParameter("loginname"));
        // fdf.setFieldAsString("personal.password", request.getParameter("password"));
        //fdf.setFieldAsString("personal.reason", request.getParameter("reason"));
        // fdf.setFile("subscribe.pdf");
        // fdf.writeTo(response.getOutputStream());
    }

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4841218549441233308L;
}