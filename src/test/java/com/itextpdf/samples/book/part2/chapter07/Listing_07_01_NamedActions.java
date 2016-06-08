/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_07_01_NamedActions extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter07/Listing_07_01_NamedActions.pdf";

    public static final String MOVIE_TEMPLATES = "./src/test/resources/book/part1/chapter03/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        // new Listing_03_29_MovieTemplates().manipulatePdf(Listing_03_29_MovieTemplates.DEST);
        new Listing_07_01_NamedActions().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES),
                new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfFont symbol = PdfFontFactory.createFont(FontConstants.SYMBOL);

        Table table = new Table(4).
            setFont(symbol).
            setFontSize(20);
        Style cellStyle = new Style().setBorder(Border.NO_BORDER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        PdfAction action = PdfAction.createNamed(PdfName.FirstPage);
        Link first = new Link(String.valueOf((char) 220), action);
        table.addCell(new Cell()
                .add(new Paragraph().add(first))
                .addStyle(cellStyle));

        action = PdfAction.createNamed(PdfName.PrevPage);
        Link previous = new Link(String.valueOf((char) 172), action);
        table.addCell(new Cell()
                .add(new Paragraph().add(previous))
                .addStyle(cellStyle));

        action = PdfAction.createNamed(PdfName.NextPage);
        Link next = new Link(String.valueOf((char) 174), action);
        table.addCell(new Cell()
                .add(new Paragraph().add(next))
                .addStyle(cellStyle));

        action = PdfAction.createNamed(PdfName.LastPage);
        Link last = new Link(String.valueOf((char) 222), action);
        table.addCell(new Cell()
                .add(new Paragraph().add(last))
                .addStyle(cellStyle));

        // Add the table to each page
        PdfCanvas canvas;
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            table.setFixedPosition(i, 696, 0, 120);
            doc.add(table);
        }
        doc.close();
    }
}
