package com.itextpdf.samples.testrunners;

import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class MemoryInfoSampleTest extends WrappedSamplesRunner {
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();

        // TODO DEVSIX-3691
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.book.part2.chapter06.Listing_06_02_MemoryInfo");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    @Disabled("TODO DEVSIX-3691")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(dest))) {
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }
        }

        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(content.toString());
        matcher.find();
        int fullMemoryValue = Integer.parseInt(matcher.group());

        matcher.find();
        int partialMemoryValue = Integer.parseInt(matcher.group());

        System.out.println(fullMemoryValue + "-" + partialMemoryValue);
        assert (fullMemoryValue != 0) && (partialMemoryValue != 0);
        assert fullMemoryValue > partialMemoryValue;
    }
}
