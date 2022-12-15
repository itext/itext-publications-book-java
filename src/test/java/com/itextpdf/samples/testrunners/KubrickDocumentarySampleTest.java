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

import java.util.Collection;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class KubrickDocumentarySampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter16.Listing_16_06_KubrickDocumentary");

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

        String xmlDest = dest.substring(0, dest.lastIndexOf("/")) + "/kubrick.xml";
        String xmlCmp = getCmpFilePath(xmlDest);
        if (!compareTool.compareXmls(xmlDest, xmlCmp)) {
            addError("The XML structures are different.");
        }
    }

    private String getCmpFilePath(String dest) {
        return "./cmpfiles/xml" + dest.substring(dest.lastIndexOf("/"));
    }
}
