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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Category(SampleTest.class)
public class PageInformationSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig
                .addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter06.Listing_06_01_PageInformation");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        try (
                BufferedReader destReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest)));
                BufferedReader cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(cmp)))
        ) {
            String curDestStr;
            String curCmpStr;
            int row = 1;
            while ((curDestStr = destReader.readLine()) != null) {
                assertNotNull("The lengths of files are different.", curCmpStr = cmpReader.readLine());
                assertEquals("The files are different on the row " + row, curCmpStr, curDestStr);
                row++;
            }
            assertNull("The lengths of files are different.", curCmpStr = cmpReader.readLine());
        }
    }
}
