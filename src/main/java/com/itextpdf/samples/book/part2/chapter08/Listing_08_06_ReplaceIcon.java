package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
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
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;

import java.io.File;
import java.io.IOException;

public class Listing_08_06_ReplaceIcon {
    public static final String DEST = "./target/book/part2/chapter08/Listing_08_06_ReplaceIcon.pdf";

    public static final String ADVERTISEMENT = "./src/main/resources/pdfs/cmp_Listing_07_27_Advertisement.pdf";

    /** Image that will be used as an icon. */
    public static final String RESOURCE = "./src/main/resources/img/iia2.jpg";

    /**
     * Possible values of a Choice field.
     */
    protected String[] arguments;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        Listing_08_06_ReplaceIcon application = new Listing_08_06_ReplaceIcon();
        application.arguments = args;
        application.manipulatePdf(DEST);
    }

    public void manipulatePdf2(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src),new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Document doc = new Document(pdfDoc);
        CustomButton ad = new CustomButton((PdfButtonFormField) form.getField("advertisement"));
        ad.setImage(ImageDataFactory.create(RESOURCE));
        form.removeField("advertisement");
        doc.add(new Paragraph().add(ad));
        pdfDoc.close();
    }

    protected void manipulatePdf(String dest) throws Exception {
        manipulatePdf2(ADVERTISEMENT, DEST);
    }


    class CustomButton extends AbstractElement<CustomButton> implements ILeafElement, IAccessibleElement {

        protected DefaultAccessibilityProperties accessibilityProperties;
        protected PdfButtonFormField button;
        protected String caption;
        protected ImageData image;
        protected Rectangle rect;
        protected Color borderColor = ColorConstants.BLACK;
        protected Color buttonBackgroundColor = ColorConstants.WHITE;

        public CustomButton(PdfButtonFormField button) {
            this.button = button;
        }

        @Override
        protected IRenderer makeNewRenderer() {
            return new CustomButtonRenderer(this);
        }

        @Override
        public AccessibilityProperties getAccessibilityProperties() {
            if (accessibilityProperties == null) {
                accessibilityProperties = new DefaultAccessibilityProperties(StandardRoles.FIGURE);
            }
            return accessibilityProperties;
        }

        public PdfButtonFormField getButton() {
            return button;
        }

        public String getCaption() {
            return caption == null ? "" : caption;
        }

        public void setImage(ImageData image) {
            this.image = image;
        }

        public ImageData getImage() {
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
            occupiedArea = new LayoutArea(area.getPageNumber(), new Rectangle(modelButton.button.getWidgets().get(0).getRectangle().toRectangle()));
            PdfButtonFormField button = ((CustomButton) getModelElement()).getButton();
            button.getWidgets().get(0).setRectangle(new PdfArray(occupiedArea.getBBox()));

            return new LayoutResult(LayoutResult.FULL, occupiedArea, null, null);
        }

        @Override
        public void draw(DrawContext drawContext) {
            CustomButton modelButton = (CustomButton) modelElement;
            Rectangle rect = modelButton.button.getWidgets().get(0).getRectangle().toRectangle();
            occupiedArea.setBBox(rect);

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
                    rectangle(0.5f, 0.5f, occupiedArea.getBBox().getWidth() - 1, occupiedArea.getBBox().getHeight() - 1).
                    fill().
                    restoreState();

            Paragraph paragraph = new Paragraph(modelButton.getCaption()).setFontSize(10).setMargin(0).setMultipliedLeading(1);

            new Canvas(canvas, new Rectangle(0, 0, width, height)).
                    showTextAligned(paragraph, 1, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM);

            PdfImageXObject imageXObject = new PdfImageXObject(modelButton.getImage());
            float imageWidth = imageXObject.getWidth();
            if (imageXObject.getWidth() > rect.getWidth()) {
                imageWidth = rect.getWidth();
            } else if (imageXObject.getHeight() > rect.getHeight()) {
                imageWidth = imageWidth * (rect.getHeight() / imageXObject.getHeight());
            }

            Rectangle rectangle = PdfXObject.calculateProportionallyFitRectangleWithWidth(imageXObject, 0.5f, 0.5f, imageWidth - 1);
            canvas.addXObjectFittedIntoRectangle(imageXObject, rectangle);


            PdfButtonFormField button = modelButton.getButton();
            button.getWidgets().get(0).setNormalAppearance(xObject.getPdfObject());
            xObject.getPdfObject().getOutputStream().writeBytes(str.getBytes());
            xObject.getResources().addImage(imageXObject);

            PdfAcroForm.getAcroForm(drawContext.getDocument(), true).addField(button, drawContext.getDocument().getPage(1));
        }

        @Override
        public IRenderer getNextRenderer() {
            return null;
        }
    }
}
