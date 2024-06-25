package com.itextpdf.samples.testrunners;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class PdfAndXmlCompareSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.book.part2.chapter07.Listing_07_02_LinkActions");
        searchConfig.addClassToRunnerSearchPath(
                "com.itextpdf.samples.book.part2.chapter07.Listing_07_14_CreateOutlineTree");

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

        String xmlPath = getStringField(sampleClass, "DEST_XML");
        String cmpXmlPath = getCmpPdf(xmlPath);
        if (!compareTool.compareXmls(xmlPath, cmpXmlPath)) {
            addError("The XML structures are different.");
        }

        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(dest, cmp));
    }
}
