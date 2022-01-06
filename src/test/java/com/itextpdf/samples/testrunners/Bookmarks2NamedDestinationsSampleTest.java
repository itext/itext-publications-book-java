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

import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class Bookmarks2NamedDestinationsSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter13.Listing_13_13_Bookmarks2NamedDestinations");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(dest, cmp));

        String currentDest = getStringField(sampleClass, "RESULT1");
        String currentCmp = getCmpPdf(currentDest);
        addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(currentDest, currentCmp));

        currentDest = getStringField(sampleClass, "RESULT2");
        currentCmp = getCmpPdf(currentDest);
        if (!compareTool.compareXmls(currentDest, currentCmp)) {
            addError("The XML structures are different.");
        }
    }
}
