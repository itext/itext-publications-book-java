/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

@Category(SampleTest.class)
public class Listing_07_29_Calculator extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter07/Listing_07_29_Calculator.pdf";

    /**
     * Path to the resource.
     */
    public static final String RESOURCE = "./src/test/resources/js/calculator.js";

    /**
     * Position of the digits
     */
    Rectangle[] digits = new Rectangle[10];
    /**
     * Position of the operators.
     */
    Rectangle plus, minus, mult, div, equals;
    /**
     * Position of the other annotations
     */
    Rectangle clearEntry, clear, result, move;

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_07_29_Calculator().manipulatePdf(DEST);
    }

    protected static String readFileToString(String path) throws IOException {
        File file = new File(path);
        byte[] jsBytes = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(jsBytes);
        return new String(jsBytes);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        initializeRectangles();
        createPdf(DEST);
    }

    /**
     * Initializes the rectangles for the calculator keys.
     */
    public void initializeRectangles() {
        digits[0] = createRectangle(3, 1, 1, 1);
        digits[1] = createRectangle(1, 3, 1, 1);
        digits[2] = createRectangle(3, 3, 1, 1);
        digits[3] = createRectangle(5, 3, 1, 1);
        digits[4] = createRectangle(1, 5, 1, 1);
        digits[5] = createRectangle(3, 5, 1, 1);
        digits[6] = createRectangle(5, 5, 1, 1);
        digits[7] = createRectangle(1, 7, 1, 1);
        digits[8] = createRectangle(3, 7, 1, 1);
        digits[9] = createRectangle(5, 7, 1, 1);
        plus = createRectangle(7, 7, 1, 1);
        minus = createRectangle(9, 7, 1, 1);
        mult = createRectangle(7, 5, 1, 1);
        div = createRectangle(9, 5, 1, 1);
        equals = createRectangle(7, 1, 3, 1);
        clearEntry = createRectangle(7, 9, 1, 1);
        clear = createRectangle(9, 9, 1, 1);
        result = createRectangle(1, 9, 5, 1);
        move = createRectangle(8, 3, 1, 1);
    }

    /**
     * Creates a PDF document.
     *
     * @param dest the path to the new PDF document
     * @throws IOException
     */
    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(360, 360));
        pdfDoc.getCatalog().setOpenAction(PdfAction.createJavaScript(readFileToString(RESOURCE).replace("\r\n", "\n")));
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        // step 4
        // add the keys for the digits
        for (int i = 0; i < 10; i++) {
            addPushButton(pdfDoc, digits[i], String.valueOf(i), "this.augment(" + i + ")", font);
        }
        // add the keys for the operators
        addPushButton(pdfDoc, plus, "+", "this.register('+')", font);
        addPushButton(pdfDoc, minus, "-", "this.register('-')", font);
        addPushButton(pdfDoc, mult, "x", "this.register('*')", font);
        addPushButton(pdfDoc, div, ":", "this.register('/')", font);
        addPushButton(pdfDoc, equals, "=", "this.calculateResult()", font);
        // add the other keys
        addPushButton(pdfDoc, clearEntry, "CE", "this.reset(false)", font);
        addPushButton(pdfDoc, clear, "C", "this.reset(true)", font);
        addTextField(pdfDoc, result, "result");
        addTextField(pdfDoc, move, "move");

        doc.close();
    }

    /**
     * Add a text field.
     *
     * @param pdfDoc the PdfDocument
     * @param rect   the position of the text field
     * @param name   the name of the text field
     */
    public void addTextField(PdfDocument pdfDoc, Rectangle rect, String name) {
        PdfTextFormField field = PdfFormField.createText(pdfDoc, rect, name, "");
        field.setMultiline(false).setPassword(false).setMaxLen(0);
        field.getWidgets().get(0).setHighlightMode(PdfName.None);
        field.getWidgets().get(0).put(PdfName.Q, new PdfNumber(2));
        field.setFieldFlags(PdfFormField.FF_READ_ONLY);
        field.setBorderWidth(1);
        PdfAcroForm.getAcroForm(pdfDoc, true).addField(field);
    }

    /**
     * Create a pushbutton for a key
     *
     * @param pdfDoc the PdfDocument
     * @param rect   the position of the key
     * @param btn    the label for the key
     * @param script the script to be executed when the button is pushed
     * @param font   the PdfFont
     */
    public void addPushButton(PdfDocument pdfDoc, Rectangle rect, String btn, String script, PdfFont font) {
        float w = rect.getWidth();
        float h = rect.getHeight();
        PdfButtonFormField pushButton = PdfFormField.createPushButton(pdfDoc, rect, "btn_" + btn, "btn_" + btn);
        pushButton.setFieldName("btn_" + btn);
        pushButton.getWidgets().get(0).setHighlightMode(PdfAnnotation.HIGHLIGHT_PUSH);
        pushButton.getWidgets().get(0).setNormalAppearance(createAppearance(pdfDoc, btn, Color.GRAY, w, h, font));
        pushButton.getWidgets().get(0).setRolloverAppearance(createAppearance(pdfDoc, btn, Color.RED, w, h, font));
        pushButton.getWidgets().get(0).setDownAppearance(createAppearance(pdfDoc, btn, Color.BLUE, w, h, font));
        pushButton.getWidgets().get(0).setAdditionalAction(PdfName.U, PdfAction.createJavaScript(script));
        pushButton.getWidgets().get(0).setAdditionalAction(PdfName.E,
                PdfAction.createJavaScript("this.showMove('" + btn + "');"));
        pushButton.getWidgets().get(0).setAdditionalAction(new PdfName("X"),
                PdfAction.createJavaScript("this.showMove(' ');"));
        PdfAcroForm.getAcroForm(pdfDoc, true).addField(pushButton);
    }

    /**
     * Creates an appearance for a key
     *
     * @param pdfDoc the PdfDocument
     * @param btn    the label for the key
     * @param color  the color of the key
     * @param w      the width
     * @param h      the height
     * @param font   the PdfFont
     * @return an appearance
     */
    public PdfDictionary createAppearance(PdfDocument pdfDoc, String btn, Color color, float w, float h, PdfFont font) {
        PdfFormXObject xObject = new PdfFormXObject(new Rectangle(w, h));
        PdfCanvas canvas = new PdfCanvas(xObject, pdfDoc);
        canvas
                .saveState()
                .setFillColor(color)
                .rectangle(2, 2, w - 2, h - 2)
                .fill()
                .restoreState();
        Paragraph p = new Paragraph(btn).setFont(font).setFontSize(h / 2);
        new Canvas(xObject, pdfDoc).showTextAligned(p, w / 2, h / 4, 1,
                TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        return xObject.getPdfObject();
    }

    /**
     * Create a rectangle object for a key.
     *
     * @param column column of the key on the key pad
     * @param row    row of the key on the key pad
     * @param width  width of the key
     * @param height height of the key
     * @return a rectangle defining the position of a key.
     */
    public Rectangle createRectangle(int column, int row, int width,
                                     int height) {
        column = column * 36 - 18;
        row = row * 36 - 18;
        return new Rectangle(column, row, width * 36, height * 36);
    }
}
