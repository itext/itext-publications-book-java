/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter03;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_03_29_MovieTemplates extends Listing_03_15_MovieCalendar {
    public static final String DEST = "./target/test/resources/book/part1/chapter03/Listing_03_29_MovieTemplates.pdf";
    public static final String RESOURCE = "src/test/resources/img/posters/%s.jpg";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_03_29_MovieTemplates().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize doc and add page
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        doc.setProperty(Property.FONT, font);

        Text press = new Text("P").
                setFont(font).
                setFontSize(HEIGHT_LOCATION / 2).
                setFontColor(Color.WHITE);

        try {
            DatabaseConnection connection = new HsqldbConnection("filmfestival");
            locations = PojoFactory.getLocations(connection);

            // Initialize form XObject and write to it
            PdfFormXObject t_under = new PdfFormXObject(new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth()));
            PdfFormXObject t_over = new PdfFormXObject(new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth()));
            PdfCanvas over = new PdfCanvas(t_over, pdfDoc);
            PdfCanvas under = new PdfCanvas(t_under, pdfDoc);
            drawTimeTable(under);
            drawTimeSlots(over);

            List<Date> days = PojoFactory.getDays(connection);
            List<Screening> screenings;
            PdfPage page;
            int d = 1;
            for (Date day : days) {
                page = pdfDoc.addNewPage();
                if (d != 1) {
                    doc.add(new AreaBreak());
                }
                new PdfCanvas(pdfDoc.getLastPage()).addXObject(t_under, 0, 0);
                new PdfCanvas(pdfDoc.getLastPage()).addXObject(t_over, 0, 0);

                over = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
                under = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

                drawInfo(doc);
                drawDateInfo(day, d++, doc);
                screenings = PojoFactory.getScreenings(connection, day);
                for (Screening screening : screenings) {
                    drawBlock(screening, under, over);
                    drawMovieInfo(screening, doc, press);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            doc.add(new Paragraph("Database error: " + sqle.getMessage()));
        }
        //Close doc
        doc.close();
    }
}
