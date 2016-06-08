/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_16_03_ListUsedFonts {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_03_ListUsedFonts.pdf";
    public static final String RESULT = "./target/test/resources/book/part4/chapter16/Listing_16_03_ListUsedFonts.txt";
    public static final String FONT_TYPES = "./src/test/resources/book/part3/chapter11/cmp_Listing_11_01_FontTypes.pdf";
    public static final String CMP_RESULT = "CMR10 (Type 1) embedded\n" +
            "FreeSans subset (ETQTMI)\n" +
            "FreeSans subset (IOFFKS)\n" +
            "Helvetica nofontdescriptor\n" +
            "IPAMincho subset (WPTCON)\n" +
            "KozMinPro-Regular-UniJIS-UCS2-H nofontdescriptor\n" +
            "Puritan2 (Type1) embedded\n";

    public static void main(String args[]) throws Exception {
        new Listing_16_03_ListUsedFonts().manipulatePdf();
    }

    @Test
    public void manipulatePdf() throws Exception {
        new File(RESULT).getParentFile().mkdirs();
        Set<String> set = listFonts(FONT_TYPES);
        StringBuffer buffer = new StringBuffer();
        for (String fontName : set) {
            buffer.append(fontName+"\n");
        }

        PrintWriter out = new PrintWriter(new FileOutputStream(RESULT));
        out.print(buffer.toString());
        out.flush();
        out.close();

        Assert.assertEquals(CMP_RESULT, buffer.toString());
    }

    /**
     * Creates a Set containing information about the fonts in the src PDF file.
     *
     * @param src the path to a PDF file
     * @throws IOException
     */
    public Set<String> listFonts(String src) throws IOException {
        Set<String> set = new TreeSet<>();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfDictionary resources;
        for (int k = 1; k <= pdfDoc.getNumberOfPages(); ++k) {
            resources = pdfDoc.getPage(k).getPdfObject().getAsDictionary(PdfName.Resources);
            processResource(set, resources);
        }
        pdfDoc.close();
        return set;
    }

    /**
     * Extracts the font names from page or XObject resources.
     *
     * @param set the set with the font names
     */
    public static void processResource(Set<String> set, PdfDictionary resource) {
        if (resource == null)
            return;
        PdfDictionary xobjects = resource.getAsDictionary(PdfName.XObject);
        if (xobjects != null) {
            for (PdfName key : xobjects.keySet()) {
                processResource(set, xobjects.getAsDictionary(key));
            }
        }
        PdfDictionary fonts = resource.getAsDictionary(PdfName.Font);
        if (fonts == null)
            return;
        PdfDictionary font;
        for (PdfName key : fonts.keySet()) {
            font = fonts.getAsDictionary(key);
            String name = font.getAsName(PdfName.BaseFont).toString();
            if (name.length() > 8 && name.charAt(7) == '+') {
                name = String.format("%s subset (%s)", name.substring(8), name.substring(1, 7));
            } else {
                name = name.substring(1);
                PdfDictionary desc = font.getAsDictionary(PdfName.FontDescriptor);
                if (desc == null)
                    name += " nofontdescriptor";
                else if (desc.get(PdfName.FontFile) != null)
                    name += " (Type 1) embedded";
                else if (desc.get(PdfName.FontFile2) != null)
                    name += " (TrueType) embedded";
                else if (desc.get(PdfName.FontFile3) != null)
                    name += " (" + font.getAsName(PdfName.Subtype).toString().substring(1) + ") embedded";
            }
            set.add(name);
        }
    }
}
