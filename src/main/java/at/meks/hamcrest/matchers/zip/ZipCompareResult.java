package at.meks.hamcrest.matchers.zip;

import java.util.LinkedList;
import java.util.List;

class ZipCompareResult {

    private List<EntryCompareResult> entryDiffs = new LinkedList<>();

    void addEntryResult(EntryCompareResult result) {
        entryDiffs.add(result);
    }

    List<EntryCompareResult> getEntryDiffs() {
        return entryDiffs;
    }

    boolean hasDiffs() {
        return !entryDiffs.isEmpty();
    }
}
