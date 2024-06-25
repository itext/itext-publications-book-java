package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class HelloZipSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part1.chapter01.Listing_01_13_HelloZip");

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
        ZipFile zf = new ZipFile(dest);
        for (int i = 1; i <= 3; i++) {
            InputStream in = zf.getInputStream(zf.getEntry("hello_" + i + ".pdf"));

            // Check only whether the created files can be opened or not
            try {
                new PdfDocument(new PdfReader(in));
            } catch (IOException e) {
                Assertions.fail();
            }
        }
    }
}
