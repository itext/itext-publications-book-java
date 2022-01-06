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
import java.io.FileReader;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class MemoryInfoSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();

        // TODO DEVSIX-3691
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter06.Listing_06_02_MemoryInfo");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(dest))) {
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }
        }

        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(content.toString());
        matcher.find();
        int fullMemoryValue = Integer.parseInt(matcher.group());

        matcher.find();
        int partialMemoryValue = Integer.parseInt(matcher.group());

        System.out.println(fullMemoryValue + "-" + partialMemoryValue);
        Assert.assertTrue((fullMemoryValue != 0) && (partialMemoryValue != 0));
        Assert.assertTrue(fullMemoryValue > partialMemoryValue);
    }
}
