package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
public class XFAMovieSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter08.Listing_08_18_XfaMovie");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter08.Listing_08_28_XfaMovies");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
        try (FileInputStream coreLicense = new FileInputStream(System.getenv("ITEXT_LICENSE_FILE_LOCAL_STORAGE") + "/all-products.json")) {
            LicenseKey.loadLicenseFile(coreLicense);
        }
        runSamples();
        LicenseKey.unloadLicenses();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        String[] txtPathArray = getStringArrayField(sampleClass, "TXT_FILES");
        for (String destTxt : txtPathArray) {
            String cmpTxt = getCmpFilePath(destTxt, "txt");
            addError(compareTxt(destTxt, cmpTxt));
        }

        String[] xmlPathArray = getStringArrayField(sampleClass, "XML_FILES");
        for (String destXml : xmlPathArray) {
            String cmpXml = getCmpFilePath(destXml, "xml");
            if (!compareTool.compareXmls(destXml, cmpXml)) {
                addError("The XML structures are different.");
            }
        }

        String[] destPdfs = getStringArrayField(sampleClass, "RESULT");
        for (String pdf : destPdfs) {
            String currentCmp = getCmpPdf(pdf);
            addError(compareTool.compareByContent(pdf, currentCmp, outPath, "diff_"));
        }
    }

    protected static String[] getStringArrayField(Class<?> c, String name) {
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

    private String getCmpFilePath(String dest, String fileType) {
        return "./cmpfiles/" + fileType + "/cmp_" + dest.substring(dest.lastIndexOf("/") + 1);
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
