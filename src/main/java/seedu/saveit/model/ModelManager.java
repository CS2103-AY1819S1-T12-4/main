package seedu.saveit.model;

import static java.util.Objects.requireNonNull;
import static seedu.saveit.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.saveit.commons.core.ComponentManager;
import seedu.saveit.commons.core.LogsCenter;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.commons.events.model.SaveItChangedEvent;
import seedu.saveit.model.issue.IssueSort;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * Represents the in-memory model of the saveIt data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedSaveIt versionedSaveIt;
    private FilteredList<Issue> filteredIssues;
    private SortedList<Issue> filteredAndSortedIssues;

    /**
     * Initializes a ModelManager with the given saveIt and userPrefs.
     */
    public ModelManager(ReadOnlySaveIt saveIt, UserPrefs userPrefs) {
        super();
        requireAllNonNull(saveIt, userPrefs);

        logger.fine("Initializing with SaveIt: " + saveIt + " and user prefs " + userPrefs);

        versionedSaveIt = new VersionedSaveIt(saveIt);
        filteredIssues = new FilteredList<>(versionedSaveIt.getIssueList());
        filteredAndSortedIssues = new SortedList<>(getFilteredIssueList());
    }

    public ModelManager() {
        this(new SaveIt(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlySaveIt newData) {
        versionedSaveIt.resetData(newData);
        indicateSaveItChanged();
    }

    @Override
    public void resetDirectory(Directory newDirectory) {
        versionedSaveIt.setCurrentDirectory(newDirectory);
        indicateSaveItChanged();
    }

    @Override
    public Directory getCurrentDirectory() {
        return versionedSaveIt.getCurrentDirectory();
    }

    @Override
    public Comparator<Issue> getCurrentSortType() {
        return versionedSaveIt.getCurrentSortType();
    }

    @Override
    public ReadOnlySaveIt getSaveIt() {
        return versionedSaveIt;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateSaveItChanged() {
        raise(new SaveItChangedEvent(versionedSaveIt));
    }

    @Override
    public boolean hasIssue(Issue issue) {
        requireNonNull(issue);
        return versionedSaveIt.hasIssue(issue);
    }

    @Override
    public boolean hasSolution(Index index, Solution solution) {
        requireAllNonNull(index, solution);
        return versionedSaveIt.hasSolution(index, solution);
    }

    @Override
    public void deleteIssue(Issue target) {
        versionedSaveIt.removeIssue(target);
        indicateSaveItChanged();
    }

    @Override
    public void addSolution(Index index, Solution solution) {
        versionedSaveIt.addSolution(index, solution);
        indicateSaveItChanged();
    }

    @Override
    public void addIssue(Issue issue) {
        versionedSaveIt.addIssue(issue);
        updateFilteredIssueList(PREDICATE_SHOW_ALL_ISSUES);
        indicateSaveItChanged();
    }

    @Override
    public void updateIssue(Issue target, Issue editedIssue) {
        requireAllNonNull(target, editedIssue);

        versionedSaveIt.updateIssue(target, editedIssue);
        indicateSaveItChanged();
    }

    @Override
    public void filterIssues(Predicate<Issue> predicate) {
        updateFilteredIssueList(predicate);
        // Update the search frequencies after filtering
        for (Issue issue : filteredIssues) {
            issue.updateFrequency();
        }
    }

    //=========== Add Tag ===================================================================================
    @Override
    public void addTag(Set<Issue> issues, Set<Tag> tagList) {
        requireAllNonNull(issues, tagList);
        versionedSaveIt.addTag(issues, tagList);

        indicateSaveItChanged();
    }

    //=========== Refactor Tag ==============================================================================
    @Override
    public boolean refactorTag(Tag oldTag, Tag newTag) {
        requireAllNonNull(oldTag, newTag);
        boolean isEdit = versionedSaveIt.refactorTag(oldTag, newTag);

        indicateSaveItChanged();
        return isEdit;
    }

    @Override
    public boolean refactorTag(Tag tag) {
        requireAllNonNull(tag);
        boolean isEdit = versionedSaveIt.refactorTag(tag);

        indicateSaveItChanged();
        return isEdit;
    }


    @Override
    public void sortIssues(IssueSort sortType) {
        updateFilteredAndSortedIssueList(sortType.getComparator());
        versionedSaveIt.setSortType(sortType.getComparator());
    }

    //=========== Filtered Issue List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Issue} backed by the internal list of
     * {@code versionedSaveIt}
     */
    @Override
    public ObservableList<Issue> getFilteredIssueList() {
        return FXCollections.unmodifiableObservableList(filteredIssues);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Solution} backed by the internal list of
     * {@code Issue}
     */
    @Override
    public ObservableList<Solution> getFilteredSolutionList() {
        Directory directory = getCurrentDirectory();
        if (directory.isRootLevel()) {
            return null;
        } else {
            ObservableList<Solution> solutions = filteredIssues.get(directory.getIssue() - 1).getObservableSolutions();
            solutions.sort(new SolutionComparator());
            return FXCollections.unmodifiableObservableList(solutions);
        }
    }

    @Override
    public void updateFilteredIssueList(Predicate<Issue> predicate) {
        requireNonNull(predicate);
        filteredIssues.setPredicate(predicate);
    }

    //=========== Sorted Issue List Accessors =============================================================
    @Override
    public void updateFilteredAndSortedIssueList(Comparator<Issue> comparator) {
        filteredAndSortedIssues.setComparator(comparator);
    }

    //=========== Sorted Issue List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Issue} backed by the internal list of
     * {@code versionedSaveIt}
     */
    @Override
    public ObservableList<Issue> getFilteredAndSortedIssueList() {
        return FXCollections.unmodifiableObservableList(filteredAndSortedIssues);
    }

    //=========== Tag Set Accessors ======================================================================
    @Override
    public TreeSet<String> getCurrentTagSet() {
        TreeSet<String> tagSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        versionedSaveIt.getIssueList().forEach(issue -> issue.getTags()
                .forEach(tag -> tagSet.add(tag.tagName)));
        return tagSet;
    }

    //=========== Tag Set Accessors ======================================================================
    @Override
    public TreeSet<String> getCurrentIssueStatementSet() {
        TreeSet<String> statementSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        versionedSaveIt.getIssueList().forEach(issue -> statementSet.add(issue.getStatement().getValue()));
        return statementSet;
    }


    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoSaveIt() {
        return versionedSaveIt.canUndo();
    }

    @Override
    public boolean canRedoSaveIt() {
        return versionedSaveIt.canRedo();
    }

    @Override
    public void undoSaveIt() {
        versionedSaveIt.undo();
        indicateSaveItChanged();
    }

    @Override
    public void redoSaveIt() {
        versionedSaveIt.redo();
        indicateSaveItChanged();
    }

    @Override
    public void commitSaveIt() {
        versionedSaveIt.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedSaveIt.equals(other.versionedSaveIt)
                && filteredIssues.equals(other.filteredIssues)
                && filteredAndSortedIssues.equals(other.filteredAndSortedIssues);
    }

    /**
     * A comparator for putting primary solution on top of the solution list.
     */
    private class SolutionComparator implements Comparator<Solution> {
        @Override
        public int compare(Solution solutionOne, Solution solutionTwo) {
            if (solutionOne.isPrimarySolution() && !solutionTwo.isPrimarySolution()) {
                return -1;
            }

            if (solutionTwo.isPrimarySolution() && !solutionOne.isPrimarySolution()) {
                return 1;
            }

            return 0;
        }
    }
}
