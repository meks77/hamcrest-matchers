#lang:en
Feature: A hamcrest matcher which can be used to verify that 2 zip files are equal
  The matcher provides a assertion report which can be used in in the idea to compare the result

  Background:
    Given expected zip is "expected.zip"

  Scenario: The 2 zip files are equal
    Given actual zip is "actualEquals.zip"
    And expected zip is "expected.zip"
    When matcher is asked if files match
    Then the result contains the empty diff text
    And the matcher returns that they match

  Scenario: In the actual zip some files are missing
    Given actual zip is "actualContainsLessFiles.zip"
    When matcher is asked if files match
    Then the matcher returns that they do not match
    And the result contains the diff text:
      | (-) ComparedEntryData[name='dir1/dir1file1.txt                |
      | (-) ComparedEntryData[name='file1.txt                         |
      | (-) ComparedEntryData[name='dir1/subdir1/dir1subdir1file1.txt |
      | (-) ComparedEntryData[name='dir1/subdir2                      |

  Scenario: The actual zip contains more files then the expected
    Given actual zip is "actualContainsMoreFiles.zip"
    When matcher is asked if files match
    Then the matcher returns that they do not match
    And the result contains the diff text:
      | (+) ComparedEntryData[name='dir1/dir1file4.txt', hashOfFileContent=0, size=30, lastModifiedDate=2019-03-21T22:36:06] |
      | (+) ComparedEntryData[name='dir3/file2.txt', hashOfFileContent=0, size=12, lastModifiedDate=2019-03-21T22:35:08]     |

  Scenario: In the actual zip some file differ to the expected
    Given actual zip is "actualSomeFilesDiffer.zip"
    When matcher is asked if files match
    Then the matcher returns that they do not match
    Then the result contains the diff text:
      | (-) ComparedEntryData[name='dir1/dir1file2.txt', hashOfFileContent=1163207965, size=24, lastModifiedDate=2019-03-21T22:35:58]                |
      | (+) ComparedEntryData[name='dir1/dir1file2.txt', hashOfFileContent=1163207965, size=24, lastModifiedDate=2019-03-21T22:40:50]                |
      | (-) ComparedEntryData[name='dir1/subdir1/dir1subdir1file1.txt', hashOfFileContent=1726840705, size=36, lastModifiedDate=2019-03-21T22:36:50] |
      | (+) ComparedEntryData[name='dir1/subdir1/dir1subdir1file1.txt', hashOfFileContent=-806727547, size=39, lastModifiedDate=2019-03-21T22:42:18] |
      | (-) ComparedEntryData[name='dir1/subdir2', hashOfFileContent=0, size=0, lastModifiedDate=2019-03-23T23:16:42.848]                           |

  Scenario: The expected file doesn't exist
    Given not existing expected zip is "notExisting.zip"
    And actual zip is "actualEquals.zip"
    When matcher is asked if files match
    And the result contains the diff text:
      | java.io.FileNotFoundException: notExisting.zip |

  Scenario: The expected file created with 7zip differs from the actual file, created with java, just by the last modification date
  Therefor for each file 2 lines should appear, the missing with the date from the expected, and the one to much of the actual zip.
    Given expected zip is "expectedZipCreatedWith7zip.zip"
    And actual zip is "actualZipCreatedWithJava.zip"
    When matcher is asked if files match
    And the result contains the diff text:
      | (-) ComparedEntryData[name='backupset1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:24.263]                            |
      | (+) ComparedEntryData[name='backupset1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:26]                                |
      | (-) ComparedEntryData[name='backupset1\file1.txt', hashOfFileContent=196203805, size=12, lastModifiedDate=2019-03-08T06:57:54.724]                                       |
      | (+) ComparedEntryData[name='backupset1\file1.txt', hashOfFileContent=196203805, size=12, lastModifiedDate=2019-03-08T06:57:56]                                           |
      | (-) ComparedEntryData[name='backupset1\file2.txt', hashOfFileContent=367942806, size=12, lastModifiedDate=2019-03-08T06:57:43.799]                                       |
      | (+) ComparedEntryData[name='backupset1\file2.txt', hashOfFileContent=367942806, size=12, lastModifiedDate=2019-03-08T06:57:44]                                           |
      | (-) ComparedEntryData[name='backupset1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:54.736]                                     |
      | (+) ComparedEntryData[name='backupset1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:56]                                         |
      | (-) ComparedEntryData[name='backupset1\set1subdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:24.263]                |
      | (+) ComparedEntryData[name='backupset1\set1subdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:26]                    |
      | (-) ComparedEntryData[name='backupset1\set1subdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:54.736]                         |
      | (+) ComparedEntryData[name='backupset1\set1subdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:56]                             |
      | (-) ComparedEntryData[name='backupset1\set1subdir1\set1subsubdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:24.263] |
      | (+) ComparedEntryData[name='backupset1\set1subdir1\set1subsubdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:26]     |
      | (-) ComparedEntryData[name='backupset1\set1subdir1\set1subsubdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:54.736]          |
      | (+) ComparedEntryData[name='backupset1\set1subdir1\set1subsubdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:56]              |
      | (-) ComparedEntryData[name='backupset2\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:24.263]                            |
      | (+) ComparedEntryData[name='backupset2\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:26]                                |
      | (-) ComparedEntryData[name='backupset2\file1.txt', hashOfFileContent=-319519678, size=12, lastModifiedDate=2019-03-08T07:03:13.567]                                      |
      | (+) ComparedEntryData[name='backupset2\file1.txt', hashOfFileContent=-319519678, size=12, lastModifiedDate=2019-03-08T07:03:14]                                          |
      | (-) ComparedEntryData[name='backupset2\file2.txt', hashOfFileContent=985522389, size=15, lastModifiedDate=2019-03-08T07:03:13.579]                                       |
      | (+) ComparedEntryData[name='backupset2\file2.txt', hashOfFileContent=985522389, size=15, lastModifiedDate=2019-03-08T07:03:14]                                           |
      | (-) ComparedEntryData[name='backupset2\file3.txt', hashOfFileContent=580289438, size=12, lastModifiedDate=2019-03-08T07:03:13.59]                                        |
      | (+) ComparedEntryData[name='backupset2\file3.txt', hashOfFileContent=580289438, size=12, lastModifiedDate=2019-03-08T07:03:14]                                           |
      | (-) ComparedEntryData[name='backupset2\set2subdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:24.263]                |
      | (+) ComparedEntryData[name='backupset2\set2subdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:26]                    |
      | (-) ComparedEntryData[name='backupset2\set2subdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:54.736]                         |
      | (+) ComparedEntryData[name='backupset2\set2subdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:56]                             |
      | (-) ComparedEntryData[name='backupset2\set2subdir1\set2subsubdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:24.263] |
      | (+) ComparedEntryData[name='backupset2\set2subdir1\set2subsubdir1\.clientServerBackup\file1.txt', hashOfFileContent=1, size=0, lastModifiedDate=2019-03-08T06:59:26]     |
      | (-) ComparedEntryData[name='backupset2\set2subdir1\set2subsubdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:54.736]          |
      | (+) ComparedEntryData[name='backupset2\set2subdir1\set2subsubdir1\file3.txt', hashOfFileContent=-1046394121, size=12, lastModifiedDate=2019-03-08T06:57:56]              |

  Scenario: The expected file created with 7zip differs from the actual file, created with java, just by the last modification date, but the date may differ by 2 seconds and the date diff of directories is ignored
  Therefor for each file 2 lines should appear, the missing with the date from the expected, and the one to much of the actual zip.
    Given expected zip is "expectedZipCreatedWith7zip.zip"
    And actual zip is "actualZipCreatedWithJava.zip"
    And the last modified diff may differ up to 2 seconds
    And the last modified diff of directories is ignored
    When matcher is asked if files match
    Then the result contains the empty diff text
    And the matcher returns that they match