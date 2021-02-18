package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TabRenderer;

import java.io.IOException;

public class PositionedArrowTabRenderer extends TabRenderer {
    protected boolean isLeft;
    protected PdfFont zapfdingbats;
    protected PageSize pageSize;

    public PositionedArrowTabRenderer(Tab tab, Document doc, boolean isLeft) {
        super(tab);
        this.isLeft = isLeft;
        try {
            zapfdingbats = PdfFontFactory.createFont(StandardFonts.ZAPFDINGBATS, PdfEncodings.WINANSI);
        } catch (IOException ioe) {
            zapfdingbats = null;
        }
        this.pageSize = new PageSize(doc.getPageEffectiveArea(doc.getPdfDocument().getDefaultPageSize()));
    }

    @Override
    public void draw(DrawContext drawContext) {
        Rectangle rect = getOccupiedAreaBBox().clone().setHeight(20);
        Paragraph p = new Paragraph(new String(new char[]{220}));
        p.setFont(zapfdingbats);
        if (isLeft) {
            new Canvas(drawContext.getCanvas(), rect)
                    .showTextAligned(p, pageSize.getLeft()-15, rect.getBottom()+5, TextAlignment.CENTER, VerticalAlignment.MIDDLE);
        } else {
            new Canvas(drawContext.getCanvas(), rect)
                    .showTextAligned(p, pageSize.getRight()+15, rect.getBottom()+5, drawContext.getDocument().getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.MIDDLE, (float) Math.PI);
        }
    }
}
