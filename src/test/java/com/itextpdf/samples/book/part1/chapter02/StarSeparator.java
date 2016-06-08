/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part1.chapter02;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class StarSeparator extends Div {

    public StarSeparator() {
        super();

        Paragraph[] content = {new Paragraph("*"), new Paragraph("*  *")};
        for (Paragraph p : content) {
            p.setFontSize(10).
                    setTextAlignment(TextAlignment.CENTER).
                    setMargins(0, 0, 0, 0);
            add(p);
        }
        content[1].setMarginTop(-4);

        setMargins(0, 0, 0, 0);
    }

}
