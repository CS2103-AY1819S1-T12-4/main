package seedu.saveit.model;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;

import java.util.Comparator;

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
