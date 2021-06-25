package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.action.PdfTarget;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfFileAttachmentAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.collection.PdfCollectionField;
import com.itextpdf.kernel.pdf.collection.PdfCollectionItem;
import com.itextpdf.kernel.pdf.collection.PdfCollectionSchema;
import com.itextpdf.kernel.pdf.collection.PdfCollectionSort;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitRemoteGoToDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ImageRenderer;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

public class Listing_16_11_KubrickCollection {
    public static final String DEST = "./target/book/part4/chapter16/Listing_16_11_KubrickCollection.pdf";
    public static final String RESOURCE = "./src/main/resources/pdfs/";
    public static final String IMG_BOX = "./src/main/resources/img/kubrick_box.jpg";
    public static final String IMG_KUBRICK = "./src/main/resources/img/kubrick.jpg";
    public static final String TYPE_FIELD = "TYPE";
    public static final String TYPE_CAPTION = "File type";
    public static final String FILE_FIELD = "FILE";
    public static final String FILE_CAPTION = "File name";
    public String[] KEYS = {TYPE_FIELD, FILE_FIELD};
    public static final PdfArray EMPTY_ANNOTATION_BORDER = new PdfArray(new int[]{0, 0, 0});

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_16_11_KubrickCollection().manipulatePdf(DEST);
    }

    public void manipulatePdf(String destination) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destination));
        Document doc = new Document(pdfDoc);
        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.HIDDEN);
        PdfCollectionSchema schema = getCollectionSchema();
        collection.setSchema(schema);
        PdfCollectionSort sort = new PdfCollectionSort(KEYS);
        collection.setSort(sort);
        pdfDoc.getCatalog().setCollection(collection);

        PdfCollectionItem collectionitem = new PdfCollectionItem(schema);
        PdfFileSpec fs;
        fs = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, IMG_KUBRICK, "Stanley Kubrick", "kubrick.jpg", null, null);
        collectionitem.addItem(TYPE_FIELD, "JPEG");
        fs.setCollectionItem(collectionitem);
        pdfDoc.addFileAttachment("Stanley Kubrick", fs);

        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        Image img = new Image(ImageDataFactory.create(IMG_BOX));
        doc.add(img);
        List list = new List();
        list.setSymbolIndent(20);
        PdfDestination dest = PdfExplicitRemoteGoToDestination.createFit(2);
        PdfTarget intermediate;
        PdfTarget target;
        Link link;
        ListItem item;
        PdfAction action = null;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> box = new TreeSet<Movie>();
        java.util.List<Movie> movies = PojoFactory.getMovies(connection, 1);
        box.addAll(movies);
        box.addAll(PojoFactory.getMovies(connection, 4));
        connection.close();
        for (Movie movie : box) {
            if (movie.getYear() > 1960) {
                out.print(String.format(
                        "%s;%s;%s\n", movie.getMovieTitle(), movie.getYear(), movie.getDuration()));
                item = new ListItem();
                Paragraph content = new Paragraph(movie.getMovieTitle());
                if (!"0278736".equals(movie.getImdb())) {
                    target = PdfTarget.createChildTarget(movie.getTitle());
                    intermediate = PdfTarget.createChildTarget(2, 1);
                    intermediate.setTarget(target);
                    action = PdfAction.createGoToE(dest, true, intermediate);
                    link = new Link(" (see info)", action);
                    link.getLinkAnnotation().setBorder(EMPTY_ANNOTATION_BORDER);
                    content.add(link);
                }
                item.add(content);
                list.add(item);
            }
        }
        out.flush();
        out.close();
        doc.add(list);
        fs = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, txt.toByteArray(),
                "Kubrick box: the movies", "kubrick.txt", null, null, null);
        collectionitem.addItem(TYPE_FIELD, "TXT");
        fs.setCollectionItem(collectionitem);
        pdfDoc.addFileAttachment("Kubrick box: the movies", fs);

        doc.add(new AreaBreak());
        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.setMarginBottom(10);
        Cell cell = new Cell().add(new Paragraph("All movies by Kubrick"));
        cell.setBorder(Border.NO_BORDER);
        fs = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, RESOURCE + "cmp_Listing_16_09_KubrickMovies.pdf",
                null, Listing_16_09_KubrickMovies.FILENAME, null, null);
        collectionitem.addItem(TYPE_FIELD, "PDF");
        fs.setCollectionItem(collectionitem);
        target = PdfTarget.createChildTarget("movies", "The movies of Stanley Kubrick");
        cell.setNextRenderer(new FileAttachmentRenderer(cell, "The movies of Stanley Kubrick", fs,
                true, PdfAction.createGoToE(dest, true, target), "movies"));
        table.addCell(cell);
        pdfDoc.addFileAttachment(Listing_16_09_KubrickMovies.FILENAME, fs);

        cell = new Cell().add(new Paragraph("Kubrick DVDs"));
        cell.setBorder(Border.NO_BORDER);
        fs = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, RESOURCE + "cmp_Listing_16_05_KubrickDvds.pdf",
                null, Listing_16_05_KubrickDvds.FILENAME, null, null);
        collectionitem.addItem(TYPE_FIELD, "PDF");
        fs.setCollectionItem(collectionitem);
        cell.setNextRenderer(new FileAttachmentRenderer(cell, "Kubrick DVDs", fs, false, null, null));

        table.addCell(cell);
        pdfDoc.addFileAttachment(Listing_16_05_KubrickDvds.FILENAME, fs);

        cell = new Cell().add(new Paragraph("Kubrick documentary"));
        cell.setBorder(Border.NO_BORDER);
        fs = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, RESOURCE + "cmp_Listing_16_06_KubrickDocumentary.pdf",
                null, Listing_16_06_KubrickDocumentary.FILENAME, null, null);
        collectionitem.addItem(TYPE_FIELD, "PDF");
        fs.setCollectionItem(collectionitem);
        cell.setNextRenderer(new FileAttachmentRenderer(cell, "Kubrick Documentary", fs, false, null, null));
        table.addCell(cell);
        pdfDoc.addFileAttachment(Listing_16_06_KubrickDocumentary.FILENAME, fs);

        doc.add(table);
        doc.close();
    }


    class FileAttachmentRenderer extends CellRenderer {
        protected boolean isExtendedEvent;
        protected PdfFileSpec fs;
        protected String description;

        protected PdfAction action;
        protected String top;

        public FileAttachmentRenderer(Cell modelElement, String description, PdfFileSpec fs,
                                      boolean isExtendedEvent, PdfAction action, String top) {
            super(modelElement);
            this.description = description;
            this.fs = fs;
            this.isExtendedEvent = isExtendedEvent;
            this.action = action;
            this.top = top;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new FileAttachmentRenderer((Cell) modelElement, description, fs, isExtendedEvent, action, top);
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            super.drawBorder(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            int pageNumber = getOccupiedArea().getPageNumber();
            PdfAnnotation annotation = new PdfFileAttachmentAnnotation(new Rectangle(rect.getLeft() - 20,
                    rect.getTop() - 15, 15, 10), fs);
            annotation.setName(new PdfString(description));
            if (isExtendedEvent) {
                drawContext.getDocument().getPage(pageNumber).addAnnotation(new PdfLinkAnnotation(rect).setAction(action).setBorder(EMPTY_ANNOTATION_BORDER));
                PdfArray array = new PdfArray();
                array.add(drawContext.getDocument().getPage(pageNumber).getPdfObject());
                array.add(PdfName.FitH);
                array.add(new PdfNumber(rect.getTop()));
                drawContext.getDocument().addNamedDestination(top, array);
            }
            drawContext.getDocument().getPage(pageNumber).addAnnotation(annotation);

        }
    }

    /**
     * Creates a Collection schema that can be used to organize the movies of Stanley Kubrick
     * in a collection: year, title, duration, DVD acquisition, filesize (filename is present, but hidden).
     *
     * @return a collection schema
     */
    private static PdfCollectionSchema getCollectionSchema() {
        PdfCollectionSchema schema = new PdfCollectionSchema();

        PdfCollectionField type = new PdfCollectionField(TYPE_CAPTION, PdfCollectionField.TEXT);
        type.setOrder(0);
        schema.addField(TYPE_FIELD, type);

        PdfCollectionField filename = new PdfCollectionField(FILE_FIELD, PdfCollectionField.FILENAME);
        filename.setOrder(1);
        schema.addField(FILE_FIELD, filename);

        return schema;
    }
}
