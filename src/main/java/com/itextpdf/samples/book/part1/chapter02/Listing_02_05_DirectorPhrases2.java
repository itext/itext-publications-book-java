package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listing_02_05_DirectorPhrases2 {
    public static final String DEST =
            "./target/book/part1/chapter02/Listing_02_05_DirectorPhrases2.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_02_05_DirectorPhrases2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont timesbd = null;
        PdfFont times = null;
        try {
            // create a font that will be embedded
            timesbd = PdfFontFactory.createFont(
                    /*"c:/windows/fonts/timesbd.ttf"*/"./src/main/resources/font/FreeSans.ttf", PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
            // create a font that will be embedded
            times = PdfFontFactory.createFont(
                    /*"c:/windows/fonts/times.ttf"*/"./src/main/resources/font/FreeSans.ttf", PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // create the statement
        Statement stm = connection.createStatement();
        // execute the query
        ResultSet rs = stm.executeQuery("SELECT name, given_name FROM film_director ORDER BY name, given_name");
        // loop over the results
        while (rs.next()) {
            doc.add(createDirectorPhrase(rs, timesbd, times));
        }
        stm.close();
        connection.close();
        doc.close();
    }

    public Paragraph createDirectorPhrase(ResultSet rs, PdfFont timesbd, PdfFont times)
            throws SQLException, IOException {
        Paragraph director = new Paragraph();
        Text name = new Text(rs.getString("name")).setFont(timesbd).setBold();
        name.setUnderline(0.2f, -2f);
        director.add(name);
        director.add(new Text(",").setFont(timesbd));
        director.add(new Text(" ").setFont(times));
        director.add(new Text(rs.getString("given_name")).setFont(times));
        director.setFixedLeading(24);
        return director;
    }
}
