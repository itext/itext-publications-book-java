/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.io.font.FontConstants;
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
            zapfdingbats = PdfFontFactory.createFont(FontConstants.ZAPFDINGBATS, PdfEncodings.WINANSI, false);
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
            new Canvas(drawContext.getCanvas(), drawContext.getDocument(), rect)
                    .showTextAligned(p, pageSize.getLeft()-15, rect.getBottom()+8, TextAlignment.CENTER, VerticalAlignment.MIDDLE);
        } else {
            new Canvas(drawContext.getCanvas(), drawContext.getDocument(), rect)
                    .showTextAligned(p, pageSize.getRight()+15, rect.getBottom()+8, drawContext.getDocument().getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.MIDDLE, (float) Math.PI);
        }
    }
}
