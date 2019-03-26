package at.meks.hamcrest.matchers.zip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class ZipFileComparator {

    ZipCompareResult assertZipEquals(Path actual, Path expected) throws IOException  {
        ZipFile actualZipFile = new ZipFile(actual.toFile());
        ZipFile expectedZipFile = new ZipFile(expected.toFile());

        ZipFileDescriptor actualZipFileDescriptor = new ZipFileDescriptor(actualZipFile);
        ZipFileDescriptor expectedZipFileDescriptor = new ZipFileDescriptor(expectedZipFile);

        return getDiff(actualZipFileDescriptor, expectedZipFileDescriptor);
    }

    private ZipCompareResult getDiff(ZipFileDescriptor actualZip, ZipFileDescriptor expectedZip) throws IOException {
        ZipCompareResult result = new ZipCompareResult();
        actualZip.entries.values().stream()
                .filter(entry -> !expectedZip.entries.containsKey(entry.getName()))
                .map(this::toNotExpectedResult)
                .forEach(result::addEntryResult);
        expectedZip.entries.values().stream()
                .filter(entry -> !actualZip.entries.containsKey(entry.getName()))
                .map(this::toMissingResult)
                .forEach(result::addEntryResult);


        for (String key : actualZip.entries.keySet().stream().filter(expectedZip.entries::containsKey).collect(Collectors.toList())) {
            ZipEntry actualEntry = actualZip.entries.get(key);
            ZipEntry expectedEntry = expectedZip.entries.get(key);

            ComparedEntryData actualData = toComparedData(actualEntry, getHashOfContent(actualZip, key));
            ComparedEntryData expectedData = toComparedData(expectedEntry, getHashOfContent(expectedZip, key));
            if (!actualData.equals(expectedData)) {
                result.addEntryResult(new EntryCompareResult(actualData, expectedData));
            }
        }
        return result;
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
        return ComparedEntryData.aComparedEntryData()
                .withName(entry.getName())
                .withSize(entry.getSize())
                .withLastModifiedDate(Date.from(entry.getLastModifiedTime().toInstant()))
                .build();
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



}