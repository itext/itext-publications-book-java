/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Listing_14_22_TextExample4 {
    /** A String that needs to be displayed in a PDF document */
    public static final String[] AKIRA = {
            "Akira Kurosawa", " (", "\u9ed2\u6fa4 \u660e", " or ",
            "\u9ed2\u6ca2 \u660e", ", Kurosawa Akira, 23 March 1910" +
            " - 6 September 1998) was a Japanese film director," +
            " producer, screenwriter and editor. In a career that" +
            " spanned 50 years, Kurosawa directed 30 films. " +
            "He is widely regarded as one of the most important and " +
            "influential filmmakers in film history." };

    /** Styles that will be used in the AKIRA String. */
    public static final String[] STYLES =  {
            "bold", "regular", "japanese", "regular", "japanese", "regular" };

    public static JTextPane createTextPane() throws BadLocationException {
        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();
        initStyles(doc);
        for (int i=0; i < AKIRA.length; i++) {
            doc.insertString(doc.getLength(), AKIRA[i],
                    doc.getStyle(STYLES[i]));
        }
        return textPane;
    }

    public static void initStyles(StyledDocument doc) {
        Style def =
                StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontFamily(def, "SansSerif");
        Style regular = doc.addStyle("regular", def);
        Style bold = doc.addStyle("bold", regular);
        StyleConstants.setBold(bold, true);
        Style japanese = doc.addStyle("japanese", def);
        StyleConstants.setFontFamily(japanese, "MS PGothic");
    }

    public static void main(String[] args) throws BadLocationException {
        JFrame f = new JFrame("Kurosawa");
        f.getContentPane().add( createTextPane(), "Center" );

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(new Dimension(300, 150));
        f.setVisible(true);
    }
}