package com.itextpdf.samples.testrunners;

import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("SampleTest")
public class PageInformationSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig
                .addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter06.Listing_06_01_PageInformation");

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
        try (
                BufferedReader destReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest)));
                BufferedReader cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(cmp)))
        ) {
            String curDestStr;
            String curCmpStr;
            int row = 1;
            while ((curDestStr = destReader.readLine()) != null) {
                assertNotNull(curCmpStr = cmpReader.readLine(), "The lengths of files are different.");
                assertEquals(curCmpStr, curDestStr, "The files are different on the row " + row);
                row++;
            }
            assertNull(curCmpStr = cmpReader.readLine(), "The lengths of files are different.");
        }
    }
}
