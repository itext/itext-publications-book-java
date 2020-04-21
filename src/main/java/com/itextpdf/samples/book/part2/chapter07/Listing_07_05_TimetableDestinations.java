package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Listing_07_05_TimetableDestinations {
    public static final String DEST
            = "./target/book/part2/chapter07/Listing_07_05_TimetableDestinations.pdf";
    public static final String SRC
            = "./src/main/resources/js/viewer_version.js";
    public static final String MOVIE_TEMPLATES
            = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    protected List<PdfAction> actions;
    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_07_05_TimetableDestinations().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create the pdf document
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        int n = pdfDoc.getNumberOfPages();

        // Make a list with all the possible actions
        actions = new ArrayList<PdfAction>();
        for (int i = 1; i <= n; i++) {
            actions.add(PdfAction.createGoTo(PdfExplicitDestination.createFit(pdfDoc.getPage(i))));
        }

        PdfFont symbol = PdfFontFactory.createFont(StandardFonts.SYMBOL);
        // Add a navigation table to every page
        for (int i = 1; i <= n; i++) {
            doc.add(createNavigationTable(i, n).setFixedPosition(i, 696, 0, 120).setFont(symbol));
            if (n != i) {
                doc.add(new AreaBreak());
            }
        }

        // Close the pdf document
        pdfDoc.close();
    }

    public Table createNavigationTable(int pagenumber, int total) {
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setWidth(120);
        Style style = new Style().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Link first = new Link(String.valueOf((char) 220), actions.get(0));
        table.addCell(new Cell().add(new Paragraph(first)).addStyle(style));
        Link previous = new Link(String.valueOf((char) 172), actions.get(pagenumber - 2 < 0 ? 0 : pagenumber - 2));
        table.addCell(new Cell().add(new Paragraph(previous)).addStyle(style));
        Link next = new Link(String.valueOf((char) 174), actions.get(pagenumber >= total ? total - 1 : pagenumber));
        table.addCell(new Cell().add(new Paragraph(next)).addStyle(style));
        Link last = new Link(String.valueOf((char) 222), actions.get(total - 1));
        table.addCell(new Cell().add(new Paragraph(last)).addStyle(style));
        return table;
    }
}
