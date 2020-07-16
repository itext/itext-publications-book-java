package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_07_13_EventAndActions {
    public static final String DEST = "./target/book/part2/chapter07/Listing_07_13_EventAndActions.pdf";
    public static final String RESOURCE = "./src/main/resources/js/print_page.js";

    public static final String MOVIE_TEMPLATES = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        Listing_07_13_EventAndActions application = new Listing_07_13_EventAndActions();
        application.arguments = args;
        application.manipulatePdf(DEST);
    }

    protected static String readFileToString(String path) throws IOException {
        File file = new File(path);
        byte[] jsBytes = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(jsBytes);
        return new String(jsBytes);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Listing_03_29_MovieTemplates.main(arguments);
        // Create a reader
        PdfReader reader = new PdfReader(MOVIE_TEMPLATES);
        // Create a writer
        PdfWriter writer = new PdfWriter(DEST);
        // Create a pdf document
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        int n = pdfDoc.getNumberOfPages();
        // Add some javascript
        pdfDoc.getCatalog().setOpenAction(PdfAction.createJavaScript(readFileToString(RESOURCE).replace("\r\n", "\n")));
        // Create a Chunk with a chained action
        PdfCanvas canvas;
        PdfAction action = PdfAction.createJavaScript("app.alert('Think before you print!');");
        action.next(PdfAction.createJavaScript("printCurrentPage(this.pageNum);"));
        action.next(PdfAction.createURI("http://www.panda.org/savepaper/"));
        Link link = new Link("print this page", action);
        Paragraph paragraph = new Paragraph(link);
        // Add this Paragraph to every page
        for (int i = 1; i <= n; i++) {
            canvas = new PdfCanvas(pdfDoc.getPage(i));
            new Canvas(canvas, pdfDoc.getPage(i).getPageSize())
                    .showTextAligned(paragraph, 816, 18, i,
                            TextAlignment.RIGHT, VerticalAlignment.MIDDLE, 0);
        }
        // Close the pdfDocument
        pdfDoc.close();
    }
}
