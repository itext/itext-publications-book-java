/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.ITextTest;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class MultiplePdfSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter08.Listing_08_29_ReaderEnabledForm");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter08.Listing_08_01_Buttons");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter08.Listing_08_07_TextFields");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter08.Listing_08_12_ChoiceFields");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter10.Listing_10_12_CompressImage");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter10.Listing_10_13_CompressAwt");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter10.Listing_10_18_ImageMask");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter11.Listing_11_06_FontFileAndSizes");
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter12");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter13.Listing_13_06_PageLayoutExample");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter13.Listing_13_14_FixBrokenForm");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter13.Listing_13_16_AddJavaScriptToForm");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter13.Listing_13_17_ReplaceURL");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter15.Listing_15_01_PeekABoo");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter16.Listing_16_04_EmbedFontPostFacto");

        // Sample are run by separate samples runner
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter12.Listing_12_09_EncryptionPdf");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part3.chapter12.Listing_12_11_EncryptWithCertificate");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        String[] destPathArray = getStringArrayField(sampleClass, "RESULT");
        for (String currentDest : destPathArray) {
            String currentCmp = getCmpPdf(currentDest);
            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
        }
    }

    private static String[] getStringArrayField(Class<?> c, String name) {
        try {
            Field field = c.getField(name);
            if (field == null) {
                return null;
            }

            Object obj = field.get(null);
            if (obj == null || !(obj instanceof String[])) {
                return null;
            }

            return (String[]) obj;
        } catch (Exception e) {
            return null;
        }
    }
}
