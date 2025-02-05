package com.itextpdf.samples.testrunners;

import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class ExtractImagesSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter15.Listing_15_30_ExtractImages");

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
        List<SimpleEntry<String, String>> indexesAndExtensionsList = new ArrayList<>();
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("9", "jpg"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("10", "jp2"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("11", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("12", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("18", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("19", "png"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("22", "jbig2"));
        indexesAndExtensionsList.add(new SimpleEntry<String, String>("23", "png"));

        for (SimpleEntry<String, String> entry : indexesAndExtensionsList) {
            String currentDest = String.format(dest, entry.getKey(), entry.getValue());
            String currentCmp = getImageCmp(currentDest);

            addError(compareFiles(currentDest, currentCmp));
        }
    }

    private String getImageCmp(String dest) {
        return "./cmpfiles/img" + dest.substring(dest.lastIndexOf("/"));
    }

    private String compareFiles(String dest, String cmp) throws IOException {
        String errorMessage = null;

        RandomAccessFile file = new RandomAccessFile(dest, "r");
        byte[] destBytes = new byte[(int) file.length()];
        file.readFully(destBytes);
        file.close();

        file = new RandomAccessFile(cmp, "r");
        byte[] cmpBytes = new byte[(int) file.length()];
        file.readFully(cmpBytes);
        file.close();

        try {
            assert Arrays.equals(cmpBytes, destBytes);
        } catch (AssertionError exc) {
            errorMessage = "Files are not equal.";
        }

        return errorMessage;
    }
}
