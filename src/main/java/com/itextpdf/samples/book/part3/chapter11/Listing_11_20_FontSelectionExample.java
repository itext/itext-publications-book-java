package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TransparentColor;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.LineRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listing_11_20_FontSelectionExample {
    public static final String DEST
            = "./target/book/part3/chapter11/Listing_11_20_FontSelectionExample.pdf";

    public static final String TEXT
            = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
            + "\u7121\u540d (Nameless),\n"
            + "\u6b98\u528d (Broken Sword),\n"
            + "\u98db\u96ea (Flying Snow),\n"
            + "\u5982\u6708 (Moon),\n"
            + "\u79e6\u738b (the King),\n"
            + "\u9577\u7a7a (Sky).";
    public static final String kozMinProFont = "KozMinPro-Regular";
    public static final String timesRomanFont = StandardFonts.TIMES_ROMAN;

    public static void main(String[] agrs) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_11_20_FontSelectionExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph paragraph = new Paragraph(TEXT);

        FontProvider provider = new FontProvider();
        provider.addFont(timesRomanFont);
        provider.addFont(kozMinProFont, "UniJIS-UCS2-H");

        // Adding fonts to the font provider
        doc.setFontProvider(provider);

        // Setting font family to the particular element will trigger iText's font selection mechanism:
        // font for the element will be picked up from the provider's fonts
        paragraph.setFontFamily(timesRomanFont);

        Map<String, Color> fontColorMap = new HashMap<>();
        fontColorMap.put(kozMinProFont, ColorConstants.RED);
        fontColorMap.put(timesRomanFont, ColorConstants.BLUE);

        // Set custom renderer, which will change the color of text written within specific font
        paragraph.setNextRenderer(new CustomParagraphRenderer(paragraph, fontColorMap));

        doc.add(paragraph);

        doc.close();
    }

    public static class CustomParagraphRenderer extends ParagraphRenderer {
        private Map<String, Color> fontColorMap;

        public CustomParagraphRenderer(Paragraph modelElement, Map<String, Color> fontColorMap) {
            super(modelElement);
            this.fontColorMap = new HashMap<>(fontColorMap);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = super.layout(layoutContext);

            // During layout() execution iText parses the Paragraph and splits it into a number of Text objects,
            // each of which have glyphs to be processed by the same font.
            // Lines, which are the result of layout(), will be drawn to the pdf on draw().
            // In order to change the color of text written in specific font, we could just go through all the lines
            // and update the color of Text objects, which have this font being set.
            List<LineRenderer> lines = getLines();
            updateColor(lines);

            return result;
        }

        private void updateColor(List<LineRenderer> lines) {
            for (LineRenderer renderer : lines) {
                List<IRenderer> children = renderer.getChildRenderers();
                for (IRenderer child : children) {
                    if (child instanceof TextRenderer) {
                        PdfFont pdfFont = ((TextRenderer) child).getPropertyAsFont(Property.FONT);
                        if (null != pdfFont) {
                            Color updatedColor = fontColorMap
                                    .get(pdfFont.getFontProgram().getFontNames().getFontName());
                            if (null != updatedColor) {

                                // Although setting a property via setProperty might be useful,
                                // it's regarded as internal iText functionality. The properties are expected
                                // to be set on elements via specific setters (setFont, for example).
                                // iText doesn't guarantee that setProperty will work as you expect it,
                                // so please be careful while calling this method.
                                child.setProperty(Property.FONT_COLOR, new TransparentColor(updatedColor));
                            }
                        }
                    }
                }
            }
        }
    }
}
