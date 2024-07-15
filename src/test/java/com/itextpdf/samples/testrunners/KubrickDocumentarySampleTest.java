package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class KubrickDocumentarySampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter16.Listing_16_06_KubrickDocumentary");

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
