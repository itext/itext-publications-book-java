/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter07;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.IAccessibleElement;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_07_27_Advertisement extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter07/Listing_07_27_Advertisement.pdf";

    /** Path to a resource. */
    public static final String RESOURCE = "./src/test/resources/pdfs/hero.pdf";
    /** Path to a resource. */
    public static final String IMAGE = "./src/test/resources/img/close.png";
    /** The resulting PDF file. */

    public static final String NESTED_TABLES = "./src/test/resources/book/part1/chapter04/cmp_Listing_04_17_NestedTables.pdf";

    protected String[] arguments;

    public static void main(String args[]) throws IOException, SQLException {
        Listing_07_27_Advertisement application = new Listing_07_27_Advertisement();
        application.arguments = args;
        application.manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // Create a reader for the original document
        PdfReader reader = new PdfReader(NESTED_TABLES);
        // Create a reader for the advertisement resource
        PdfReader ad = new PdfReader(RESOURCE);
        PdfDocument adDoc = new PdfDocument(ad);
        // Create a stamper
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        // Create the advertisement annotation for the menubar
        Rectangle rect = new Rectangle(400, 772, 145, 20);

        CustomButton button = new CustomButton("click", "Close this advertisement", pdfDoc, rect);
        button.setImage(new PdfImageXObject(ImageDataFactory.create(IMAGE)));
        button.setButtonBackgroundColor(Color.RED);
        button.setBorderColor(Color.RED);
        button.setFontSize(10);
        doc.add(new Paragraph().add(button));

        PdfAnnotation menubar = button.getButton().getWidgets().get(0);
        String js = "var f1 = getField('click'); f1.display = display.hidden;"
                + "var f2 = getField('advertisement'); f2.display = display.hidden;";
        menubar.setAction(PdfAction.createJavaScript(js));

        // Create the advertisement annotation for the content
        rect = new Rectangle(400, 550, 545-400, 222);
        button = new CustomButton("advertisement", "Buy the book iText in Action 2nd edition", pdfDoc, rect);
        button.setButtonBackgroundColor(Color.WHITE);
        button.setBorderColor(Color.RED);
        button.setImage(adDoc.getPage(1).copyAsFormXObject(pdfDoc));
        button.setFontSize(8);
        doc.add(new Paragraph().add(button));

        PdfAnnotation advertisement = button.getButton().getWidgets().get(0);
        advertisement.setAction(PdfAction.createURI("http://www.1t3xt.com/docs/book.php"));
        // Close the pdf document
        pdfDoc.close();
    }

    private class CustomButton extends AbstractElement<CustomButton> implements ILeafElement, IAccessibleElement {

        protected PdfName role = PdfName.Figure;
        protected PdfButtonFormField button;
        protected String caption;
        protected PdfXObject image;
        protected Rectangle rect;
        protected Color borderColor = Color.BLACK;
        protected Color buttonBackgroundColor = Color.WHITE;
        protected int fontSize = 12;

        public CustomButton(String name, String caption, PdfDocument document, Rectangle rect) {
            button = PdfFormField.createButton(document, new Rectangle(0, 0), 0);
            button.setFieldName(name);
            button.setPushButton(true);

            this.caption = caption;
            this.rect = rect;
        }

        @Override
        protected IRenderer makeNewRenderer() {
            return new CustomButtonRenderer(this);
        }

        @Override
        public PdfName getRole() {
            return role;
        }

        @Override
        public void setRole(PdfName role) {
            this.role = role;
        }

        @Override
        public AccessibilityProperties getAccessibilityProperties() {
            return null;
        }

        public PdfButtonFormField getButton() {
            return button;
        }

        public String getCaption() {
            return caption;
        }

        public void setImage(PdfXObject image) {
            this.image = image;
        }

        public PdfXObject getImage() {
            return image;
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
        }

        public void setButtonBackgroundColor(Color buttonBackgroundColor) {
            this.buttonBackgroundColor = buttonBackgroundColor;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }
    }

    class CustomButtonRenderer extends AbstractRenderer {

        public CustomButtonRenderer(CustomButton button) {
            super(button);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutArea area = layoutContext.getArea().clone();
            Rectangle layoutBox = area.getBBox();
            applyMargins(layoutBox, false);
            CustomButton modelButton = (CustomButton) modelElement;
            occupiedArea = new LayoutArea(area.getPageNumber(), new Rectangle(modelButton.rect));
            PdfButtonFormField button = ((CustomButton) getModelElement()).getButton();
            button.getWidgets().get(0).setRectangle(new PdfArray(occupiedArea.getBBox()));

            return new LayoutResult(LayoutResult.FULL, occupiedArea, null, null);
        }

        @Override
        public void draw(DrawContext drawContext) {
            CustomButton modelButton = (CustomButton) modelElement;
            occupiedArea.setBBox(modelButton.rect);

            super.draw(drawContext);
            float width = occupiedArea.getBBox().getWidth();
            float height = occupiedArea.getBBox().getHeight();

            PdfStream str = new PdfStream();
            PdfCanvas canvas = new PdfCanvas(str, new PdfResources(), drawContext.getDocument());
            PdfFormXObject xObject = new PdfFormXObject(new Rectangle(0, 0, width, height));

            canvas.
                    saveState().
                    setStrokeColor(modelButton.getBorderColor()).
                    setLineWidth(1).
                    rectangle(0, 0, occupiedArea.getBBox().getWidth(), occupiedArea.getBBox().getHeight()).
                    stroke().
                    setFillColor(modelButton.buttonBackgroundColor).
                    rectangle(.5f, .5f, occupiedArea.getBBox().getWidth() - 1, occupiedArea.getBBox().getHeight() - 1).
                    fill().
                    restoreState();

            Paragraph paragraph = new Paragraph(modelButton.getCaption()).setFontSize(modelButton.getFontSize()).setMargin(0).setMultipliedLeading(1);

            new Canvas(canvas, drawContext.getDocument(), new Rectangle(0, 0, width, height)).
                    showTextAligned(paragraph, 1, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM);

            PdfXObject obj = modelButton.getImage();
            float imageWidth = obj.getWidth();
            if (obj instanceof PdfImageXObject) {
                if (obj.getWidth() > modelButton.rect.getWidth()) {
                    imageWidth = modelButton.rect.getWidth();
                } else if (obj.getHeight() > modelButton.rect.getHeight()) {
                    imageWidth = imageWidth * (modelButton.rect.getHeight() / obj.getHeight());
                }
            }

            if (obj instanceof PdfFormXObject) {
                canvas.addXObject(obj, width / obj.getWidth(), 0, 0, height / obj.getHeight(), .5f, .5f);
            } else {
                canvas.addXObject(obj, 0 + occupiedArea.getBBox().getWidth() - imageWidth, 0, imageWidth);
            }


            PdfButtonFormField button = modelButton.getButton();
            button.getWidgets().get(0).setNormalAppearance(xObject.getPdfObject());
            xObject.getPdfObject().getOutputStream().writeBytes(str.getBytes());
            if (obj instanceof PdfImageXObject) {
                xObject.getResources().addImage((PdfImageXObject) obj);
            } else {
                xObject.getResources().addForm((PdfFormXObject) obj);
            }

            PdfAcroForm.getAcroForm(drawContext.getDocument(), true).addField(button, drawContext.getDocument().getPage(1));
        }

        @Override
        public IRenderer getNextRenderer() {
            return null;
        }
    }
}

