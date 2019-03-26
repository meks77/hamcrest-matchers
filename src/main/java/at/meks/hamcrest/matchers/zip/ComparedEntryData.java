package at.meks.hamcrest.matchers.zip;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

import static java.time.LocalDateTime.ofInstant;
import static java.util.Optional.ofNullable;

class ComparedEntryData {

    private String name;
    private int hashOfFileContent;
    private long size;
    private LocalDateTime lastModifiedDate;

    String getName() {
        return name;
    }

    int getHashOfFileContent() {
        return hashOfFileContent;
    }

    void setHashOfFileContent(int hashOfFileContent) {
        this.hashOfFileContent = hashOfFileContent;
    }

    long getSize() {
        return size;
    }

    LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ComparedEntryData that = (ComparedEntryData) o;
        return Objects.equals(size, that.size) &&
                Objects.equals(name, that.name) &&
                Objects.equals(hashOfFileContent, that.hashOfFileContent) &&
                Objects.equals(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), name, hashOfFileContent, size, lastModifiedDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ComparedEntryData.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("hashOfFileContent=" + hashOfFileContent)
                .add("size=" + size)
                .add("lastModifiedDate=" + ofNullable(lastModifiedDate)
                        .map(date -> date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .orElse(null))
                .toString();
    }

    static ComparedEntryDataBuilder aComparedEntryData() {
        return new ComparedEntryDataBuilder();
    }

    static final class ComparedEntryDataBuilder {
        private String name;
        private long size;
        private Date lastModifiedDate;

        private ComparedEntryDataBuilder() {
        }

        ComparedEntryDataBuilder withName(String name) {
            this.name = name;
            return this;
        }

        ComparedEntryDataBuilder withSize(long size) {
            this.size = size;
            return this;
        }

        ComparedEntryDataBuilder withLastModifiedDate(Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }

        ComparedEntryData build() {
            ComparedEntryData comparedEntryData = new ComparedEntryData();
            comparedEntryData.name = name;
            comparedEntryData.size = size;
            comparedEntryData.lastModifiedDate = ofInstant(lastModifiedDate.toInstant(), ZoneId.systemDefault());
            return comparedEntryData;
        }
    }
}
