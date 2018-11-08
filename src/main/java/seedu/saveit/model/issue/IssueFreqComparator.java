package seedu.saveit.model.issue;

import java.util.Comparator;

import seedu.saveit.model.Issue;

/**
 * Comparator used to sort the Issues in search frequency order
 */
public class IssueFreqComparator implements Comparator<Issue> {
    public int compare(Issue a, Issue b) {
        return b.getFrequency().compare(a.getFrequency());
    }

    @Override
    public String toString() {
        return "Search Frequency";
    }
}
