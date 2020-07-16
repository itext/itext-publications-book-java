package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listing_13_11_PageLabelExample {
    public static final String DEST
            = "./target/book/part4/chapter13/Listing_13_11_PageLabelExample.pdf";
    public static final String DEST2
            = "./target/book/part4/chapter13/Listing_13_11_PageLabelExample_2.pdf";
    public static final String DEST_TXT
            = "./target/book/part4/chapter13/page_labels.txt";
    public static final String[] SQL = {
            "SELECT country FROM film_country ORDER BY country",
            "SELECT name FROM film_director ORDER BY name",
            "SELECT title FROM film_movietitle ORDER BY title"
    };
    /** SQL statements */
    public static final String[] FIELD = { "country", "name", "title" };

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_13_11_PageLabelExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        createPdf(dest);
        listPageLabels(dest, DEST_TXT);
        //manipulatePageLabel(RESULT, DEST2);
    }

    /**
     * Creates a PDF document.
     * @param dest the path to the new PDF document
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String dest)
            throws IOException, SQLException{
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A5);
        int[] start = new int[3];
        pdfDoc.addNewPage();
        for (int i = 0; i < 3; i++) {
            start[i] = pdfDoc.getPageNumber(pdfDoc.getLastPage());
            addParagraphs(doc, connection, SQL[i], FIELD[i]);
            doc.add(new AreaBreak());
        }
        pdfDoc.getPage(start[0]).setPageLabel(PageLabelNumberingStyle.UPPERCASE_LETTERS, null);
        pdfDoc.getPage(start[1]).setPageLabel(PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, null);
        pdfDoc.getPage(start[2]).setPageLabel(PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, "Movies-", start[2] - start[1] + 1);
        pdfDoc.close();
        connection.close();
    }

    /**
     * Performs an SQL query and writes the results to a PDF using the Paragraph object.
     * @param doc    The document to which the paragraphs have to be added
     * @param connection  The database connection that has to be used
     * @param sql         The SQL statement
     * @param field       The name of the field that has to be shown
     * @throws SQLException
     * @throws IOException
     */
    public void addParagraphs(Document doc, DatabaseConnection connection, String sql, String field)
            throws SQLException,IOException {
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            doc.add(new Paragraph(rs.getString(field)));
        }
    }

    /**
     * Reads the page labels from an existing PDF
     * @param src  the path to an existing PDF
     * @param dest the path to the resulting text file
     * @throws IOException
     */
    public void listPageLabels(String src, String dest) throws IOException {
        // no PDF, just a text file
        PrintStream out = new PrintStream(new FileOutputStream(dest));
        PdfReader reader = new PdfReader(src);
        PdfDocument pdfDoc = new PdfDocument(reader);

        String[] pageLabels = pdfDoc.getPageLabels();
        for (String textLabel : pageLabels) {
            out.println(textLabel);
        }
        out.flush();
        out.close();
        reader.close();
    }

    /**
     * Manipulates the page labels at the lowest PDF level.
     * @param src  The path to the source file
     * @param dest The path to the changed PDF
     * @throws IOException
     */
    public void manipulatePageLabel(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfDictionary root = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary labels = root.getAsDictionary(new PdfName("PageLabels"));
        PdfArray nums = labels.getAsArray(PdfName.Nums);
        int n;
        PdfDictionary pagelabel;
        for (int i = 0; i < nums.size(); i++) {
            n = nums.getAsNumber(i).intValue();
            i++;
            if (n == 5) {
                pagelabel = nums.getAsDictionary(i);
                pagelabel.remove(PdfName.St);
                pagelabel.put(PdfName.P, new PdfString("Film-"));
            }
        }
        pdfDoc.close();
    }
}
