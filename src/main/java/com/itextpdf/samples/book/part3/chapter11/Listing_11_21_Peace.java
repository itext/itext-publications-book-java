package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;

public class Listing_11_21_Peace {

    public static final String DEST = "./target/book/part3/chapter11/Listing_11_21_Peace.pdf";

    private static final String fontsFolder = "src/main/resources/font/listing_11_21_fonts/";

    private static final FontSet fontSet;
    /** Paths to and encodings of fonts we're going to use in this example */
    static {
        fontSet = new FontSet();
        fontSet.addDirectory(fontsFolder);
    }

    private static final String RESOURCE = "src/main/resources/xml/peace.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_21_Peace().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        //Load the license file to use advanced typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setFontProvider(new FontProvider(fontSet));
        document.setFontFamily("Noto Sans");

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(new InputSource(new FileInputStream(RESOURCE)), new CustomHandler(table));
        document.add(table);
        //Close document
        document.close();
    }

    private static class CustomHandler extends DefaultHandler {

        private StringBuilder buf = new StringBuilder();
        private Table tab;

        public CustomHandler(Table t) throws IOException {
            tab = t;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes att) {
            if (null != qName && "pace".equals(qName)) {
                tab.addCell(att.getValue("language"));
                tab.addCell(att.getValue("countries"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (null != qName && "pace".equals(qName)) {
                Paragraph para = new Paragraph();
                para.add(strip(buf));
                tab.addCell(para);
                buf = new StringBuilder();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }

        private static String strip(StringBuilder buf) {
            int pos;
            while ((pos = buf.indexOf("\n")) != -1) {
                buf.replace(pos, pos + 1, " ");
            }
            return buf.toString().trim();
        }
    }
}
