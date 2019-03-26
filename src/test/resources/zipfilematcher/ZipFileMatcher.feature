#lang:en
Feature: A hamcrest matcher which can be used to verify that 2 zip files are equal
  The matcher provides a assertion report which can be used in in the idea to compare the result

  Background:
    Given expected zip is "expected.zip"

  Scenario: The 2 zip files are equal
    Given actual zip is "actualEquals.zip"
    And expected zip is "expected.zip"
    When matcher is asked if files match
    Then the matcher returns that they match
    And the result contains the empty actual text
    And the result contains the empty expected text

  Scenario: In the actual zip some files are missing
    Given actual zip is "actualContainsLessFiles.zip"
    When matcher is asked if files match
    Then the matcher returns that they do not match
    And the result contains the expected text:
      | dir1/dir1file1.txt                |
      | file1.txt                         |
      | dir1/subdir1/dir1subdir1file1.txt |
      | dir1/subdir2/                     |

  Scenario: The actual zip contains more files then the expected
    Given actual zip is "actualContainsMoreFiles.zip"
    When matcher is asked if files match
    Then the matcher returns that they do not match
    And the result contains the actual text:
      | ComparedEntryData[name='dir1/dir1file4.txt', hashOfFileContent=0, size=30, lastModifiedDate=2019-03-21T22:36:06] |
      | ComparedEntryData[name='dir3/file2.txt', hashOfFileContent=0, size=12, lastModifiedDate=2019-03-21T22:35:08]     |

  Scenario: In the actual zip some file differ to the expected
    Given actual zip is "actualSomeFilesDiffer.zip"
    When matcher is asked if files match
    Then the matcher returns that they do not match
    Then the result contains the expected text:
      | ComparedEntryData[name='dir1/subdir2/', hashOfFileContent=0, size=0, lastModifiedDate=2019-03-23T23:16:42.848]                           |
      | ComparedEntryData[name='dir1/subdir1/dir1subdir1file1.txt', hashOfFileContent=1726840705, size=36, lastModifiedDate=2019-03-21T22:36:50] |
      | ComparedEntryData[name='dir1/dir1file2.txt', hashOfFileContent=1163207965, size=24, lastModifiedDate=2019-03-21T22:35:58]                |
    And the result contains the actual text:
      | ComparedEntryData[name='dir1/subdir1/dir1subdir1file1.txt', hashOfFileContent=-806727547, size=39, lastModifiedDate=2019-03-21T22:42:18] |
      | ComparedEntryData[name='dir1/dir1file2.txt', hashOfFileContent=1163207965, size=24, lastModifiedDate=2019-03-21T22:40:50]                |

  Scenario: The expected file doesn't exist
    Given not existing expected zip is "notExisting.zip"
    And actual zip is "actualEquals.zip"
    When matcher is asked if files match
    Then the result contains the empty expected text
    And the result contains the actual text:
      | java.io.FileNotFoundException: notExisting.zip |