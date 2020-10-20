package com.itextpdf.samples.book.part4.chapter15;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.PdfUserPropertiesAttributes;
import com.itextpdf.kernel.pdf.tagging.PdfUserProperty;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.layout.element.Image;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Listing_15_11_ObjectData {
    public static final String DEST
            = "./target/book/part4/chapter15/Listing_15_11_ObjectData.pdf";
    public static final String RESOURCE
            = "./src/main/resources/img/posters/%s.jpg";
    public static final String SELECTDIRECTORS
            = "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
            + "FROM film_director d, film_movie_director md "
            + "WHERE d.id = md.director_id AND d.id < 8 "
            + "GROUP BY d.id, d.name, d.given_name ORDER BY id";

    public static void main(String args[]) throws IOException, SQLException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_15_11_ObjectData().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setTagged();
        pdfDoc.setUserProperties(true);

        pdfDoc.getStructTreeRoot().getRoleMap().put(new PdfName("Directors"), PdfName.H);
        for (int i = 1; i < 8; i++) {
            pdfDoc.getStructTreeRoot().getRoleMap().put(new PdfName("director" + i), PdfName.P);
        }

        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        TagTreePointer tagPointer = new TagTreePointer(pdfDoc);
        tagPointer.setPageForTagging(pdfDoc.addNewPage());
        tagPointer.addTag("Directors");

        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECTDIRECTORS);

        int id;
        Director director;
        while (rs.next()) {
            id = rs.getInt("id");
            director = PojoFactory.getDirector(rs);
            PdfUserPropertiesAttributes userproperties = new PdfUserPropertiesAttributes();
            userproperties.addUserProperty(new PdfUserProperty("Name", director.getName()));
            userproperties.addUserProperty(new PdfUserProperty("Given name", director.getGivenName()));
            userproperties.addUserProperty(new PdfUserProperty("Posters", rs.getInt("c")));
            tagPointer.addTag("director" + id).getProperties().addAttributes(userproperties);
            tagPointer.moveToParent();
        }

        Map<Movie, Integer> map = new TreeMap<Movie, Integer>();
        for (int i = 1; i < 8; i++) {
            List<Movie> movies = PojoFactory.getMovies(connection, i);
            for (Movie movie : movies) {
                map.put(movie, i);
            }
        }
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        ImageData img;
        float x = 11.5f;
        float y = 769.7f;
        for (Map.Entry<Movie, Integer> entry : map.entrySet()) {
            img = ImageDataFactory.create(String.format(RESOURCE, entry.getKey().getImdb()));
            Image image = new Image(img);
            tagPointer.moveToKid(entry.getValue() - 1);
            tagPointer.addTag(image.getAccessibilityProperties().getRole());
            canvas.openTag(tagPointer.getTagReference());
            canvas.addImageFittedIntoRectangle(img, new Rectangle(x + (45 - 30) / 2, y, 30, 46), false);
            canvas.closeTag();
            tagPointer.moveToParent();
            tagPointer.moveToParent();

            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        pdfDoc.close();
    }
}
