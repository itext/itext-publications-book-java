/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class ResizeImageSampleTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter16.Listing_16_02_ResizeImage");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 150000)
    public void test() throws Exception {
        runSamples();
    }

    @Override
    protected void runMain() throws IllegalAccessException, InvocationTargetException {
        Method sampleMethod;
        try {
            sampleMethod = sampleClass.getDeclaredMethod("manipulatePdf", String.class, boolean.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class must have manipulatePdf(String, boolean) method.");
        }

        Object instance;
        try {
            instance = sampleClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Sample class object couldn't be instantiated.");
        }

        File file = new File(getDest());
        file.getParentFile().mkdirs();

        sampleMethod.invoke(instance, getDest(), true);
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(dest, cmp));
    }
}
