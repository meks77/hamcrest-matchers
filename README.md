[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=alert_status)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=bugs)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=code_smells)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=coverage)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=sqale_index)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=security_rating)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=at.meks%3Ahamcrest-matchers&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=at.meks%3Ahamcrest-matchers)
# hamcrest-matchers
A collection of matchers for hamcrest assertions.
Currently it's not started. But soon it will provide the possibility to assert the content of a zip file.

## Matchers

### Zip File matcher
In the first step only the file structure(directories and filenames), the size, last modified date and the content of the file are compared.
The diff of the content is only provided by an sha1 string. Providing the content a byte array or encoded string isn't that usefull.

#### Usage

Maven Dependency:
```xml
<dependency>
    <groupId>at.meks</groupId>
    <artifactId>hamcrest-matchers</artifactId>
    <version>${version}</version>
</dependency>
```
Pleas replace ${version} with the version you want to use.
```java
import at.meks.hamcrest.matchers.zip.ZipFileMatcher;

public class ExampleTest {
    
    @Test
    public void testZipFile() {
        Path expectedZip = Paths.get(getClass().getResource("/expected.zip").toURI());
        Path actualZip = Paths.get(getClass().getResource("/compared.zip").toURI());
        assertThat(actual, ZipFileMatcher.matchesWithNameLastModifiedAndContent(expected));
    }
    
}
```