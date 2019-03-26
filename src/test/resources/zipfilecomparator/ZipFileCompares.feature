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
      | name                              | actual last modified | expected last modified  | actual size | expected size | actual content hash | expected content hash |
      | dir1/dir1file2.txt                | 2019-03-21T22:40:50  | 2019-03-21T22:35:58     | 24          | 24            | 1163207965          | 1163207965            |
      | dir1/subdir1/dir1subdir1file1.txt | 2019-03-21T22:42:18  | 2019-03-21T22:36:50     | 39          | 36            | -806727547          | 1726840705            |
    And the result contains the following missing files:
      | dir1/subdir2/                     |