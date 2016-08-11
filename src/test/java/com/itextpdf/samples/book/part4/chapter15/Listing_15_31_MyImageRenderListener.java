/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.book.part4.chapter15;


import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;


public class Listing_15_31_MyImageRenderListener implements IEventListener {

    protected String path;

    protected String extension;

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
                    byte[] imageByte = image.getImageBytes(true);
                    extension = image.identifyImageFileExtension();
                    filename = String.format(path, image.getPdfObject().getIndirectReference().getObjNumber(), extension);
                    os = new FileOutputStream(filename);
                    os.write(imageByte);
                    os.flush();
                    os.close();
                } catch (com.itextpdf.io.IOException | IOException e) {
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
