/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.testrunners;

import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class GenericSampleTest extends WrappedSamplesRunner {

    /**
     * List of samples, which require txt files comparison
     */
    private List<String> txtCompareList = Arrays.asList(
            "com.itextpdf.samples.book.part2.chapter06.Listing_06_18_FormInformation",
            "com.itextpdf.samples.book.part4.chapter13.Listing_13_15_InspectForm",
            "com.itextpdf.samples.book.part4.chapter15.Listing_15_25_ExtractPageContent",
            "com.itextpdf.samples.book.part4.chapter15.Listing_15_26_ExtractPageContentSorted1",
            "com.itextpdf.samples.book.part4.chapter15.Listing_15_27_ExtractPageContentSorted2",
            "com.itextpdf.samples.book.part4.chapter15.Listing_15_28_ExtractPageContentArea",
            "com.itextpdf.samples.book.part4.chapter16.Listing_16_03_ListUsedFonts"
    );

    /**
     * List of samples, which require xml files comparison
     */
    private static final List<String> xmlCompareList = Arrays.asList(
            "com.itextpdf.samples.book.part4.chapter15.Listing_15_19_ParseTaggedPdf"
    );

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.book");

        // Samples are run by separate samples runner
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part1.chapter01.Listing_01_13_HelloZip");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter06.Listing_06_01_PageInformation");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter06.Listing_06_02_MemoryInfo");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter07.Listing_07_02_LinkActions");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter07.Listing_07_14_CreateOutlineTree");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_18_XfaMovie");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_28_XfaMovies");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_01_Buttons");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_07_TextFields");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_12_ChoiceFields");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_29_ReaderEnabledForm");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_15_HtmlMovies1");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_16_HtmlMovies2");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter10.Listing_10_18_ImageMask");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter10.Listing_10_12_CompressImage");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter10.Listing_10_13_CompressAwt");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter11.Listing_11_06_FontFileAndSizes");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter12.Listing_12_01_MetadataPdf");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter12.Listing_12_04_MetadataXmp");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter12.Listing_12_06_HelloWorldCompression");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter12.Listing_12_09_EncryptionPdf");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter13.Listing_13_06_PageLayoutExample");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter13.Listing_13_11_PageLabelExample");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter13.Listing_13_13_Bookmarks2NamedDestinations");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter13.Listing_13_14_FixBrokenForm");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter13.Listing_13_16_AddJavaScriptToForm");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter13.Listing_13_17_ReplaceURL");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter14.Listing_14_01_GetContentStream");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_01_PeekABoo");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_20_ParsingHelloWorld");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_30_ExtractImages");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter16.Listing_16_04_EmbedFontPostFacto");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter16.Listing_16_05_KubrickDvds");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter16.Listing_16_06_KubrickDocumentary");

        // Not sample classes
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part1.chapter02.PipeSplitCharacter");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part1.chapter02.PositionedArrowTabRenderer");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part1.chapter02.StarSeparator");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter08.Listing_08_14_ChildFieldEvent");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_01_Hello");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_03_PdfServlet");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_07_ShowData");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_08_FormServlet");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_11_XFDFServlet");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_19_XmlHandler");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter09.Listing_09_20_MovieServlet");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter14.Listing_14_11_PearExample");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter14.Listing_14_16_TextExample1");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter14.Listing_14_19_TextExample2");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter14.Listing_14_21_TextExample3");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter14.Listing_14_22_TextExample4");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_17_StructureParser");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_18_ContentParser");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_24_MyTextRenderListener");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part4.chapter15.Listing_15_31_MyImageRenderListener");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 150000)
    public void test() throws Exception {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.xml");
        FontCache.clearSavedFonts();
        FontProgramFactory.clearRegisteredFonts();

        runSamples();

        unloadLicense();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        if (txtCompareList.contains(sampleClass.getName())) {
            addError(compareTxt(dest, cmp));
        } else if (xmlCompareList.contains(sampleClass.getName())) {
            if (!compareTool.compareXmls(dest, cmp)) {
                addError("The XML structures are different.");
            }
        } else {
            addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(dest, cmp));
        }
    }

    private String compareTxt(String dest, String cmp) throws IOException {
        String errorMessage = null;

        try (
                BufferedReader destReader = new BufferedReader(new FileReader(dest));
                BufferedReader cmpReader = new BufferedReader(new FileReader(cmp))
        ) {
            int lineNumber = 1;
            String destLine = destReader.readLine();
            String cmpLine = cmpReader.readLine();
            while (destLine != null || cmpLine != null) {
                if (destLine == null || cmpLine == null) {
                    errorMessage = "The number of lines is different\n";
                    break;
                }

                if (!destLine.equals(cmpLine)) {
                    errorMessage = "Txt files differ at line " + lineNumber
                            + "\n See difference: cmp file: \"" + cmpLine + "\"\n"
                            + "target file: \"" + destLine + "\n";
                }

                destLine = destReader.readLine();
                cmpLine = cmpReader.readLine();
                lineNumber++;
            }
        }

        return errorMessage;
    }

    private void unloadLicense() {
        try {
            Field validators = LicenseKey.class.getDeclaredField("validators");
            validators.setAccessible(true);
            validators.set(null, null);
            Field versionField = Version.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(null, null);
        } catch (Exception ignored) {

            // No exception handling required, because there can be no license loaded
        }
    }
}
