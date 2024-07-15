package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class ResizeImageSampleTest extends WrappedSamplesRunner {

    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter16.Listing_16_02_ResizeImage");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 150000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
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
