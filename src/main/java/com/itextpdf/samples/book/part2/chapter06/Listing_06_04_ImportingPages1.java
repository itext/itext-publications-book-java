package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_06_04_ImportingPages1 {
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_04_ImportingPages1.pdf";
    public static final String MOVIE_TEMPLATES
            = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_04_ImportingPages1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize destination document
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(resultDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        PdfDocument srcDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES));

        for (int i = 1; i <= srcDoc.getNumberOfPages(); i++) {
            PdfFormXObject header = srcDoc.getPage(i).copyAsFormXObject(resultDoc);
            Cell cell = new Cell().add(new Image(header).setWidth(UnitValue.createPercentValue(100)).setAutoScale(true));
            table.addCell(cell);
        }

        doc.add(table);

        resultDoc.close();
        srcDoc.close();
    }
}
