package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Listing_06_16_InsertPages {
    public static final String DEST_TEMP =
            "./target/book/part2/chapter06/Listing_06_16_InsertPages.pdf";
    // Notice that we will compare via CompareTool with the reordered document
    public static final String DEST =
            "./target/book/part2/chapter06/Listing_06_16_InsertPages_reordered.pdf";
    public static final String STAMP_STATIONERY =
            "./src/main/resources/pdfs/cmp_Listing_06_15_StampStationery.pdf";
    public static final String STATIONERY_WATERMARK =
            "./src/main/resources/pdfs/cmp_Listing_06_08_Stationery_watermark.pdf";


    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_16_InsertPages().manipulatePdf(DEST_TEMP);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        changePdf(STAMP_STATIONERY, DEST_TEMP);

        PdfDocument resultDoc = new PdfDocument(new PdfWriter(DEST));
        resultDoc.initializeOutlines();

        PdfDocument srcDoc = new PdfDocument(new PdfReader(DEST_TEMP));
        List<Integer> pageNumbers = new ArrayList<>();
        pageNumbers.add(srcDoc.getNumberOfPages()-1);
        pageNumbers.add(srcDoc.getNumberOfPages());
        for (int i = 1; i <= srcDoc.getNumberOfPages()-2; i++) {
            pageNumbers.add(i);
        }
        srcDoc.copyPagesTo(pageNumbers, resultDoc);
        resultDoc.close();
        srcDoc.close();
    }

    public void changePdf(String src, String dest) throws SQLException, IOException {
        // Create a database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
                "SELECT country, id FROM film_country ORDER BY country");

        // Create a reader for the original document and for the stationery
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfDocument pdfDocToInsert = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdfDocToInsert);
        pdfDocToInsert.addEventHandler(PdfDocumentEvent.START_PAGE, new StationeryHandler(STATIONERY_WATERMARK));

        // One can make document renderer in order to set area,
        // but in a such simple case it looks unreasonable
        doc.setTopMargin(72);
        while (rs.next()) {
            doc.add(new Paragraph(rs.getString("country")).setFontSize(24));
        }
        doc.close();

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfDocument srcDoc = new PdfDocument(new PdfReader(
                new RandomAccessSourceFactory().createSource(stream.toByteArray()), new ReaderProperties()));
        srcDoc.copyPagesTo(1, srcDoc.getNumberOfPages(), pdfDoc);

        pdfDoc.close();
        srcDoc.close();
        stm.close();
        connection.close();

    }


    public class StationeryHandler implements IEventHandler {
        protected PdfDocument stationery;

        public StationeryHandler(String src) {
            try {
                stationery = new PdfDocument(new PdfReader(src));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;

            PdfFormXObject page = null;
            try {
                page = stationery.getPage(1).copyAsFormXObject(docEvent.getDocument());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new PdfCanvas(docEvent.getPage().newContentStreamBefore(),
                    docEvent.getPage().getResources(),
                    docEvent.getDocument())
                    .addXObjectAt(page, 0, 0);
        }
    }
}
