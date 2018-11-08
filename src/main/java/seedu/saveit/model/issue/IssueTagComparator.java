package seedu.saveit.model.issue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import seedu.saveit.model.Issue;

/**
 * Comparator used to sort the Issues in order
 */
public class IssueTagComparator implements Comparator<Issue> {
    /**
     * Compare Issue a and b with their first tag in the tag set. Tag is compared based on String tagName.
     */
    public int compare(Issue a, Issue b) {
        Set<Tag> tagSetB = b.getTags();
        Set<Tag> tagSetA = a.getTags();

        if (tagSetA.size() == 0 && tagSetB.size() != 0) {
            return 1;
        }

        if (tagSetB.size() == 0 && tagSetA.size() != 0) {
            return -1;
        }

        Iterator<Tag> tagIteratorA = tagSetA.iterator();
        Iterator<Tag> tagIteratorB = tagSetB.iterator();
        while (tagIteratorA.hasNext() && tagIteratorB.hasNext()) {
            Tag tagA = tagIteratorA.next();
            Tag tagB = tagIteratorB.next();
            if (tagA.compare(tagB) != 0) {
                return tagA.compare(tagB);
            }
        }
        return tagSetA.size() - tagSetB.size();
    }

    @Override
    public String toString() {
        return "Tag";
    }
}
