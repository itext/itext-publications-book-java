package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.action.PdfTarget;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitRemoteGoToDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Listing_16_08_KubrickBox {
    public static final String DEST = "./target/book/part4/chapter16/Listing_16_08_KubrickBox.pdf";
    public static final String IMG_BOX = "./src/main/resources/img/kubrick_box.jpg";
    public static final String RESOURCE_FILES = "./src/main/resources/pdfs/%s.pdf";
    public static final String RESOURCE_PDFS_PREFIX = "16_08_";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";
    public static final float[] WIDTHS = {1, 7};

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_16_08_KubrickBox().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Image img = new Image(ImageDataFactory.create(IMG_BOX));
        doc.add(img);
        List list = new List();
        list.setSymbolIndent(20);
        PdfTarget target;
        Link link;
        ListItem item;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> box = new TreeSet<>();
        box.addAll(PojoFactory.getMovies(connection, 1));
        box.addAll(PojoFactory.getMovies(connection, 4));
        connection.close();
        for (Movie movie : box) {
            if (movie.getYear() > 1960) {
                pdfDoc.addFileAttachment(movie.getTitle(),
                        PdfFileSpec.createEmbeddedFileSpec(pdfDoc, String.format(RESOURCE_FILES, RESOURCE_PDFS_PREFIX + movie.getImdb()) /*createMoviePage(movie)*/, null,
                                String.format("kubrick_%s.pdf", movie.getImdb()), null, /*null, */null));
                item = new ListItem(movie.getMovieTitle());
                target = PdfTarget.createChildTarget(movie.getTitle());
                link = new Link(" (see info)",
                        PdfAction.createGoToE(PdfExplicitRemoteGoToDestination.createFit(1), true, target));
                item.add(new Paragraph(link));
                list.add(item);
            }
        }
        doc.add(list);
        doc.close();
    }

    // due to CompareTool reasons we do not call this method. However it was used to create RESOURCE_FILES pdfs.
    public byte[] createMoviePage(Movie movie) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        Paragraph p = new Paragraph(movie.getMovieTitle())
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_NOT_EMBEDDED))
                .setFontSize(16);
        doc.add(p);
        doc.add(new Paragraph("\n"));
        Table table = new Table(WIDTHS);
        table.addCell(new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb()))).setAutoScale(true));
        Cell cell = new Cell();
        cell.add(new Paragraph("Year: " + movie.getYear()));
        cell.add(new Paragraph("Duration: " + movie.getDuration()));
        table.addCell(cell);
        doc.add(table);
        PdfTarget target = PdfTarget.createParentTarget();
        Link link = new Link("Go to original document",
                PdfAction.createGoToE(PdfExplicitRemoteGoToDestination.createFit(1), false, target));
        doc.add(new Paragraph(link));
        doc.close();
        return baos.toByteArray();
    }
}
