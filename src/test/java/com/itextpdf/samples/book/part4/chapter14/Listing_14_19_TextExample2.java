/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class Listing_14_19_TextExample2 extends JPanel {
    public static final String AKIRA =
            "Akira Kurosawa (\u9ed2\u6fa4 \u660e or \u9ed2\u6ca2 \u660e, " +
                    "Kurosawa Akira, 23 March 1910 - 6 September 1998) was a " +
                    "Japanese film director, producer, screenwriter and editor. " +
                    "In a career that spanned 50 years, Kurosawa directed 30 films. " +
                    "He is widely regarded as one of the most important and " +
                    "influential filmmakers in film history.";

    /** Strings that need to be displayed in a PDF document */
    private static AttributedString akira;
    private static final long serialVersionUID = -3639324875232824761L;

    public Listing_14_19_TextExample2() {
        akira = new AttributedString(AKIRA);
        akira.addAttribute(TextAttribute.FONT, new Font("Arial Unicode MS", Font.PLAIN, 12));
    }

    public void paint(Graphics g) {
        LineBreakMeasurer lineMeasurer = null;
        int paragraphStart = 0;
        int paragraphEnd = 0;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        if (lineMeasurer == null) {
            AttributedCharacterIterator paragraph = akira.getIterator();
            paragraphStart = paragraph.getBeginIndex();
            paragraphEnd = paragraph.getEndIndex();
            FontRenderContext frc = g2d.getFontRenderContext();
            lineMeasurer = new LineBreakMeasurer(paragraph, frc);
        }
        float breakWidth = (float)getSize().width;
        float drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
        int start = 0;
        int end = 0;
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(breakWidth);
            drawPosY += layout.getAscent();
            end = start + layout.getCharacterCount();
            g2d.drawString(AKIRA.substring(start, end), 0, drawPosY);
            start = end;
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }

    public static void main(String[] args) {
        Listing_14_19_TextExample2 Kurosawa = new Listing_14_19_TextExample2();
        JFrame f = new JFrame("Kurosawa");
        f.getContentPane().add( Kurosawa, "Center" );

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(new Dimension(300, 150));
        f.setVisible(true);
    }
}