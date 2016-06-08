/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;


import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;


public class Listing_15_31_MyImageRenderListener implements IEventListener {

    protected String path;

    protected String extension;
    protected int i = 1;

    public Listing_15_31_MyImageRenderListener(String path) {
        this.path = path;
    }

    public void eventOccurred(IEventData data, EventType type) {
        switch (type) {
            case RENDER_IMAGE:
                try {
                    String filename;
                    FileOutputStream os;
                    ImageRenderInfo renderInfo = (ImageRenderInfo) data;
                    PdfImageXObject image = renderInfo.getImage();
                    if (image == null) {
                        return;
                    }
                    byte[] imageByte = null;
                    try {
                        imageByte = image.getImageBytes(true);
                        switch (ImageDataFactory.create(imageByte).getOriginalType()) {
                            case JPEG:
                                extension = "jpg";
                                break;
                            case PNG:
                                extension = "png";
                                break;
                            case GIF:
                                extension = "gif";
                                break;
                            case BMP:
                                extension = "bmp";
                                break;
                            case TIFF:
                                extension = "tif";
                                break;
                            case WMF:
                                extension = "wmf";
                                break;
                            case JPEG2000:
                                extension = "jp2";
                                break;
                            case JBIG2:
                                extension = "jbig2";
                                break;

                            default:
                                extension = "jpg";
                        }
                    } catch (com.itextpdf.io.IOException e) {
                        System.out.println(e.getMessage());
                    }
                    if (imageByte == null) {
                        return;
                    }
                    filename = String.format(path, i++, extension);
                    os = new FileOutputStream(filename);
                    os.write(imageByte);
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;

            default:
                break;
        }
    }

    public Set<EventType> getSupportedEvents() {
        return null;
    }
}
