package seedu.saveit.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.saveit.commons.core.ComponentManager;
import seedu.saveit.commons.core.LogsCenter;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.events.model.SaveItChangedEvent;
import seedu.saveit.commons.util.CollectionUtil;
import seedu.saveit.model.issue.IssueSort;
import seedu.saveit.model.issue.Solution;

/**
 * Represents the in-memory model of the saveIt data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedSaveIt versionedSaveIt;
    private FilteredList<Issue> filteredIssues;
    private SortedList<Issue> sortedList;

    /**
     * Initializes a ModelManager with the given saveIt and userPrefs.
     */
    public ModelManager(ReadOnlySaveIt saveIt, UserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(saveIt, userPrefs);

        logger.fine("Initializing with SaveIt: " + saveIt + " and user prefs " + userPrefs);

        versionedSaveIt = new VersionedSaveIt(saveIt);
        filteredIssues = new FilteredList<>(versionedSaveIt.getIssueList());
        sortedList = new SortedList<>(versionedSaveIt.getIssueList());
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
    public void resetDirectory(Directory currentDirectory) {
        versionedSaveIt.setCurrentDirectory(currentDirectory);
        indicateSaveItChanged();
    }

    @Override
    public Directory getCurrentDirectory() {
        return versionedSaveIt.getCurrentDirectory();
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
    public void deleteIssue(Issue target) {
        versionedSaveIt.removeIssue(target);
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
        CollectionUtil.requireAllNonNull(target, editedIssue);

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


    @Override
    public void sortIssues(IssueSort sortType) {
        updateSortedIssueList(sortType.getComparator());
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
            return FXCollections.unmodifiableObservableList
                    (filteredIssues.get(directory.getIssue() - 1).getObservableSolutions());
        }
    }

    @Override
    public void updateFilteredIssueList(Predicate<Issue> predicate) {
        requireNonNull(predicate);
        filteredIssues.setPredicate(predicate);
    }

    //=========== Filtered Issue List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Issue} backed by the internal list of
     * {@code versionedSaveIt}
     */
    @Override
    public ObservableList<Issue> getSortedIssueList() {
        return FXCollections.unmodifiableObservableList(sortedList);
    }

    @Override
    public void updateSortedIssueList(Comparator<Issue> sortType) {
        requireNonNull(sortType);
        sortedList.setComparator(sortType);
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
                && filteredIssues.equals(other.filteredIssues);
    }

}
