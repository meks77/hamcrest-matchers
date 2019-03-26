package at.meks.hamcrest.matchers.zip;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.fest.assertions.api.Assertions.assertThat;

public class ZipFileComparatorSteps {

    private ZipFileComparator zipFileComparator = new ZipFileComparator();
    private ZipCompareResult compareResult;


    @When("(.*).zip is compared to (.*).zip")
    public void compareFiles(String actualFileName, String expectedFileName) throws URISyntaxException, IOException {
        compareResult = zipFileComparator.assertZipEquals(getFile(actualFileName), getFile(expectedFileName));
    }

    private Path getFile(String fileName) throws URISyntaxException {
        return Paths.get(getClass().getResource("/" + fileName + ".zip").toURI());
    }

    @Then("the result contains no diff")
    public void verifyNoDiff() {
        assertThat(compareResult).as("compare result").isNotNull();
        assertThat(compareResult.getEntryDiffs()).as("diffs").isEmpty();
        assertThat(compareResult.hasDiffs()).as("has diffs").isFalse();
    }

    @Then("the result contains the following missing files:")
    public void verifyAssertionsResulTo(DataTable dataTable){
        assertThat(compareResult).as("compare result").isNotNull();
        assertThat(compareResult.hasDiffs()).as("has diffs").isTrue();
        dataTable.unorderedDiff(DataTable.create(compareResult.getEntryDiffs().stream()
                .filter(entryCompareResult -> entryCompareResult.getActual() == null)
                .map(EntryCompareResult::getExpected)
                .map(ComparedEntryData::getName)
                .map(Collections::singletonList)
                .collect(Collectors.toList())));
    }

    @Then("the result contains the following files which shouldn't exist:")
    public void verifyNotExpectedFilesTo(DataTable dataTable) {
        assertThat(compareResult).as("compare result").isNotNull();
        assertThat(compareResult.hasDiffs()).as("has diffs").isTrue();
        dataTable.unorderedDiff(DataTable.create(compareResult.getEntryDiffs().stream()
                .filter(entryCompareResult -> entryCompareResult.getExpected() == null)
                .map(EntryCompareResult::getActual)
                .map(ComparedEntryData::getName)
                .map(Collections::singletonList)
                .collect(Collectors.toList())));
    }

    @Then("the result contains the following differences:")
    public void verifyDifferencesInResultTo(DataTable dataTable) {
        assertThat(compareResult).as("compare result").isNotNull();
        assertThat(compareResult.hasDiffs()).as("has diffs").isTrue();
        List<List<String>> actualDataTable = new LinkedList<>();
        actualDataTable.add(Arrays.asList("name", "actual last modified", "expected last modified", "actual size", "expected size", "actual content hash", "expected content hash"));
        actualDataTable.addAll(compareResult.getEntryDiffs().stream()
                .filter(entryCompareResult -> entryCompareResult.getExpected() != null)
                .filter(entryCompareResult -> entryCompareResult.getActual() != null)
                .map(this::toDataTableRow)
                .collect(Collectors.toList()));
        dataTable.unorderedDiff(DataTable.create(actualDataTable));
    }

    private List<String> toDataTableRow(EntryCompareResult entryCompareResult) {
        ComparedEntryData actual = entryCompareResult.getActual();
        ComparedEntryData expected = entryCompareResult.getExpected();

        List<String> dataTableRow = new ArrayList<>(7);
        dataTableRow.add(ofNullable(expected)
                .map(ComparedEntryData::getName)
                .orElse(ofNullable(actual)
                        .map(ComparedEntryData::getName)
                        .orElse("name null")));
        dataTableRow.add(getLastModified(actual));
        dataTableRow.add(getLastModified(expected));

        dataTableRow.add(getSize(actual));
        dataTableRow.add(getSize(expected));

        dataTableRow.add(getHashOfFileContent(actual));
        dataTableRow.add(getHashOfFileContent(expected));

        return dataTableRow;
    }

    private String getHashOfFileContent(ComparedEntryData actual) {
        return ofNullable(actual).map(ComparedEntryData::getHashOfFileContent).map(String::valueOf).orElse("");
    }

    private String getSize(ComparedEntryData actual) {
        return ofNullable(actual).map(ComparedEntryData::getSize).map(String::valueOf).orElse("");
    }

    private String getLastModified(ComparedEntryData actual) {
        return ofNullable(actual)
                .map(ComparedEntryData::getLastModifiedDate)
                .map(LocalDateTime::from)
                .map(dateTime -> dateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                .orElse("");
    }

}
