package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
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
public class KubrickDvdsSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter16.Listing_16_05_KubrickDvds");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
        try (FileInputStream coreLicense = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.json")) {
            LicenseKey.loadLicenseFile(coreLicense);
        }
        runSamples();
        LicenseKey.unloadLicenses();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(dest, cmp));

        List<String> imagesToCompareList = new ArrayList<>();
        imagesToCompareList.add("0048254.jpg");
        imagesToCompareList.add("0049406.jpg");
        imagesToCompareList.add("0050825.jpg");
        imagesToCompareList.add("0054331.jpg");
        imagesToCompareList.add("0056193.jpg");
        imagesToCompareList.add("0057012.jpg");
        imagesToCompareList.add("0062622.jpg");
        imagesToCompareList.add("0066921.jpg");
        imagesToCompareList.add("0072684.jpg");
        imagesToCompareList.add("0081505.jpg");
        imagesToCompareList.add("0093058.jpg");
        imagesToCompareList.add("0120663.jpg");
        imagesToCompareList.add("0278736.jpg");

        String imgPath = getStringField(sampleClass, "PATH");
        for (String img : imagesToCompareList) {
            String currentDest = String.format(imgPath, img);
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
