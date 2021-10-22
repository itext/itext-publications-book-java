package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Listing_08_07_TextFields {
    public static final String[] RESULT = {
            "./target/book/part2/chapter08/Listing_08_07_TextFields.pdf",
            "./target/book/part2/chapter08/Listing_08_07_TextFields_filled.pdf"
    };

    public static final String DEST = RESULT[0];

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_08_07_TextFields().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        createPdf(DEST);
        fillPdf(DEST, RESULT[1]);
    }

    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Cell cell;
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
        table.setWidth(UnitValue.createPercentValue(80));

        table.addCell(new Cell().add(new Paragraph("Name:")));
        cell = new Cell();
        cell.setNextRenderer(new TextFilledCellRenderer(cell, 1));
        table.addCell(cell);

        table.addCell(new Cell().add(new Paragraph("Loginname:")));

        cell = new Cell();
        cell.setNextRenderer(new TextFilledCellRenderer(cell, 2));
        table.addCell(cell);

        table.addCell(new Cell().add(new Paragraph("Password:")));

        cell = new Cell();
        cell.setNextRenderer(new TextFilledCellRenderer(cell, 3));

        table.addCell(cell);

        table.addCell(new Cell().add(new Paragraph("Reason:")));

        cell = new Cell();
        cell.setNextRenderer(new TextFilledCellRenderer(cell, 4));

        cell.setHeight(60);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }

    public void fillPdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();

        fields.get("text_1").setValue("Bruno Lowagie");

        fields.get("text_2").setFieldFlags(0);
        fields.get("text_2").setBorderColor(ColorConstants.RED);
        fields.get("text_2").setValue("bruno");

        fields.get("text_3").setFieldFlag(PdfFormField.FF_PASSWORD, false);
        fields.get("text_3").getWidgets().get(0).setFlag(PdfAnnotation.PRINT);
        form.getField("text_3").setValue("12345678", "********");
        ((PdfTextFormField) form.getField("text_4")).setMaxLen(12);
        form.getField("text_4").regenerateField();
        pdfDoc.close();
    }


    protected class TextFilledCellRenderer extends CellRenderer {
        /**
         * The text field index of a TextField that needs to be added to a cell.
         */
        protected int tf = 0;

        public TextFilledCellRenderer(Cell modelElement, int tf) {
            super(modelElement);
            this.tf = tf;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TextFilledCellRenderer((Cell) modelElement, tf);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfTextFormField text = PdfFormField.createText(drawContext.getDocument(), getOccupiedAreaBBox(),
                    String.format("text_%s", tf), "Enter your name here...");
            text.setBackgroundColor(new DeviceGray(0.75f));
            PdfDictionary borderStyleDict = new PdfDictionary();
            switch (tf) {
                case 1:
                    borderStyleDict.put(PdfName.S, PdfName.B);
                    text.getWidgets().get(0).setBorderStyle(borderStyleDict);
                    text.setFontSize(10);
                    text.setValue("Enter your name here...");
                    text.setRequired(true);
                    text.setJustification(PdfFormField.ALIGN_CENTER);
                    break;
                case 2:
                    borderStyleDict.put(PdfName.S, PdfName.S);
                    text.getWidgets().get(0).setBorderStyle(borderStyleDict);
                    text.setValue("Name...");
                    text.setMaxLen(8);
                    text.setComb(true);
                    text.setBorderWidth(2);
                    break;
                case 3:
                    borderStyleDict.put(PdfName.S, PdfName.I);
                    text.getWidgets().get(0).setBorderStyle(borderStyleDict);
                    text.setPassword(true);
                    text.setValue("Choose a password");
                    text.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
                    break;
                case 4:
                    borderStyleDict.put(PdfName.S, PdfName.D);
                    text.getWidgets().get(0).setBorderStyle(borderStyleDict);
                    text.setBorderColor(ColorConstants.RED);
                    text.setFontSize(8);
                    text.setValue("Enter the reason why you want to win a free " +
                            "accreditation for the Foobar Film Festival");
                    text.setBorderWidth(2);
                    text.setMultiline(true);
                    text.setRequired(true);
                    break;
            }
            PdfAcroForm.getAcroForm(drawContext.getDocument(), true).addField(text);
        }
    }
}
