package seedu.saveit.model;

import java.util.Comparator;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;

/**
 * Unmodifiable view of an saveIt
 */
public interface ReadOnlySaveIt {

    /**
     * Returns an unmodifiable view of the issues list.
     * This list will not contain any duplicate issues.
     */
    ObservableList<Issue> getIssueList();

    Directory getCurrentDirectory();

    Comparator<Issue> getCurrentSortType();
}
