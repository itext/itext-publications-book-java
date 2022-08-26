/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.testrunners;

import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class ExtractImagesSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter15.Listing_15_30_ExtractImages");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        List<SimpleEntry<String, String>> indexesAndExtensionsList = new ArrayList<>();
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("9", "jpg"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("10", "jp2"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("11", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("12", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("18", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("19", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("22", "jbig2"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("23", "png"));

        for (SimpleEntry<String, String> entry : indexesAndExtensionsList) {
            String currentDest = String.format(dest, entry.getKey(), entry.getValue());
            String currentCmp = getImageCmp(currentDest);

            addError(compareFiles(currentDest, currentCmp));
        }
    }

    private String getImageCmp(String dest) {
        return "./cmpfiles/img" + dest.substring(dest.lastIndexOf("/"));
    }

    private String compareFiles(String dest, String cmp) throws IOException {
        String errorMessage = null;

        RandomAccessFile file = new RandomAccessFile(dest, "r");
        byte[] destBytes = new byte[(int) file.length()];
        file.readFully(destBytes);
        file.close();

        file = new RandomAccessFile(cmp, "r");
        byte[] cmpBytes = new byte[(int) file.length()];
        file.readFully(cmpBytes);
        file.close();

        try {
            Assert.assertArrayEquals(cmpBytes, destBytes);
        } catch (AssertionError exc) {
            errorMessage = "Files are not equal.";
        }

        return errorMessage;
    }
}
