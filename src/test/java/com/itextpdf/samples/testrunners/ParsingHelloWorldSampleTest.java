package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class ParsingHelloWorldSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part4.chapter15.Listing_15_20_ParsingHelloWorld");

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

        String[] txtToCompare = getStringArrayField(sampleClass, "TEXT");
        for (String txtDest : txtToCompare) {
            String txtCmp = getCmpPdf(txtDest);

            addError(compareTxt(txtDest, txtCmp));
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

    private String compareTxt(String dest, String cmp) throws IOException {
        String errorMessage = null;

        try (
                BufferedReader destReader = new BufferedReader(new FileReader(dest));
                BufferedReader cmpReader = new BufferedReader(new FileReader(cmp))
        ) {
            int lineNumber = 1;
            String destLine = destReader.readLine();
            String cmpLine = cmpReader.readLine();
            while (destLine != null || cmpLine != null) {
                if (destLine == null || cmpLine == null) {
                    errorMessage = "The number of lines is different\n";
                    break;
                }

                if (!destLine.equals(cmpLine)) {
                    errorMessage = "Txt files differ at line " + lineNumber
                            + "\n See difference: cmp file: \"" + cmpLine + "\"\n"
                            + "target file: \"" + destLine + "\n";
                }

                destLine = destReader.readLine();
                cmpLine = cmpReader.readLine();
                lineNumber++;
            }
        }

        return errorMessage;
    }
}
