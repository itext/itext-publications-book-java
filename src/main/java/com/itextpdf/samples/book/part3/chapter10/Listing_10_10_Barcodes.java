package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.Barcode1D;
import com.itextpdf.barcodes.Barcode39;
import com.itextpdf.barcodes.BarcodeCodabar;
import com.itextpdf.barcodes.BarcodeDataMatrix;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.barcodes.BarcodeEANSUPP;
import com.itextpdf.barcodes.BarcodeInter25;
import com.itextpdf.barcodes.BarcodePDF417;
import com.itextpdf.barcodes.BarcodePostnet;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Listing_10_10_Barcodes {
    public static final String DEST = "./target/book/part3/chapter10/Listing_10_10_Barcodes.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_10_10_Barcodes().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        Document doc = new Document(pdfDoc, new PageSize(340, 842));

        //EAN 13
        doc.add(new Paragraph("Barcode EAN.UCC-13"));
        BarcodeEAN codeEAN = new BarcodeEAN(pdfDoc);
        codeEAN.setCode("4512345678906");
        doc.add(new Paragraph("default:"));
        doc.add(new Image(codeEAN.createFormXObject(pdfDoc)));
        codeEAN.setGuardBars(false);
        doc.add(new Paragraph("without guard bars:"));
        doc.add(new Image(codeEAN.createFormXObject(pdfDoc)));
        codeEAN.setBaseline(-1);
        codeEAN.setGuardBars(true);
        doc.add(new Paragraph("text above:"));
        doc.add(new Image(codeEAN.createFormXObject(pdfDoc)));
        codeEAN.setBaseline(codeEAN.getSize());

        //UPC A
        doc.add(new Paragraph("Barcode UCC-12 (UPC-A)"));
        codeEAN.setCodeType(BarcodeEAN.UPCA);
        codeEAN.setCode("785342304749");
        doc.add(new Image(codeEAN.createFormXObject(pdfDoc)));

        //EAN 8
        doc.add(new Paragraph("Barcode EAN.UCC-8"));
        codeEAN.setCodeType(BarcodeEAN.EAN8);
        codeEAN.setBarHeight(codeEAN.getSize() * 1.5f);
        codeEAN.setCode("34569870");
        doc.add(new Image(codeEAN.createFormXObject(pdfDoc)));

        //UPC E
        doc.add(new Paragraph("Barcode UPC-E"));
        codeEAN.setCodeType(BarcodeEAN.UPCE);
        codeEAN.setCode("03456781");
        doc.add(new Image(codeEAN.createFormXObject(pdfDoc)));
        codeEAN.setBarHeight(codeEAN.getSize() * 3);


        //EANSUPP
        doc.add(new Paragraph("Bookland - BarcodeEANSUPP"));
        doc.add(new Paragraph("ISBN 0-321-30474-8"));
        codeEAN = new BarcodeEAN(pdfDoc);
        codeEAN.setCodeType(BarcodeEAN.EAN13);
        codeEAN.setCode("9781935182610");
        BarcodeEAN codeSUPP = new BarcodeEAN(pdfDoc);
        codeSUPP.setCodeType(BarcodeEAN.SUPP5);
        codeSUPP.setCode("55999");
        codeSUPP.setBaseline(-2);
        BarcodeEANSUPP eanSupp = new BarcodeEANSUPP(codeEAN, codeSUPP);
        doc.add(new Image(eanSupp.createFormXObject(null, ColorConstants.BLUE, pdfDoc)));

        // CODE 128
        doc.add(new Paragraph("Barcode 128"));
        Barcode128 code128 = new Barcode128(pdfDoc);
        code128.setCode("0123456789 hello");
        doc.add(new Image(code128.createFormXObject(pdfDoc)));
        code128.setCode("0123456789\uffffMy Raw Barcode (0 - 9)");
        code128.setCodeType(Barcode128.CODE128_RAW);
        doc.add(new Image(code128.createFormXObject(pdfDoc)));

        // Data for the barcode :
        String code402 = "24132399420058289";
        String code90 = "3700000050";
        String code421 = "422356";
        StringBuffer data = new StringBuffer(code402);
        data.append(Barcode128.FNC1);
        data.append(code90);
        data.append(Barcode128.FNC1);
        data.append(code421);
        Barcode128 shipBarCode = new Barcode128(pdfDoc);
        shipBarCode.setX(0.75f);
        shipBarCode.setN(1.5f);
        shipBarCode.setSize(10f);
        shipBarCode.setTextAlignment(Barcode1D.ALIGN_CENTER);
        shipBarCode.setBaseline(10f);
        shipBarCode.setBarHeight(50f);
        shipBarCode.setCode(data.toString());
        doc.add(new Image(shipBarCode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLUE, pdfDoc)));

        // it is composed of 3 blocks whith AI 01, 3101 and 10
        Barcode128 uccEan128 = new Barcode128(pdfDoc);
        uccEan128.setCodeType(Barcode128.CODE128_UCC);
        uccEan128.setCode("(01)00000090311314(10)ABC123(15)060916");
        doc.add(new Image(uccEan128.createFormXObject(ColorConstants.BLUE, ColorConstants.BLACK, pdfDoc)));
        uccEan128.setCode("0191234567890121310100035510ABC123");
        doc.add(new Image(uccEan128.createFormXObject(ColorConstants.BLUE, ColorConstants.RED, pdfDoc)));
        uccEan128.setCode("(01)28880123456788");
        doc.add(new Image(uccEan128.createFormXObject(ColorConstants.BLUE, ColorConstants.BLACK, pdfDoc)));


        // INTER25
        doc.add(new Paragraph("Barcode Interrevealed 2 of 5"));
        BarcodeInter25 code25 = new BarcodeInter25(pdfDoc);
        code25.setGenerateChecksum(true);
        code25.setCode("41-1200076041-001");
        doc.add(new Image(code25.createFormXObject(pdfDoc)));
        code25.setCode("411200076041001");
        doc.add(new Image(code25.createFormXObject(pdfDoc)));
        code25.setCode("0611012345678");
        code25.setChecksumText(true);
        doc.add(new Image(code25.createFormXObject(pdfDoc)));


        // POSTNET
        doc.add(new Paragraph("Barcode Postnet"));
        BarcodePostnet codePost = new BarcodePostnet(pdfDoc);
        doc.add(new Paragraph("ZIP"));
        codePost.setCode("01234");
        doc.add(new Image(codePost.createFormXObject(pdfDoc)));
        doc.add(new Paragraph("ZIP+4"));
        codePost.setCode("012345678");
        doc.add(new Image(codePost.createFormXObject(pdfDoc)));
        doc.add(new Paragraph("ZIP+4 and dp"));
        codePost.setCode("01234567890");
        doc.add(new Image(codePost.createFormXObject(pdfDoc)));

        doc.add(new Paragraph("Barcode Planet"));
        BarcodePostnet codePlanet = new BarcodePostnet(pdfDoc);
        codePlanet.setCode("01234567890");
        codePlanet.setCodeType(BarcodePostnet.TYPE_PLANET);
        doc.add(new Image(codePlanet.createFormXObject(pdfDoc)));

        //CODE 39
        doc.add(new Paragraph("Barcode 3 of 9"));
        Barcode39 code39 = new Barcode39(pdfDoc);
        code39.setCode("ITEXT IN ACTION");
        doc.add(new Image(code39.createFormXObject(pdfDoc)));

        doc.add(new Paragraph("Barcode 3 of 9 extended"));
        Barcode39 code39ext = new Barcode39(pdfDoc);
        code39ext.setCode("iText in Action");
        code39ext.setStartStopText(false);
        code39ext.setExtended(true);
        doc.add(new Image(code39ext.createFormXObject(pdfDoc)));


        //CODABAR
        doc.add(new Paragraph("Codabar"));
        BarcodeCodabar codabar = new BarcodeCodabar(pdfDoc);
        codabar.setCode("A123A");
        codabar.setStartStopText(true);
        doc.add(new Image(codabar.createFormXObject(pdfDoc)));

        //PDF417
        doc.add(new Paragraph("Barcode PDF417"));
        BarcodePDF417 pdf417 = new BarcodePDF417();
        String text = "Call me Ishmael. Some years ago--never mind how long "
                + "precisely --having little or no money in my purse, and nothing "
                + "particular to interest me on shore, I thought I would sail about "
                + "a little and see the watery part of the world.";
        pdf417.setCode(text);

        PdfFormXObject xObject = pdf417.createFormXObject(pdfDoc);
        Image img = new Image(xObject);
        doc.add(img);


        doc.add(new Paragraph("Barcode Datamatrix"));
        BarcodeDataMatrix datamatrix = new BarcodeDataMatrix();
        datamatrix.setCode(text);
        Image imgDM = new Image(datamatrix.createFormXObject(pdfDoc));
        doc.add(imgDM);

        //QRCode
        doc.add(new Paragraph("Barcode QRCode"));
        BarcodeQRCode qrcode = new BarcodeQRCode("Moby Dick by Herman Melville");

        xObject = qrcode.createFormXObject(pdfDoc);
        img = new Image(xObject);
        doc.add(img);

        //Close document
        pdfDoc.close();
    }
}
