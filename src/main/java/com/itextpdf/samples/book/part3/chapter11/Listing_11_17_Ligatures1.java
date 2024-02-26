package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class Listing_11_17_Ligatures1 {
    public static final String DEST
            = "./target/book/part3/chapter11/Listing_11_17_Ligatures1.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_17_Ligatures1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(/*"c:/windows/fonts/arial.ttf"*/"./src/main/resources/font/FreeSans.ttf",
                PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
        document.add(new Paragraph("Movie title: Love at First Hiccough (Denmark)").setFont(font));
        document.add(new Paragraph("directed by Tomas Villum Jensen").setFont(font));
        document.add(new Paragraph("K\u00e6rlighed ved f\u00f8rste hik").setFont(font));
        document.add(new Paragraph(ligaturize("Kaerlighed ved f/orste hik")).setFont(font));

        //Close document
        document.close();
    }

    /**
     * Method that makes the ligatures for the combinations 'a' and 'e'
     * and for '/' and 'o'.
     *
     * @param s a String that may have the combinations ae or /o
     * @return a String where the combinations are replaced by a unicode character
     */
    public String ligaturize(String s) {
        int pos;
        while ((pos = s.indexOf("ae")) > -1) {
            s = s.substring(0, pos) + '\u00e6' + s.substring(pos + 2);
        }
        while ((pos = s.indexOf("/o")) > -1) {
            s = s.substring(0, pos) + '\u00f8' + s.substring(pos + 2);
        }
        return s;
    }
}
