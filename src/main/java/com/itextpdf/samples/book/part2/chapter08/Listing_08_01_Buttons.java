package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.CheckBoxFormFieldBuilder;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.forms.fields.RadioFormFieldBuilder;
import com.itextpdf.forms.fields.properties.CheckBoxType;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.tagging.IAccessibleElement;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AbstractElement;
import com.itextpdf.layout.element.ILeafElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Listing_08_01_Buttons {
    public static final String[] RESULT = {
            "./target/book/part2/chapter08/Listing_08_01_Buttons.pdf",

            // The resulting PDF.
            "./target/book/part2/chapter08/Listing_08_01_Buttons_filled.pdf"
    };

    public static final String DEST = RESULT[0];

    /**
     * Path to a JavaScript resource.
     */
    public static final String RESOURCE = "./src/main/resources/js/buttons.js";
    /**
     * Path to an image used as button icon.
     */
    public static final String IMAGE = "./src/main/resources/img/info.png";
    /**
     * Possible values of a radio field / checkboxes
     */
    public static final String[] LANGUAGES = {"English", "German", "French", "Spanish", "Dutch"};

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_08_01_Buttons().manipulatePdf(DEST);
    }

    public void createPdf(String filename) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);
        pdfDoc.getCatalog().setOpenAction(PdfAction.createJavaScript(readFileToString(RESOURCE).replace("\r\n", "\n")));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        Rectangle rect;
        RadioFormFieldBuilder builder = new RadioFormFieldBuilder(pdfDoc, "language");
        PdfButtonFormField radioGroup = builder.createRadioGroup();
        radioGroup.setValue("");
        radioGroup.setCheckType(CheckBoxType.CIRCLE);
        for (int i = 0; i < LANGUAGES.length; i++) {
            rect = new Rectangle(40, 806 - i * 40, 60 - 40, 806 - 788);
            PdfFormAnnotation radio = builder.createRadioButton(LANGUAGES[i], rect)
                    .setBorderColor(ColorConstants.DARK_GRAY)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY);
            radioGroup.addKid(radio);
            canvas
                    .beginText()
                    .setFontAndSize(font, 18)
                    .moveText(70, 790 - i * 40 + 20)
                    .showText(LANGUAGES[i])
                    .endText();
        }
        PdfFormCreator.getAcroForm(pdfDoc, true).addField(radioGroup);

        PdfButtonFormField checkBox;
        for (int i = 0; i < LANGUAGES.length; i++) {
            PdfFormXObject xObjectApp1 = new PdfFormXObject(new Rectangle(0, 0, 200 - 180, 806 - 788));
            PdfCanvas canvasApp1 = new PdfCanvas(xObjectApp1, pdfDoc);
            canvasApp1
                    .saveState()
                    .rectangle(1, 1, 18, 18)
                    .stroke()
                    .restoreState();
            PdfFormXObject xObjectApp2 = new PdfFormXObject(new Rectangle(0, 0, 200 - 180, 806 - 788));
            PdfCanvas canvasApp2 = new PdfCanvas(xObjectApp2, pdfDoc);
            canvasApp2
                    .saveState()
                    .setFillColorRgb(255, 128, 128)
                    .rectangle(1, 1, 18, 18)
                    .rectangle(180, 806 - i * 40, 200 - 180, 806 - 788)
                    .fillStroke()
                    .moveTo(1, 1)
                    .lineTo(19, 19)
                    .moveTo(1, 19)
                    .lineTo(19, 1)
                    .stroke()
                    .restoreState();

            rect = new Rectangle(180, 806 - i * 40, 200 - 180, 806 - 788);
            checkBox = new CheckBoxFormFieldBuilder(pdfDoc, LANGUAGES[i])
                    .setWidgetRectangle(rect).createCheckBox();
            checkBox.getWidgets().get(0).getNormalAppearanceObject().put(new PdfName("Off"),
                    xObjectApp1.getPdfObject());
            checkBox.getWidgets().get(0).getNormalAppearanceObject().put(new PdfName("Yes"),
                    xObjectApp2.getPdfObject());
            // Write text
            canvas
                    .beginText()
                    .setFontAndSize(font, 18)
                    .moveText(210, 790 - i * 40 + 20)
                    .showText(LANGUAGES[i])
                    .endText();
            PdfFormCreator.getAcroForm(pdfDoc, true).addField(checkBox);
        }

        // Add the push button
        rect = new Rectangle(300, 806, 370 - 300, 806 - 788);

        Button button = new Button("Buttons", "Push me", pdfDoc, rect);
        button.setImage(ImageDataFactory.create(IMAGE));
        button.setButtonBackgroundColor(new DeviceGray(0.75f));
        button.setBorderColor(ColorConstants.DARK_GRAY);
        button.setFontSize(12);

        doc.add(new Paragraph().add(button));

        PdfWidgetAnnotation ann = button.getButton().getWidgets().get(0);
        ann.setAction(PdfAction.createJavaScript("this.showButtonState()"));

        doc.close();
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     *
     * @param src  the original PDF
     * @param dest the resulting PDF
     *
     * @throws IOException error during file creation/accessing
     */
    public void fillPdf(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);
        String[] radiostates = form.getField("language").getAppearanceStates();
        form.getField("language").setValue(radiostates[4]);
        for (int i = 0; i < LANGUAGES.length; i++) {
            String[] checkboxStates = form.getField("English").getAppearanceStates();
            form.getField(LANGUAGES[i]).setValue(checkboxStates[i % 2 == 0 ? 1 : 0], false);
        }
        pdfDoc.close();
    }

    protected static String readFileToString(String path) throws IOException {
        File file = new File(path);
        byte[] jsBytes = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(jsBytes);
        return new String(jsBytes);
    }

    protected void manipulatePdf(String dest) throws Exception {
        createPdf(DEST);
        fillPdf(DEST, RESULT[1]);
    }

    private class Button extends AbstractElement<Button> implements ILeafElement, IAccessibleElement {

        protected PdfButtonFormField button;
        protected String caption;
        protected ImageData image;
        protected Rectangle rect;
        protected Color borderColor = ColorConstants.BLACK;
        protected Color buttonBackgroundColor = ColorConstants.WHITE;
        DefaultAccessibilityProperties accessibilityProperties;

        public Button(String name, String caption, PdfDocument document, Rectangle rect) {
            button = new PushButtonFormFieldBuilder(document, name)
                    .setWidgetRectangle(new Rectangle(0, 0)).createPushButton();
            button.setPushButton(true);

            this.caption = caption;
            this.rect = rect;
        }

        @Override
        public DefaultAccessibilityProperties getAccessibilityProperties() {
            if (accessibilityProperties == null) {
                accessibilityProperties = new DefaultAccessibilityProperties(StandardRoles.FIGURE);
            }
            return accessibilityProperties;
        }

        public PdfButtonFormField getButton() {
            return button;
        }

        public String getCaption() {
            return caption;
        }

        public ImageData getImage() {
            return image;
        }

        public void setImage(ImageData image) {
            this.image = image;
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

        @Override
        protected IRenderer makeNewRenderer() {
            return new ButtonRenderer(this);
        }
    }

    class ButtonRenderer extends AbstractRenderer {

        public ButtonRenderer(Button button) {
            super(button);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutArea area = layoutContext.getArea().clone();
            Rectangle layoutBox = area.getBBox();
            applyMargins(layoutBox, false);
            Button modelButton = (Button) modelElement;
            occupiedArea = new LayoutArea(area.getPageNumber(), new Rectangle(modelButton.rect));
            PdfButtonFormField button = ((Button) getModelElement()).getButton();
            button.getWidgets().get(0).setRectangle(new PdfArray(occupiedArea.getBBox()));

            return new LayoutResult(LayoutResult.FULL, occupiedArea, null, null);
        }

        @Override
        public IRenderer getNextRenderer() {
            return null;
        }

        @Override
        public void draw(DrawContext drawContext) {
            Button modelButton = (Button) modelElement;
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
                    rectangle(0.5f, 0.5f, occupiedArea.getBBox().getWidth() - 1,
                            occupiedArea.getBBox().getHeight() - 1).
                    fill().
                    restoreState();

            Paragraph paragraph = new Paragraph(modelButton.getCaption()).setFontSize(10).setMargin(0)
                    .setMultipliedLeading(1);

            new Canvas(canvas, new Rectangle(0, 0, width, height)).
                    showTextAligned(paragraph, 20, 3, TextAlignment.LEFT, VerticalAlignment.BOTTOM);

            ImageData image = modelButton.getImage();
            if (image != null) {
                PdfImageXObject imageXObject = new PdfImageXObject(image);
                float imageWidth = image.getWidth();

                if (image.getWidth() > modelButton.rect.getWidth() * 2 / 3) {
                    imageWidth = modelButton.rect.getWidth() * 2 / 3;
                }
                if (image.getHeight() > modelButton.rect.getHeight()) {
                    imageWidth = image.getWidth() * (modelButton.rect.getHeight() / image.getHeight()) * 2 / 3;
                }
                Rectangle rect = PdfXObject.calculateProportionallyFitRectangleWithWidth(imageXObject, 3, 3,
                        imageWidth);
                canvas.addXObjectFittedIntoRectangle(imageXObject, rect);

                xObject.getResources().addImage(imageXObject);
            }

            PdfButtonFormField button = modelButton.getButton();
            button.getWidgets().get(0).setNormalAppearance(xObject.getPdfObject());
            xObject.getPdfObject().getOutputStream().writeBytes(str.getBytes());

            PdfFormCreator.getAcroForm(drawContext.getDocument(), true)
                    .addField(button, drawContext.getDocument().getPage(1));
        }
    }
}
