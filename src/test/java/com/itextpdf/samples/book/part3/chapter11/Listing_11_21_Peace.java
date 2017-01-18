/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.property.Property;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.GenericTest;
import org.junit.Ignore;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;

@Ignore("DEVSIX-1032")
public class Listing_11_21_Peace extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part3/chapter11/Listing_11_21_Peace.pdf";
    
    private static final String fontsFolder = "src/test/resources/font/";
    
    private static final FontSet fontSet;
    /** Paths to and encodings of fonts we're going to use in this example */
    static {
        fontSet = new FontSet();
        fontSet.addFont(fontsFolder + "NotoSans-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansBengali-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansCanadianAboriginal-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansCherokee-Regular.ttf");
        // fontSet.addFont(fontsFolder + "NotoSansDevanagari-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansEthiopic-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansGothic-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansGurmukhi-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansKhmer-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansMyanmar-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansOldItalic-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansRunic-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansSinhala-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansSyriacEastern-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansTamil-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansTelugu-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansThaana-Regular.ttf");
        fontSet.addFont(fontsFolder + "NotoSansYi-Regular.ttf");
        fontSet.addFont(fontsFolder + "FreeSans.ttf");
    }
    
    private static final String RESOURCE = "src/test/resources/xml/peace.xml";

    public static void main(String[] args) throws Exception {
        new Listing_11_21_Peace().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Load the license file to use advanced typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setFontProvider(new FontProvider(fontSet));
        document.setProperty(Property.FONT, "Noto");
        
        Table table = new Table(3);
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
            while (buf.charAt(0) == ' ') {
                buf.deleteCharAt(0);
            }
            return buf.toString();
        }
    }
}
