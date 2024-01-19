/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.IOException;

public class Listing_07_01_NamedActions {
    public static final String DEST = "./target/book/part2/chapter07/Listing_07_01_NamedActions.pdf";

    public static final String MOVIE_TEMPLATES = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_07_01_NamedActions().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES),
                new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfFont symbol = PdfFontFactory.createFont(StandardFonts.SYMBOL);

        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth().
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
