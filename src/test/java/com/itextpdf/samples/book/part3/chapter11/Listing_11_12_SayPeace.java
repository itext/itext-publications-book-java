/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Category(SampleTest.class)
public class Listing_11_12_SayPeace extends GenericTest {

    public static final String DEST = "./target/test/resources/book/part3/chapter11/Listing_11_12_SayPeace.pdf";
    private static final String FONT = "src/test/resources/font/FreeSans.ttf";
    private static final String ARABIC_FONT = "src/test/resources/font/NotoNaskhArabic-Regular.ttf";
    private static final String RESOURCE = "src/test/resources/xml/say_peace.xml";

    public static void main(String[] args) throws Exception {
        new Listing_11_12_SayPeace().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Load the license file to use advanced typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc, PageSize.A4);

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(new InputSource(new FileInputStream(RESOURCE)), new CustomHandler(document));

        //Close document
        document.close();
    }

    private static class CustomHandler extends DefaultHandler {

        /** The StringBuffer that holds the characters. */
        protected StringBuffer buf = new StringBuffer();

        /** The current cell. */
        protected Cell cell;

        /** The table that holds the text. */
        protected Table table;

        protected Document document;

        protected PdfFont f;
        protected PdfFont arabicF;

        public CustomHandler(Document document) {
            this.document = document;
            try {
                this.f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
                this.arabicF = PdfFontFactory.createFont(ARABIC_FONT, PdfEncodings.IDENTITY_H, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            if ("message".equals(qName)) {
                buf = new StringBuffer();
                cell = new Cell();
                cell.setBorder(Border.NO_BORDER);
                if ("RTL".equals(attributes.getValue("direction"))) {
                    cell.setBaseDirection(BaseDirection.RIGHT_TO_LEFT).
                            setTextAlignment(TextAlignment.RIGHT).
                            setFont(f);
                } else {
                    cell.setFont(f);
                }
            }
            else if ("pace".equals(qName)) {
                table = new Table(1);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
         *      java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if ("big".equals(qName)) {
                String txt = strip(buf);
                Text bold = new Text(txt);
                if (isArabic(txt)) {
                    bold.setFontScript(Character.UnicodeScript.ARABIC);
                    bold.setFont(arabicF);
                }

                bold.setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE).
                        setStrokeWidth(0.5f).
                        setStrokeColor(DeviceGray.BLACK);
                Paragraph p = new Paragraph(bold);
                cell.add(p);
                buf = new StringBuffer();
            }
            else if ("message".equals(qName)) {
                String txt = strip(buf);
                Paragraph p = new Paragraph(strip(buf));
                if (isArabic(txt)) {
                    p.setFontScript(Character.UnicodeScript.ARABIC);
                    p.setFont(arabicF);
                }
                cell.add(p);
                table.addCell(cell);
                buf = new StringBuffer();
            }
            else if ("pace".equals(qName)) {
                    document.add(table);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }

        /**
         * Replaces all the newline characters by a space.
         *
         * @param buf
         *            the original StringBuffer
         * @return a String without newlines
         */
        protected String strip(StringBuffer buf) {
            int pos;
            while ((pos = buf.indexOf("\n")) != -1)
                buf.replace(pos, pos + 1, " ");
            while (buf.charAt(0) == ' ')
                buf.deleteCharAt(0);
            return buf.toString();
        }
    }

    private static boolean isArabic(String text) {
        Pattern pattern = Pattern.compile("\\p{InArabic}");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

}
