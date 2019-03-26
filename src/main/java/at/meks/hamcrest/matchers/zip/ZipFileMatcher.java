package at.meks.hamcrest.matchers.zip;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

public class ZipFileMatcher extends TypeSafeMatcher<Path> {

    private final Path expectedContent;
    private final ZipFileComparator zipFileComparator = new ZipFileComparator();
    private ZipCompareResult result;
    private Exception occuredException;

    private ZipFileMatcher(Path expectedContent) {
        this.expectedContent = expectedContent;
    }

    public static ZipFileMatcher matchesWithNameLastModifiedAndContent(Path expected) {
        return new ZipFileMatcher(expected);
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
        if (occuredException == null) {
            description.appendText(
                    result.getEntryDiffs().stream()
                            .map(EntryCompareResult::getExpected)
                            .filter(Objects::nonNull)
                            .map(ComparedEntryData::toString)
                            .collect(Collectors.joining("\n")));
        }
    }

    @Override
    protected void describeMismatchSafely(Path item, Description mismatchDescription) {
        if (occuredException != null) {
            mismatchDescription.appendText(occuredException.getClass().getName()).appendText(": ")
                    .appendText(occuredException.getMessage()).appendText("\n")
                    .appendText(getStackTrace(occuredException));
        } else {
            mismatchDescription.appendText(result.getEntryDiffs().stream()
                    .map(EntryCompareResult::getActual)
                    .filter(Objects::nonNull)
                    .map(ComparedEntryData::toString)
                    .collect(Collectors.joining("\n")));
        }
    }

    private String getStackTrace(Exception occuredException) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        occuredException.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
