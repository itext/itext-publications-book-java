/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

public class Listing_14_16_TextExample1 extends JPanel {
    /** A String that needs to be displayed in a PDF document */
    private static AttributedString akira;
    private static final long serialVersionUID = -3639324875232824761L;

    public Listing_14_16_TextExample1() {
        akira = new AttributedString(
                "Akira Kurosawa: \u9ed2\u6fa4 \u660e or \u9ed2\u6ca2 \u660e; " +
                        "23 March 1910 - 6 September 1998.");
        akira.addAttribute(TextAttribute.FONT, new Font("SansSerif", Font.PLAIN, 12));
        akira.addAttribute(TextAttribute.FONT, new Font("SansSerif", Font.BOLD, 12), 0, 15);
        akira.addAttribute(TextAttribute.FONT, new Font("MS PGothic", Font.PLAIN, 12), 16, 20);
        akira.addAttribute(TextAttribute.FONT, new Font("MS PGothic", Font.PLAIN, 12), 24, 28);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString(akira.getIterator(), 10, 16);
    }

    public static void main(String[] args) {
        Listing_14_16_TextExample1 Kurosawa = new Listing_14_16_TextExample1();
        JFrame f = new JFrame("Kurosawa");
        f.getContentPane().add( Kurosawa, "Center" );
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(new Dimension(500, 60));
        f.setVisible(true);
    }
}