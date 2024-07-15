package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class Bookmarks2NamedDestinationsSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter13.Listing_13_13_Bookmarks2NamedDestinations");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
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
