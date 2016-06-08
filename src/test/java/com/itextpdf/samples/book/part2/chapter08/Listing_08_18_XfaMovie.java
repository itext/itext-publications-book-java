/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.*;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Category(SampleTest.class)
public class Listing_08_18_XfaMovie extends GenericTest {
    /** The original PDF. */
    public static final String RESOURCE = "./src/test/resources/pdfs/xfa_movie.pdf";
    /** XML making up an XFA form we want to put inside an existing PDF. */
    public static final String RESOURCEXFA = "./src/test/resources/xml/xfa.xml";
    /** Shows information about a form that has an AcroForm and an XFA stream. */
    public static final String RESULTTXT1 = "./target/test/resources/book/part2/chapter08/movie_xfa.txt";
    /** Shows information about a form that has an AcroForm after the XFA stream was removed. */
    public static final String RESULTTXT2 = "./target/test/resources/book/part2/chapter08/movie_acroform.txt";
    /** The XML making up the XFA form in the original PDF */
    public static final String RESULTXML = "./target/test/resources/book/part2/chapter08/movie_xfa.xml";
    /** The XML making up the XFA form in a PDF that was filled out using iText */
    public static final String RESULTXMLFILLED = "./target/test/resources/book/part2/chapter08/movie_filled.xml";
    /** The XML data taken from an XFA form that was filled out using iText. */
    public static final String RESULTDATA = "./target/test/resources/book/part2/chapter08/movie.xml";
    /** The resulting PDF. */
    public static final String[] RESULT = {
            "./target/test/resources/book/part2/chapter08/Listing_08_18_XfaMovie_xfa_filled_1.pdf",
            "./target/test/resources/book/part2/chapter08/Listing_08_18_XfaMovie_xfa_filled_2.pdf",
            "./target/test/resources/book/part2/chapter08/Listing_08_18_XfaMovie_xfa_filled_3.pdf"
    };
    /** PDFs to compare with. */
    public static final String[] CMP_RESULT = {
            "./src/test/resources/book/part2/chapter08/cmp_Listing_08_18_XfaMovie_xfa_filled_1.pdf",
            "./src/test/resources/book/part2/chapter08/cmp_Listing_08_18_XfaMovie_xfa_filled_2.pdf",
            "./src/test/resources/book/part2/chapter08/cmp_Listing_08_18_XfaMovie_xfa_filled_3.pdf"
    };
    
    /**
     * Checks if a PDF containing an interactive form uses
     * AcroForm technology, XFA technology, or both.
     * Also lists the field names.
     * @param src the original PDF
     * @param dest a text file containing form info.
     * @throws IOException
     */
    public void readFieldnames(String src, String dest) throws IOException {
        PrintStream out = new PrintStream(new FileOutputStream(dest));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        XfaForm xfa = form.getXfaForm();
        out.println(xfa.isXfaPresent() ? "XFA form" : "AcroForm");
        Set<String> fields = form.getFormFields().keySet();
        for (String key : fields) {
            out.println(key);
        }
        out.flush();
        out.close();
        pdfDoc.close();
    }

    /**
     * Reads the XML that makes up an XFA form.
     * @param src the original PDF file
     * @param dest the resulting XML file
     * @throws IOException

     */
    public void readXfa(String src, String dest)
            throws IOException, TransformerException {
        FileOutputStream os = new FileOutputStream(dest);
        PdfDocument pdfDoc = new PdfDocument( new PdfReader(src));
        XfaForm xfa = new XfaForm(pdfDoc);
        Document doc = xfa.getDomDocument();
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(doc), new StreamResult(os));
        pdfDoc.close();
    }

    /**
     * Fill out a form the "traditional way".
     * Note that not all fields are correctly filled in because of the way the form was created.
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     */
    public void fillData1(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.getField("movies[0].movie[0].imdb[0]").setValue("1075110");
        form.getField("movies[0].movie[0].duration[0]").setValue("108");
        form.getField("movies[0].movie[0].title[0]").setValue("The Misfortunates");
        form.getField("movies[0].movie[0].original[0]").setValue("De helaasheid der dingen");
        form.getField("movies[0].movie[0].year[0]").setValue("2009");
        pdfDoc.close();
    }

    /**
     * Fills out a form by replacing the XFA stream.
     * @param src the original PDF
     * @param xml the XML making up the new form
     * @param dest the resulting PDF
     * @throws IOException

     */
    public void fillData2(String src, String xml, String dest)
            throws IOException, SAXException, ParserConfigurationException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        XfaForm xfa = new XfaForm(new FileInputStream(xml));
        xfa.write(pdfDoc);
        pdfDoc.close();
    }

    /**
     * Reads the data from a PDF containing an XFA form.
     * @param src the original PDF
     * @param dest the data in XML format
     * @throws IOException
     * @throws TransformerException
     */
    public void readData(String src, String dest)
            throws IOException, TransformerException {
        FileOutputStream os = new FileOutputStream(dest);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        XfaForm xfa = new XfaForm(pdfDoc);
        Node node = xfa.getDatasetsNode();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if("data".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if("movies".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(node), new StreamResult(os));
        pdfDoc.close();
    }

    /**
     * Fills out a PDF form, removing the XFA.
     * @param src
     * @param dest
     * @throws IOException
     */
    public void fillData3(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.removeXfaForm();
        form.getField("movies[0].movie[0].imdb[0]").setValue("1075110");
        form.getField("movies[0].movie[0].duration[0]").setValue("108");
        form.getField("movies[0].movie[0].title[0]").setValue("The Misfortunates");
        form.getField("movies[0].movie[0].original[0]").setValue("De helaasheid der dingen");
        form.getField("movies[0].movie[0].year[0]").setValue("2009");
        pdfDoc.close();
    }

    public static void main(String[] args) throws Exception {
        new Listing_08_18_XfaMovie().manipulatePdf(RESULT[0]);
    }

    protected void manipulatePdf(String dest) throws Exception {
        readFieldnames(RESOURCE, RESULTTXT1);
        readXfa(RESOURCE, RESULTXML);
        fillData1(RESOURCE, RESULT[0]);
        readXfa(RESULT[0], RESULTXMLFILLED);
        fillData2(RESOURCE, RESOURCEXFA, RESULT[1]);
        readData(RESULT[1], RESULTDATA);
        fillData3(RESOURCE, RESULT[2]);
        readFieldnames(RESULT[2], RESULTTXT2);
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        String outPath;
        for (int i = 0; i < RESULT.length; i++) {
            outPath = new File(RESULT[i]).getParent();
            if (compareXml) {
                if (!compareTool.compareXmls(RESULT[i], CMP_RESULT[i])) {
                    addError("The XML structures are different.");
                }
            } else {
                if (compareRenders) {
                    addError(compareTool.compareVisually(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                    addError(compareTool.compareLinkAnnotations(dest, cmp));
                } else {
                    addError(compareTool.compareByContent(RESULT[i], CMP_RESULT[i], outPath, differenceImagePrefix));
                }
                addError(compareTool.compareDocumentInfo(RESULT[i], CMP_RESULT[i]));
            }
        }
        if (errorMessage != null) Assert.fail(errorMessage);
    }
    
    @Override
    protected String getDest() {
        return RESULT[0];
    }
}
