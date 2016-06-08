/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part2.chapter08;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.color.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.IAccessibleElement;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AbstractElement;
import com.itextpdf.layout.element.ILeafElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_08_16_MovieAds extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part2/chapter08/Listing_08_16_MovieAds.pdf";
    public static final String TEMPLATE
            = "./target/test/resources/book/part2/chapter08/Listing_08_16_MovieAds_template.pdf";
    public static final String RESOURCE
            = "./src/test/resources/pdfs/movie_overview.pdf";
    public static final String IMAGE
            = "./src/test/resources/img/posters/%s.jpg";
    public static final String POSTER = "poster";
    public static final String TEXT = "text";
    public static final String YEAR = "year";

    public static void main(String[] args) throws Exception {
        new Listing_08_16_MovieAds().manipulatePdf(DEST);
    }

    public static float millimetersToPoints(float value) {
        return (value / 25.4f) * 72f;
    }

    /**
     * Create a small formXObject that will be used for an individual ad.
     *
     * @param filename the filename of the add
     * @throws IOException
     */
    public void createTemplate(String filename) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        pdfDoc.setDefaultPageSize(new PageSize(millimetersToPoints(35), millimetersToPoints(50)));
        pdfDoc.addNewPage();
        pdfDoc.getCatalog().setPageLayout(PdfName.SinglePage);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfButtonFormField poster = PdfFormField.createPushButton(pdfDoc, new Rectangle(millimetersToPoints(0),
                millimetersToPoints(25), millimetersToPoints(35) - millimetersToPoints(0),
                millimetersToPoints(50) - millimetersToPoints(25)), POSTER, "");
        poster.setBackgroundColor(new DeviceGray(0.4f));
        form.addField(poster);

        PdfTextFormField movie = PdfFormField.createText(pdfDoc, new Rectangle(millimetersToPoints(0),
                millimetersToPoints(7), millimetersToPoints(35) - millimetersToPoints(0),
                millimetersToPoints(25) - millimetersToPoints(7)), TEXT, "");
        movie.setMultiline(true);
        form.addField(movie);

        PdfTextFormField screening = PdfFormField.createText(pdfDoc, new Rectangle(millimetersToPoints(0),
                millimetersToPoints(0), millimetersToPoints(35) - millimetersToPoints(0),
                millimetersToPoints(7) - millimetersToPoints(0)), YEAR, "");
        screening.setJustification(PdfFormField.ALIGN_CENTER);
        screening.setBackgroundColor(new DeviceGray(0.4f));
        screening.setColor(Color.LIGHT_GRAY);
        form.addField(screening);

        pdfDoc.close();
    }

    /**
     * Fill out the small formXObject with information about the movie.
     *
     * @param filename the formXObject for an individual ad
     * @param movie    the movie that needs to be in the ad
     * @return a byte[] containing an individual ad
     * @throws IOException
     */
    public byte[] fillTemplate(String filename, Movie movie) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename), new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);


        PdfButtonFormField bt = (PdfButtonFormField) form.getField(POSTER);
        Rectangle rect = bt.getWidgets().get(0).getRectangle().toRectangle();
        CustomButton button = new CustomButton(bt);
        button.setImage(ImageDataFactory.create(String.format(IMAGE, movie.getImdb())));
        // change the background color of the poster and add a new icon
        DeviceRgb color = WebColors.getRGBColor("#" + movie.getEntry().getCategory().getColor());

        button.setButtonBackgroundColor(color);
        form.removeField(POSTER);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        new Canvas(canvas, pdfDoc, rect)
                .add(new Paragraph().add(button));
        // write the text using the appropriate font size
        rect = form.getField(TEXT).getWidgets().get(0).getRectangle().toRectangle();
        float size = 30;
        ParagraphRenderer paragraphRenderer;
        do {
            size -= 0.2;
            paragraphRenderer = (ParagraphRenderer) createMovieParagraph(movie, size).createRendererSubTree();
            paragraphRenderer.setParent(doc.getRenderer());
        } while (paragraphRenderer.layout(new LayoutContext(new LayoutArea(1, rect))).getStatus() != LayoutResult.FULL);
        paragraphRenderer.draw(new DrawContext(pdfDoc, new PdfCanvas(pdfDoc.getFirstPage()), false));
        // fill out the year and change the background color
        form.getField(YEAR).setBackgroundColor(color);
        form.getField(YEAR).setValue(String.valueOf(movie.getYear()));
        form.flattenFields();
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a paragraph containing info about a movie
     *
     * @param movie    the Movie pojo
     * @param fontsize the font size
     * @return a Paragraph object
     */
    public Paragraph createMovieParagraph(Movie movie, float fontsize) {
        PdfFont normal = null;
        PdfFont bold = null;
        PdfFont italic = null;
        try {
            normal = PdfFontFactory.createFont(FontConstants.HELVETICA, "", false, true);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD, "", false, true);
            italic = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE, "", false, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Paragraph p = new Paragraph().setFixedLeading(fontsize * 1.2f);
        p.setPaddingLeft(2);
        p.setPaddingRight(2);
        p.setPaddingBottom(2);

        p.setFont(normal).setFontSize(fontsize).setTextAlignment(TextAlignment.JUSTIFIED);
        p.add(new Text(movie.getMovieTitle()).setFont(bold));
        if (movie.getOriginalTitle() != null) {
            p.add(" ");
            p.add(new Text(movie.getOriginalTitle()).setFont(italic));
        }
        p.add(new Text(String.format("; run length: %s", movie.getDuration())).setFont(normal));
        p.add(new Text("; directed by:").setFont(normal));
        for (Director director : movie.getDirectors()) {
            p.add(" ");
            p.add(director.getGivenName());
            p.add(", ");
            p.add(director.getName());
        }
        return p;
    }

    protected void manipulatePdf(String dest) throws Exception {
        createTemplate(TEMPLATE);
        // open the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PdfDocument pageDoc = null;
        ByteArrayOutputStream baos = null;
        PdfAcroForm form = null;
        Document doc = null;
        int count = 0;
        int currentPageNumber = 0;
        for (Movie movie : PojoFactory.getMovies(connection)) {
            if (count == 0) {
                baos = new ByteArrayOutputStream();
                pageDoc = new PdfDocument(new PdfReader(RESOURCE), new PdfWriter(baos));
                form = PdfAcroForm.getAcroForm(pageDoc, true);
                doc = new Document(pageDoc);
                currentPageNumber++;
            }
            count++;
            PdfDocument ad = new PdfDocument(new PdfReader(new ByteArrayInputStream(fillTemplate(TEMPLATE, movie))));
            PdfPage curPage = ad.getFirstPage();
            PdfFormXObject xObject = curPage.copyAsFormXObject(pageDoc);
            PdfButtonFormField bt = (PdfButtonFormField) form.getField("movie_" + count);
            bt.setFieldName("movie_" + count + "_on_page_" + currentPageNumber);
            bt.setImageAsForm(xObject);
            ad.close();
            if (count == 16) {
                doc.close();
                pageDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
                pageDoc.copyPagesTo(1, 1, pdfDoc);
                pageDoc.close();
                count = 0;
            }
        }
        if (count > 0) {
            doc.close();
            pageDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
            pageDoc.copyPagesTo(1, 1, pdfDoc);
            pageDoc.close();
        }
        pdfDoc.close();
        // close the database connection
        connection.close();
    }


    class CustomButton extends AbstractElement<CustomButton> implements ILeafElement, IAccessibleElement {
        protected PdfName role = PdfName.Figure;
        protected PdfButtonFormField button;
        protected String caption;
        protected com.itextpdf.io.image.ImageData image;
        protected Rectangle rect;
        protected Color borderColor = Color.BLACK;
        protected Color buttonBackgroundColor = Color.WHITE;

        public CustomButton(PdfButtonFormField button) {
            this.button = button;
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
            return caption == null ? "" : caption;
        }

        public void setImage(com.itextpdf.io.image.ImageData image) {
            this.image = image;
        }

        public com.itextpdf.io.image.ImageData getImage() {
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

            new Canvas(canvas, drawContext.getDocument(), new Rectangle(0, 0, width, height)).
                    showTextAligned(paragraph, 1, 1, TextAlignment.LEFT, VerticalAlignment.BOTTOM);

            PdfImageXObject imageXObject = new PdfImageXObject(modelButton.getImage());
            float imageWidth = imageXObject.getWidth();
            float imageHeight = imageXObject.getHeight();
            if (imageWidth > rect.getWidth()) {
                imageHeight *= rect.getWidth() / imageWidth;
                imageWidth = rect.getWidth();
            }
            if (imageHeight > rect.getHeight()) {
                imageWidth = imageWidth * (rect.getHeight() / imageHeight);
            }

            canvas.addXObject(imageXObject, 0.5f + (rect.getWidth() - imageWidth) / 2, 0.5f, imageWidth - 1);

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
