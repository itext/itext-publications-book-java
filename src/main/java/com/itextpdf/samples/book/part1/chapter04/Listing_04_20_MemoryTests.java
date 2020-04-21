package com.itextpdf.samples.book.part1.chapter04;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;

public class Listing_04_20_MemoryTests {
    public static final String DEST =
            "./target/book/part1/chapter04/Listing_04_20_MemoryTests.pdf";
    public static final String RESOURCE = "./src/main/resources/img/posters/%s.jpg";
    public static final String RESULT0
            = "./target/book/part1/chapter04/Listing_04_20_MemoryTests_test_results.txt";
    public static final String RESULT1
            = "./target/book/part1/chapter04/Listing_04_20_MemoryTests_table_without_memory_management.pdf";


    protected PdfFont normal;
    protected PdfFont bold;
    protected PdfFont italic;
    protected PdfFont boldItalic;

    private boolean test;
    private long memory_use;
    private long initial_memory_use = 0l;
    private long maximum_memory_use = 0l;

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_04_20_MemoryTests().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        createPdfs();
    }

    public void createPdfs() {
        try {

            // The report file
            PrintWriter writer = new PrintWriter(new FileOutputStream(RESULT0));
            resetMaximum(writer);
            test = false;
            println(writer, RESULT1);

            // PDF without memory management
            createPdfWithPdfPTable(writer, RESULT1);
            resetMaximum(writer);
            test = true;
            println(writer, DEST);

            // PDF with memory management
            createPdfWithPdfPTable(writer, DEST);
            resetMaximum(writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a PDF with a table
     *
     * @param writerArg the writer to our report file
     * @param filename  the PDF that will be created
     * @throws IOException
     * @throws SQLException
     */
    private void createPdfWithPdfPTable(Writer writerArg, String filename) throws IOException, SQLException {

        // Create a connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        // Step 1
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        boldItalic = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLDOBLIQUE);

        // Step 4
        // Create a table with 2 columns
        Table table = new Table(new float[]{150, 600}, test);

        // In large table mode, we need to add it to the document at some point before flushing
        if (test) {
            doc.add(table);
        }

        java.util.List<Movie> movies = PojoFactory.getMovies(connection);
        List list;
        Cell cell;
        int count = 0;

        // Add information about a movie
        for (Movie movie : movies) {
            table.setMarginTop(5);

            // Add a movie poster
            cell = new Cell().add(new Image(ImageDataFactory.create(String.format(RESOURCE, movie.getImdb()))).scaleToFit(100,200));
            cell.setBorder(Border.NO_BORDER);
            cell.setMargin(10);
            table.addCell(cell);

            // Add movie information
            cell = new Cell();
            Paragraph p = new Paragraph(movie.getTitle()).setFont(bold);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setMarginTop(5);
            p.setMarginBottom(5);
            cell.add(p);
            cell.setBorder(Border.NO_BORDER);
            if (movie.getOriginalTitle() != null) {
                p = new Paragraph(movie.getOriginalTitle()).setFont(italic);
                p.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                cell.add(p);
            }
            list = PojoToElementFactory.getDirectorList(movie);
            list.setMarginLeft(30);
            cell.add(list);
            p = new Paragraph(String.format("Year: %d", movie.getYear())).setFont(normal);
            p.setMarginLeft(15);
            p.setFixedLeading(24);
            cell.add(p);
            p = new Paragraph(String.format("Run length: %d", movie.getDuration())).setFont(normal);
            p.setFixedLeading(14);
            p.setMarginLeft(30);
            cell.add(p);
            list = PojoToElementFactory.getCountryList(movie);
            list.setMarginLeft(40);
            cell.add(list);
            table.addCell(cell);

            // Insert a checkpoint every 20 movies
            if (count++ % 20 == 0) {

                // Add the incomplete table to the document
                if (test) {
                    table.flush();
                }
                checkpoint(writerArg);
            }
        }

        if (test) {

            // Mark the table as complete
            table.complete();
        } else {

            // Add the non-large table to the document
            doc.add(table);
        }

        // Insert a last checkpoint
        checkpoint(writerArg);

        // Step 5
        doc.close();
    }

    /**
     * Writes a checkpoint to the report file.
     *
     * @param writer the writer to our report file
     */
    private void checkpoint(Writer writer) {
        memory_use = getMemoryUse();
        maximum_memory_use = Math.max(maximum_memory_use, memory_use);
        println(writer, "memory use: ", memory_use);
    }

    /**
     * Resets the maximum memory that is in use
     *
     * @param writer the writer to our report file
     */
    private void resetMaximum(Writer writer) {
        println(writer, "maximum: ", maximum_memory_use);
        println(writer, "total used: ", maximum_memory_use - initial_memory_use);
        maximum_memory_use = 0l;
        initial_memory_use = getMemoryUse();
        println(writer, "initial memory use: ", initial_memory_use);
    }

    /**
     * Writes a line to our report file
     *
     * @param writer  the writer to our report file
     * @param message the message to write
     */
    private void println(Writer writer, String message) {
        try {
            writer.write(message);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a line to our report file
     *
     * @param writer  the writer to our report file
     * @param message the message to write
     * @param l       a memory value
     */
    private void println(Writer writer, String message, long l) {
        try {
            writer.write(message + l);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current memory use.
     *
     * @return the current memory use
     */
    private static long getMemoryUse() {
        garbageCollect();
        garbageCollect();
        long totalMemory = Runtime.getRuntime().totalMemory();
        garbageCollect();
        garbageCollect();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory);
    }

    /**
     * Makes sure all garbage is cleared from the memory.
     */
    private static void garbageCollect() {
        System.gc();
        System.runFinalization();
        System.gc();
        System.runFinalization();
    }
}
