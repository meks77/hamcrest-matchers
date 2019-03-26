package at.meks.hamcrest.matchers.zip;

class EntryCompareResult {

    private final ComparedEntryData actual;
    private final ComparedEntryData expected;

    EntryCompareResult(ComparedEntryData actual, ComparedEntryData expected) {
        this.actual = actual;
        this.expected = expected;
    }

    ComparedEntryData getActual() {
        return actual;
    }

    ComparedEntryData getExpected() {
        return expected;
    }

}
