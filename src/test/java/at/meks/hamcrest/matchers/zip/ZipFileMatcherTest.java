package at.meks.hamcrest.matchers.zip;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "at.meks.hamcrest.matchers.zip", features = "src/test/resources/zipfilematcher")
public class ZipFileMatcherTest {

}