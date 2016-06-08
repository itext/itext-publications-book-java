/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter14;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Listing_14_11_PearExample extends JPanel {
    protected Ellipse2D.Double circle;
    protected Ellipse2D.Double oval;
    protected Ellipse2D.Double leaf;
    protected Ellipse2D.Double stem;

    protected Area circ;
    protected Area ov;
    protected Area leaf1;
    protected Area leaf2;
    protected Area st1;
    protected Area st2;

    private static final long serialVersionUID = 1251626928914650961L;

    public Listing_14_11_PearExample() {
        circle = new Ellipse2D.Double();
        oval = new Ellipse2D.Double();
        leaf = new Ellipse2D.Double();
        stem = new Ellipse2D.Double();
        circ = new Area(circle);
        ov = new Area(oval);
        leaf1 = new Area(leaf);
        leaf2 = new Area(leaf);
        st1 = new Area(stem);
        st2 = new Area(stem);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        double ew = 75;
        double eh = 75;

        g2.setColor(Color.GREEN);

        // Creates the first leaf by filling the intersection of two Area
        // objects created from an ellipse.
        leaf.setFrame(ew - 16, eh - 29, 15.0, 15.0);
        leaf1 = new Area(leaf);
        leaf.setFrame(ew - 14, eh - 47, 30.0, 30.0);
        leaf2 = new Area(leaf);
        leaf1.intersect(leaf2);
        g2.fill(leaf1);

        // Creates the second leaf.
        leaf.setFrame(ew + 1, eh - 29, 15.0, 15.0);
        leaf1 = new Area(leaf);
        leaf2.intersect(leaf1);
        g2.fill(leaf2);

        g2.setColor(Color.BLACK);

        // Creates the stem by filling the Area resulting from the subtraction
        // of two Area objects created from an ellipse.
        stem.setFrame(ew, eh - 42, 40.0, 40.0);
        st1 = new Area(stem);
        stem.setFrame(ew + 3, eh - 47, 50.0, 50.0);
        st2 = new Area(stem);
        st1.subtract(st2);
        g2.fill(st1);

        g2.setColor(Color.YELLOW);

        // Creates the pear itself by filling the Area resulting from the union
        // of two Area objects created by two different ellipses.
        circle.setFrame(ew - 25, eh, 50.0, 50.0);
        oval.setFrame(ew - 19, eh - 20, 40.0, 70.0);
        circ = new Area(circle);
        ov = new Area(oval);
        circ.add(ov);
        g2.fill(circ);
    }

    public static void main(String[] args) {
        Listing_14_11_PearExample pear = new Listing_14_11_PearExample();
        JFrame f = new JFrame("Pear");
        f.getContentPane().add( pear, "Center" );

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(new Dimension(160, 200));
        f.setVisible(true);
    }
}

