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