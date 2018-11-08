package seedu.saveit.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.model.issue.IssueSort;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Issue> PREDICATE_SHOW_ALL_ISSUES = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlySaveIt newData);

    /** Reset the current directory. */
    void resetDirectory(Directory currentDirectory);

    /** Return the current directory. */
    Directory getCurrentDirectory();

    /** Return the current sortType. */
    Comparator<Issue> getCurrentSortType();

    /** Returns the SaveIt */
    ReadOnlySaveIt getSaveIt();

    /**
     * Returns true if an issue with the same identity as {@code issue} exists in the saveIt.
     */
    boolean hasIssue(Issue issue);

    /**
     * Returns true if the indexed issue has same solution as {@code solution} exists in the saveIt.
     */
    boolean hasSolution(Index index, Solution solution);

    /**
     * Deletes the given issue.
     * The issue must exist in the saveIt.
     */
    void deleteIssue(Issue target);

    /**
     * Adds the given solution to the indexed issue.
     * {@code solution} must not already exist in the given issue.
     */
    void addSolution(Index index, Solution solution);

    /**
     * Adds the given issue.
     * {@code issue} must not already exist in the saveIt.
     */
    void addIssue(Issue issue);

    /**
     * Replaces the given issue {@code target} with {@code editedIssue}.
     * {@code target} must exist in the saveIt.
     * The issue identity of {@code editedIssue} must not be the same as another existing issue in the saveIt.
     */
    void updateIssue(Issue target, Issue editedIssue);

    /** Returns an unmodifiable view of the filtered issue list */
    ObservableList<Issue> getFilteredIssueList();

    /**Returns an unmodified view of the filtered solution list of the selected issue */
    ObservableList<Solution> getFilteredSolutionList();

    /**
     * Filters the issues given the predicate and sorts them based on the search frequency
     */
    void filterIssues(Predicate<Issue> predicate);

    /**
     * Sorts the issues given the order.
     * @param sortType
     */
    void sortIssues(IssueSort sortType);

    /** Returns an unmodifiable view of the filtered or sorted issue list */
    ObservableList<Issue> getFilteredAndSortedIssueList();

    /**
     * Updates the filter of the filtered issue list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredIssueList(Predicate<Issue> predicate);

    /**
     * Updates the sorting of the sorted issue list to sort by the give {@code sortType}.
     */
    void updateFilteredAndSortedIssueList(Comparator<Issue> sortType);

    /**
     * Adds tag(s) to the existing data of this {@code SaveIt} with {@code tagList} for a range of {@code issues}.
     */
    void addTag(Set<Issue> issues, Set<Tag> tagList);

    /**
     * Returns a Set of strings representing all the Tag(s) contained in the issues
     */
    TreeSet<String> getCurrentTagSet();

    /**
     * Returns a Set of strings representing all the IssueStatement(s) contained in the issues
     */
    TreeSet<String> getCurrentIssueStatementSet();

    /**
     * Returns true if the model has previous saveIt states to restore.
     */
    boolean canUndoSaveIt();

    /**
     * Returns true if the model has undone saveIt states to restore.
     */
    boolean canRedoSaveIt();

    /**
     * Restores the model's saveIt to its previous state.
     */
    void undoSaveIt();

    /**
     * Restores the model's saveIt to its previously undone state.
     */
    void redoSaveIt();

    /**
     * Saves the current saveIt state for undo/redo.
     */
    void commitSaveIt();

    /**
     * To rename or remove a specified {@code oldTag} to {@code newTag} for all entries.
     * @return true if any tag has been replaced, otherwise false.
     */
    boolean refactorTag(Tag oldTag, Tag newTag);

    /**
     * To remove a specified {@code tag} for all entries.
     * @return true if any tag has been removed, otherwise false.
     */
    boolean refactorTag(Tag tag);
}
