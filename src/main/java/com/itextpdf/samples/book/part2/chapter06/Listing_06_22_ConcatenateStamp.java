package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_06_22_ConcatenateStamp {
    public static final String DEST =
            "./target/book/part2/chapter06/Listing_06_22_ConcatenateStamp.pdf";
    public static final String MOVIE_LINKS1 =
            "./src/main/resources/pdfs/cmp_Listing_02_22_MovieLinks1.pdf";
    public static final String MOVIE_HISTORY =
            "./src/main/resources/pdfs/cmp_Listing_02_24_MovieHistory.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_22_ConcatenateStamp().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(resultDoc);
        resultDoc.addNewPage();

        PdfDocument srcDoc1 = new PdfDocument(new PdfReader(MOVIE_LINKS1));
        int n1 = srcDoc1.getNumberOfPages();
        PdfDocument srcDoc2 = new PdfDocument(new PdfReader(MOVIE_HISTORY));
        int n2 = srcDoc2.getNumberOfPages();

        PdfPage page;
        for (int i = 1; i <= n1; i++) {
            page = resultDoc.getLastPage();
            PdfFormXObject backPage = srcDoc1.getPage(i).copyAsFormXObject(resultDoc);
            new PdfCanvas(page.newContentStreamBefore(), page.getResources(), resultDoc)
                    .addXObjectAt(backPage, 0, 0);
            doc.add(new Paragraph(String.format("page %d of %d", i, n1 + n2)).setMargin(0).setMultipliedLeading(1).setFixedPosition(297.5f, 28, 200));
            doc.add(new AreaBreak());
        }
        for (int i = 1; i <= n2; i++) {
            PdfFormXObject backPage = srcDoc2.getPage(i).copyAsFormXObject(resultDoc);
            page = resultDoc.getLastPage();
            new PdfCanvas(page.newContentStreamBefore(), page.getResources(), resultDoc)
                    .addXObjectAt(backPage, 0, 0);
            doc.add(new Paragraph(String.format("page %d of %d", i + n1, n1 + n2)).setMargin(0).setMultipliedLeading(1).setFixedPosition(297.5f, 28, 200));
            if (n2 != i) {
                doc.add(new AreaBreak());
            }
        }
        resultDoc.close();
        srcDoc1.close();
        srcDoc2.close();
    }
}
