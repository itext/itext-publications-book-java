package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class EncryptionPdfSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part3.chapter12.Listing_12_09_EncryptionPdf");

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
        compareTool.enableEncryptionCompare();

        byte[] ownerPasswordBytes = getBytesArrayField(sampleClass, "OWNER");
        String[] destPathArray = getStringArrayField(sampleClass, "RESULT");
        for (String currentDest : destPathArray) {
            String currentCmp = getCmpPdf(currentDest);
            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_",
                    ownerPasswordBytes, ownerPasswordBytes));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp, ownerPasswordBytes, ownerPasswordBytes));
        }
    }

    private static byte[] getBytesArrayField(Class<?> c, String name) {
        try {
            Field field = c.getField(name);
            if (field == null) {
                return null;
            }

            Object obj = field.get(null);
            if (obj == null || !(obj instanceof byte[])) {
                return null;
            }

            return (byte[]) obj;
        } catch (Exception e) {
            return null;
        }
    }

    private static String[] getStringArrayField(Class<?> c, String name) {
        try {
            Field field = c.getField(name);
            if (field == null) {
                return null;
            }

            Object obj = field.get(null);
            if (obj == null || !(obj instanceof String[])) {
                return null;
            }

            return (String[]) obj;
        } catch (Exception e) {
            return null;
        }
    }
}
