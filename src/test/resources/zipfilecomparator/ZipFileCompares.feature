Feature: What is the difference of 2 zip files?
  As developer you want to know if your created zip file contains the expected files. Those files are compared by
  * the name
  * the size
  * last modified time
  * hash of file content

  Scenario: The 2 zip files are equal
    When actualEquals.zip is compared to expected.zip
    Then the result contains no diff

  Scenario: In the actual zip some files are missing
    When actualContainsLessFiles.zip is compared to expected.zip
    Then the result contains the following missing files:
      | dir1/dir1file1.txt                |
      | file1.txt                         |
      | dir1/subdir1/dir1subdir1file1.txt |
      | dir1/subdir2/                     |

  Scenario: The actual zip contains more files then the expected
    When actualContainsMoreFiles.zip is compared to expected.zip
    Then the result contains the following files which shouldn't exist:
      | dir1/dir1file4.txt |
      | dir3/file2.txt     |

  Scenario: In the actual zip some file differ to the expected
    When actualSomeFilesDiffer.zip is compared to expected.zip
    Then the result contains the following differences:
      | name                              | actual last modified | expected last modified | actual size | expected size | actual content hash | expected content hash |
      | dir1/dir1file2.txt                | 2019-03-21T22:40:50  | 2019-03-21T22:35:58    | 24          | 24            | 1163207965          | 1163207965            |
      | dir1/subdir1/dir1subdir1file1.txt | 2019-03-21T22:42:18  | 2019-03-21T22:36:50    | 39          | 36            | -806727547          | 1726840705            |
    And the result contains the following missing files:
      | dir1/subdir2/ |

  Scenario: The expected zip is created with 7zip and differs to the actual zip, created by java, by the last modified date
    When actualZipCreatedWithJava.zip is compared to expectedZipCreatedWith7zip.zip
    Then the result contains the following differences:
      | name                                                                | actual last modified | expected last modified  | actual size | expected size | actual content hash | expected content hash |
      | backupset1                                                          | 2019-04-03T20:04:19  | 2019-03-08T07:01:43.279 | 0           | 0             | 1                   | 1                     |
      | backupset1/.clientServerBackup                                      | 2019-04-03T20:04:19  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/.clientServerBackup/file1.txt                            | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/file1.txt                                                | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.724 | 12          | 12            | 196203805           | 196203805             |
      | backupset1/file2.txt                                                | 2019-03-08T06:57:44  | 2019-03-08T06:57:43.799 | 12          | 12            | 367942806           | 367942806             |
      | backupset1/file3.txt                                                | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset1/set1subdir1                                              | 2019-04-03T20:04:19  | 2019-03-08T07:01:57.635 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/.clientServerBackup                          | 2019-04-03T20:04:19  | 2019-03-08T06:59:32.739 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/.clientServerBackup/file1.txt                | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/file3.txt                                    | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset1/set1subdir1/set1subsubdir1                               | 2019-04-03T20:04:19  | 2019-03-08T06:59:37.47  | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/set1subsubdir1/.clientServerBackup           | 2019-04-03T20:04:19  | 2019-03-08T06:59:37.485 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/set1subsubdir1/.clientServerBackup/file1.txt | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/set1subsubdir1/file3.txt                     | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset2                                                          | 2019-04-03T20:04:19  | 2019-03-08T07:03:13.605 | 0           | 0             | 1                   | 1                     |
      | backupset2/.clientServerBackup                                      | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.601 | 0           | 0             | 1                   | 1                     |
      | backupset2/.clientServerBackup/file1.txt                            | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset2/file1.txt                                                | 2019-03-08T07:03:14  | 2019-03-08T07:03:13.567 | 12          | 12            | -319519678          | -319519678            |
      | backupset2/file2.txt                                                | 2019-03-08T07:03:14  | 2019-03-08T07:03:13.579 | 15          | 15            | 985522389           | 985522389             |
      | backupset2/file3.txt                                                | 2019-03-08T07:03:14  | 2019-03-08T07:03:13.59  | 12          | 12            | 580289438           | 580289438             |
      | backupset2/set2subdir1                                              | 2019-04-03T20:04:19  | 2019-03-08T07:02:20.843 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/.clientServerBackup                          | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.618 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/.clientServerBackup/file1.txt                | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/file3.txt                                    | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset2/set2subdir1/set2subsubdir1                               | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.634 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/set2subsubdir1/.clientServerBackup           | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.642 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/set2subsubdir1/.clientServerBackup/file1.txt | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/set2subsubdir1/file3.txt                     | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
    And the result contains no missing files
    And the result contains no additional files

  Scenario: The expected zip is created with 7zip and differs to the actual zip, created by java, by the last modified date, but the directory modification date should be ignored
    Given directories last modified date is not compared
    When actualZipCreatedWithJava.zip is compared to expectedZipCreatedWith7zip.zip
    Then the result contains the following differences:
      | name                                                                | actual last modified | expected last modified  | actual size | expected size | actual content hash | expected content hash |
      | backupset1/.clientServerBackup/file1.txt                            | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/file1.txt                                                | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.724 | 12          | 12            | 196203805           | 196203805             |
      | backupset1/file2.txt                                                | 2019-03-08T06:57:44  | 2019-03-08T06:57:43.799 | 12          | 12            | 367942806           | 367942806             |
      | backupset1/file3.txt                                                | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset1/set1subdir1/.clientServerBackup/file1.txt                | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/file3.txt                                    | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset1/set1subdir1/set1subsubdir1/.clientServerBackup/file1.txt | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/set1subsubdir1/file3.txt                     | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset2/.clientServerBackup/file1.txt                            | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset2/file1.txt                                                | 2019-03-08T07:03:14  | 2019-03-08T07:03:13.567 | 12          | 12            | -319519678          | -319519678            |
      | backupset2/file2.txt                                                | 2019-03-08T07:03:14  | 2019-03-08T07:03:13.579 | 15          | 15            | 985522389           | 985522389             |
      | backupset2/file3.txt                                                | 2019-03-08T07:03:14  | 2019-03-08T07:03:13.59  | 12          | 12            | 580289438           | 580289438             |
      | backupset2/set2subdir1/.clientServerBackup/file1.txt                | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/file3.txt                                    | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
      | backupset2/set2subdir1/set2subsubdir1/.clientServerBackup/file1.txt | 2019-03-08T06:59:26  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/set2subsubdir1/file3.txt                     | 2019-03-08T06:57:56  | 2019-03-08T06:57:54.736 | 12          | 12            | -1046394121         | -1046394121           |
    And the result contains no missing files
    And the result contains no additional files

  Scenario: The expected zip is created with 7zip and differs to the actual zip, created by java, by the last modified date, but 2 seconds difference should be ignored
    Given the last modified diff can differ up to 2 seconds
    When actualZipCreatedWithJava.zip is compared to expectedZipCreatedWith7zip.zip
    Then the result contains the following differences:
      | name                                                                | actual last modified | expected last modified  | actual size | expected size | actual content hash | expected content hash |
      | backupset1                                                          | 2019-04-03T20:04:19  | 2019-03-08T07:01:43.279 | 0           | 0             | 1                   | 1                     |
      | backupset1/.clientServerBackup                                      | 2019-04-03T20:04:19  | 2019-03-08T06:59:24.263 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1                                              | 2019-04-03T20:04:19  | 2019-03-08T07:01:57.635 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/.clientServerBackup                          | 2019-04-03T20:04:19  | 2019-03-08T06:59:32.739 | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/set1subsubdir1                               | 2019-04-03T20:04:19  | 2019-03-08T06:59:37.47  | 0           | 0             | 1                   | 1                     |
      | backupset1/set1subdir1/set1subsubdir1/.clientServerBackup           | 2019-04-03T20:04:19  | 2019-03-08T06:59:37.485 | 0           | 0             | 1                   | 1                     |
      | backupset2                                                          | 2019-04-03T20:04:19  | 2019-03-08T07:03:13.605 | 0           | 0             | 1                   | 1                     |
      | backupset2/.clientServerBackup                                      | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.601 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1                                              | 2019-04-03T20:04:19  | 2019-03-08T07:02:20.843 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/.clientServerBackup                          | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.618 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/set2subsubdir1                               | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.634 | 0           | 0             | 1                   | 1                     |
      | backupset2/set2subdir1/set2subsubdir1/.clientServerBackup           | 2019-04-03T20:04:19  | 2019-03-08T07:00:33.642 | 0           | 0             | 1                   | 1                     |
    And the result contains no missing files
    And the result contains no additional files

  Scenario: The expected zip is created with 7zip and differs to the actual zip, created by java, by the last modified date, but directories diff and 2 seconds diff are ignored
    Given directories last modified date is not compared
    And the last modified diff can differ up to 2 seconds
    When actualZipCreatedWithJava.zip is compared to expectedZipCreatedWith7zip.zip
    Then the result contains no diff
    And the result contains no missing files
    And the result contains no additional files