package at.meks.hamcrest.matchers.zip;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.hamcrest.StringDescription;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.assertThat;

public class ZipFileMatcherSteps {


    private Path actual;
    private Path expected;
    private ZipFileMatcher matcher;
    private boolean matchesResult;
    private boolean ignoreDirectoriesLastModifiedDate;
    private TimeUnit ignoreLastModifiedDiffUnit;
    private int ignoreLastModifiedDiffCount;

    @Given("actual zip is {string}")
    public void setActualZip(String fileName) throws URISyntaxException {
        actual = getFile(fileName);
    }

    private Path getFile(String fileName) throws URISyntaxException {
        return Paths.get(getClass().getResource("/" + fileName).toURI());
    }

    @Given("expected zip is {string}")
    public void setExpectedZip(String fileName) throws URISyntaxException {
        expected = getFile(fileName);
    }

    @Given("not existing expected zip is {string}")
    public void setNotExistingExpectedZip(String fileName) {
        expected = Paths.get(fileName);
    }

    @Given("not existing actual zip is {string}")
    public void setNotExistingActualZip(String fileName) {
        actual = Paths.get(fileName);
    }

    @Given("the last modified diff may differ up to 2 seconds")
    public void setLastModifiedMayDifferBy2Seconds() {
        ignoreLastModifiedDiffUnit = TimeUnit.SECONDS;
        ignoreLastModifiedDiffCount = 2;
    }

    @Given("the last modified diff of directories is ignored")
    public void setLastModifiedDateDiffOfDirIsIgnroed() {
        this.ignoreDirectoriesLastModifiedDate = true;
    }

    @When("matcher is asked if files match")
    public void whenMatcherIsInvoked() {
        matcher = ZipFileMatcher.matchesWithNameLastModifiedAndContent(expected)
            .ignoreLastModifiedDateOnDirectories(ignoreDirectoriesLastModifiedDate);
        if (ignoreLastModifiedDiffUnit != null) {
            matcher.ignoreLastModifiedDifference(ignoreLastModifiedDiffUnit, ignoreLastModifiedDiffCount);
        }
        matchesResult = matcher.matches(actual);
    }

    @Then("the matcher returns that they match")
    public void assertMatcherReturnsTrue() {
        assertThat(matchesResult).isTrue();
    }

    @Then("the matcher returns that they do not match")
    public void assertMatcherReturnsFalse() {
        assertThat(matchesResult).isFalse();
    }

    @Then("the result contains the diff text:")
    public void assertActualText(DataTable dataTable) {
        StringDescription description = new StringDescription();
        matcher.describeMismatch(expected, description);
        String diffDescription = description.toString().replace("\\", "/");
        dataTable.asList().forEach(s -> assertThat(diffDescription).contains(s.replace("\\", "/")));
    }

    @Then("the result contains the empty diff text")
    public void assertActualTextIsEmpty() {
        StringDescription description = new StringDescription();
        matcher.describeMismatch(actual, description);
        assertThat(description.toString()).isEmpty();
    }

}