package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_07_07_AddVersionChecker {
    public static final String DEST
            = "./target/book/part2/chapter07/Listing_07_07_AddVersionChecker.pdf";
    public static final String RESOURCE
            = "./src/main/resources/js/viewer_version.js";

    public static final String HELLO_WORLD = "./src/main/resources/pdfs/cmp_Listing_01_01_HelloWorld.pdf";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        Listing_07_07_AddVersionChecker checker = new Listing_07_07_AddVersionChecker();
        checker.arguments = args;
        checker.manipulatePdf(DEST);

    }

    protected static String readFileToString(String path) throws IOException {
        File file = new File(path);
        byte[] jsBytes = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(jsBytes);
        return new String(jsBytes).replace("\r\n", "\n");
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Listing_01_01_HelloWorld.main(arguments);
        // Create a reader
        PdfReader reader = new PdfReader(HELLO_WORLD);
        // Create a stamper
        PdfDocument pdfDoc
                = new PdfDocument(reader, new PdfWriter(DEST));
        // Add some javascript
        pdfDoc.getCatalog()
                .setOpenAction(PdfAction.createJavaScript(readFileToString(RESOURCE)));
        // Close the pdfDocument
        pdfDoc.close();
    }
}
