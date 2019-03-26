package at.meks.hamcrest.matchers.zip;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.fest.assertions.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Assert;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class ZipFileMatcherSteps {


    private Path actual;
    private Path expected;
    private ZipFileMatcher matcher;
    private boolean matchesResult;

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

    @When("matcher is asked if files match")
    public void whenMatcherIsInvoked() {
        matcher = ZipFileMatcher.matchesWithNameLastModifiedAndContent(expected);
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

    @Then("the result contains the expected text:")
    public void assertExpectedText(DataTable dataTable) {
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        dataTable.asList().forEach(s ->  assertThat(description.toString()).contains(s));
    }

    @Then("the result contains the actual text:")
    public void assertActualText(DataTable dataTable) {
        StringDescription description = new StringDescription();
        matcher.describeMismatch(expected, description);
        dataTable.asList().forEach(s ->  assertThat(description.toString()).contains(s));
    }

    @Then("the result contains the empty actual text")
    public void assertActualTextIsEmpty() {
        StringDescription description = new StringDescription();
        matcher.describeMismatch(actual, description);
        assertThat(description.toString()).isEmpty();
    }

    @Then("the result contains the empty expected text")
    public void assertExpectedTextIsEmpty() {
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        assertThat(description.toString()).isEmpty();
    }
}