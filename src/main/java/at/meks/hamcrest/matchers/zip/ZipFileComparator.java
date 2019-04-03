package at.meks.hamcrest.matchers.zip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class ZipFileComparator {

    private boolean ignoreLastModifiedDateOnDirectories;
    private TimeUnit ignoreLastModifiedDiffUnit;
    private int ignoreLastModifiedDiffCount;

    ZipCompareResult assertZipEquals(Path actual, Path expected) throws IOException  {
        ZipFile actualZipFile = new ZipFile(actual.toFile());
        ZipFile expectedZipFile = new ZipFile(expected.toFile());

        ZipFileDescriptor actualZipFileDescriptor = new ZipFileDescriptor(actualZipFile);
        ZipFileDescriptor expectedZipFileDescriptor = new ZipFileDescriptor(expectedZipFile);

        return getDiff(actualZipFileDescriptor, expectedZipFileDescriptor);
    }

    private ZipCompareResult getDiff(ZipFileDescriptor actualZip, ZipFileDescriptor expectedZip) throws IOException {
        ZipCompareResult result = new ZipCompareResult();
        actualZip.entries.entrySet().stream()
                .filter(entry -> !expectedZip.entries.containsKey(entry.getKey()))
                .map(entry -> toNotExpectedResult(entry.getValue()))
                .forEach(result::addEntryResult);
        expectedZip.entries.entrySet().stream()
                .filter(entry -> !actualZip.entries.containsKey(entry.getKey()))
                .map(entry -> toMissingResult(entry.getValue()))
                .forEach(result::addEntryResult);


        for (String key : actualZip.entries.keySet().stream().filter(expectedZip.entries::containsKey).collect(Collectors.toList())) {
            ZipEntry actualEntry = actualZip.entries.get(key);
            ZipEntry expectedEntry = expectedZip.entries.get(key);

            ComparedEntryData actualData = toComparedData(actualEntry, getHashOfContent(actualZip, key));
            ComparedEntryData expectedData = toComparedData(expectedEntry, getHashOfContent(expectedZip, key));
            if (!isDataMatching(actualEntry.isDirectory(), actualData, expectedData)) {
                result.addEntryResult(new EntryCompareResult(actualData, expectedData));
            }
        }
        return result;
    }

    private boolean isDataMatching(boolean directory, ComparedEntryData actualData, ComparedEntryData expectedData) {
        return Objects.equals(actualData.getSize(), expectedData.getSize()) &&
                Objects.equals(actualData.getHashOfFileContent(), expectedData.getHashOfFileContent()) &&
                ((directory && ignoreLastModifiedDateOnDirectories) || isLastModifiedWithinAllowedDiff(actualData, expectedData));
    }

    private boolean isLastModifiedWithinAllowedDiff(ComparedEntryData actualData, ComparedEntryData expectedData) {
        long timeLastModifiedActual = actualData.getLastModifiedDate().toEpochSecond(ZoneOffset.UTC);
        long timeLastModifiedExpected = expectedData.getLastModifiedDate().toEpochSecond(ZoneOffset.UTC);
        long diffLastModifiedSeconds = Math.abs(timeLastModifiedActual - timeLastModifiedExpected);
        if (diffLastModifiedSeconds == 0) {
            return true;
        } else if (ignoreLastModifiedDiffUnit != null) {
            return diffLastModifiedSeconds <= ignoreLastModifiedDiffUnit.toSeconds(ignoreLastModifiedDiffCount);
        }
        return false;
    }

    private int getHashOfContent(ZipFileDescriptor actualZip, String key) throws IOException {
        return Arrays.hashCode(getFileContent(actualZip, key));
    }

    private EntryCompareResult toMissingResult(ZipEntry entry) {
        return new EntryCompareResult(null, toComparedDataWithoutHash(entry));
    }

    private EntryCompareResult toNotExpectedResult(ZipEntry entry) {
        return new EntryCompareResult(toComparedDataWithoutHash(entry), null);
    }

    private ComparedEntryData toComparedDataWithoutHash(ZipEntry entry) {
        ComparedEntryData.ComparedEntryDataBuilder builder = ComparedEntryData.aComparedEntryData()
                .withName(toOsDirSeparator(entry))
                .withSize(entry.getSize())
                .withLastModifiedDate(Date.from(entry.getLastModifiedTime().toInstant()));
        return builder.build();
    }

    private String toOsDirSeparator(ZipEntry entry) {
        return Paths.get(entry.getName()).toString();
    }

    private ComparedEntryData toComparedData(ZipEntry entry, int contentHash) {
        ComparedEntryData data = toComparedDataWithoutHash(entry);
        data.setHashOfFileContent(contentHash);
        return data;
    }


    private byte[] getFileContent(ZipFileDescriptor zip, String key) throws IOException {
        ZipEntry entry = zip.entries.get(key);
        long size = entry.getSize();
        byte[] content = new byte[(int)size];
        try (InputStream inputStream = zip.zipFile.getInputStream(entry)) {
            inputStream.read(content, 0, (int) size);
        }
        return content;
    }


    void setIgnoreLastModifiedDateOnDirectories(boolean ignore) {
        ignoreLastModifiedDateOnDirectories = ignore;
    }

    void setIgnoreLastModifiedDifference(TimeUnit unit, int count) {
        ignoreLastModifiedDiffUnit = unit;
        ignoreLastModifiedDiffCount = count;
    }
}