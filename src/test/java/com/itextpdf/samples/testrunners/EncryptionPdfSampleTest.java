/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class EncryptionPdfSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter12.Listing_12_09_EncryptionPdf");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        compareTool.enableEncryptionCompare();

        byte[] ownerPasswordBytes = getBytesArrayField(sampleClass, "OWNER");
        String[] destPathArray = getStringArrayField(sampleClass, "RESULT");
        for (String currentDest : destPathArray) {
            String currentCmp = getCmpPdf(currentDest);
            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_",
                    ownerPasswordBytes, ownerPasswordBytes));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp, ownerPasswordBytes, ownerPasswordBytes));
        }
    }

    private static byte[] getBytesArrayField(Class<?> c, String name) {
        try {
            Field field = c.getField(name);
            if (field == null) {
                return null;
            }

            Object obj = field.get(null);
            if (obj == null || !(obj instanceof byte[])) {
                return null;
            }

            return (byte[]) obj;
        } catch (Exception e) {
            return null;
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
