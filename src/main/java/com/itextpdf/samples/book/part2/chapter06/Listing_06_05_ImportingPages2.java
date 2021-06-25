package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Listing_06_05_ImportingPages2 {
    public static final String DEST
            = "./target/book/part2/chapter06/Listing_06_05_ImportingPages2.pdf";
    public static final String MOVIE_TEMPLATES
            = "./src/main/resources/pdfs/cmp_Listing_03_29_MovieTemplates.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_06_05_ImportingPages2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize destination document
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(resultDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        PdfDocument srcDoc = new PdfDocument(new PdfReader(MOVIE_TEMPLATES));

        for (int i = 1; i <= srcDoc.getNumberOfPages(); i++) {
            PdfPage curPage = srcDoc.getPage(i);
            PdfFormXObject header = curPage.copyAsFormXObject(resultDoc);
            Cell cell = new Cell()
                    .add(new Image(header).setWidth(UnitValue.createPercentValue(100)).setAutoScaleWidth(true))
                    .setBorder(new SolidBorder(1));
            cell.setNextRenderer(new MyCellRenderer(cell));
            table.addCell(cell);
        }

        doc.add(table);

        resultDoc.close();
        srcDoc.close();
    }


    class MyCellRenderer extends CellRenderer {
        public MyCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new MyCellRenderer((Cell) modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            System.out.println(rect.getX());
            System.out.println(rect.getY());
            System.out.println(rect.getWidth());
            System.out.println(rect.getHeight());
            System.out.println();
        }
    }
}
