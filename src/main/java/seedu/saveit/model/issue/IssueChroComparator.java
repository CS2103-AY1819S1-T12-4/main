package seedu.saveit.model.issue;

import java.util.Comparator;

import seedu.saveit.model.Issue;

/**
 * Comparator used to sort the Issues in chronological order
 */
public class IssueChroComparator implements Comparator<Issue> {
    public int compare(Issue a, Issue b) {
        return b.getLastModifiedTime().compareTo(a.getLastModifiedTime());
    }

    @Override
    public String toString() {
        return "Chronological";
    }
}
