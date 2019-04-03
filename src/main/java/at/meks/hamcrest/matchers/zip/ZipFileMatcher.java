package at.meks.hamcrest.matchers.zip;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class ZipFileMatcher extends TypeSafeMatcher<Path> {

    private final Path expectedContent;
    private final ZipFileComparator zipFileComparator = new ZipFileComparator();
    private ZipCompareResult result;
    private Exception occuredException;

    private ZipFileMatcher(Path expectedContent) {
        this.expectedContent = expectedContent;
    }

    /**
     * compares the content of 2 zip files. The name, content, size and last modification date is compared.
     * @param expected  the path to the zip file with the expected content
     * @return a configured instance of the matcher for the assertion
     */
    public static ZipFileMatcher matchesWithNameLastModifiedAndContent(Path expected) {
        return new ZipFileMatcher(expected);
    }

    public ZipFileMatcher ignoreLastModifiedDateOnDirectories(boolean ignore) {
        zipFileComparator.setIgnoreLastModifiedDateOnDirectories(ignore);
        return this;
    }

    public ZipFileMatcher ignoreLastModifiedDifference(TimeUnit unit, int count) {
        zipFileComparator.setIgnoreLastModifiedDifference(unit, count);
        return this;
    }

    @Override
    protected boolean matchesSafely(Path item) {
        try {
            result = zipFileComparator.assertZipEquals(item, expectedContent);
            return !result.hasDiffs();
        } catch (Exception e) {
            occuredException = e;
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("the content of the zips are equal by filenames, size, last modified and content");
    }

    @Override
    protected void describeMismatchSafely(Path item, Description mismatchDescription) {
        if (occuredException != null) {
            mismatchDescription.appendText(occuredException.getClass().getName()).appendText(": ")
                    .appendText(occuredException.getMessage()).appendText("\n")
                    .appendText(getStackTrace(occuredException));
        } else {
            mismatchDescription.appendText(result.getEntryDiffs().stream()
                    .sorted(Comparator.comparing(diff -> ofNullable(diff.getExpected())
                            .orElse(diff.getActual()).getName()))
                    .map(this::toCompareDiff)
                    .collect(Collectors.joining("\n")));
        }
    }

    private String toCompareDiff(EntryCompareResult compareResult) {
        StringBuilder diffText = new StringBuilder();
        if (compareResult.getActual() == null) {
            diffText.append("(-) ").append(compareResult.getExpected());
        } else if (compareResult.getExpected() == null) {
            diffText.append("(+) ").append(compareResult.getActual());
        } else {
            diffText.append("(-) ").append(compareResult.getExpected()).append("\n");
            diffText.append("(+) ").append(compareResult.getActual());
        }
        return diffText.toString();
    }

    private String getStackTrace(Exception occuredException) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        occuredException.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
