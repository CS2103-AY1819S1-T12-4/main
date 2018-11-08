package seedu.saveit.model;

import static java.util.Objects.requireNonNull;
import static seedu.saveit.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.commons.exceptions.IllegalValueException;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.model.issue.exceptions.IssueNotFoundException;

/**
 * Wraps all data at the saveit-book level
 * Duplicates are not allowed (by .isSameIssue comparison)
 */
public class SaveIt implements ReadOnlySaveIt {

    private final UniqueIssueList issues;
    private Directory currentDirectory;
    private Comparator<Issue> currentSortType;


    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        issues = new UniqueIssueList();
        currentDirectory = new Directory(0, 0);
        currentSortType = null;
    }

    public SaveIt() {}

    /**
     * Creates an SaveIt using the Issues in the {@code toBeCopied}
     */
    public SaveIt(ReadOnlySaveIt toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the issue list with {@code issues}.
     * {@code issues} must not contain duplicate issues.
     */
    public void setIssues(List<Issue> issues) {
        this.issues.setIssues(issues);
    }

    /**
     * Update the current directory.
     * {@code CurrentDirectory} must not exceeds the length of {@code issues}.
     * @param directory
     */
    public void setCurrentDirectory(Directory directory) {
        try {
            if (directory.isIssueLevel() && directory.getIssue() > issues.size()) {
                throw new IllegalValueException("Refer to non-existent directory.");
            } else if (directory.isSolutionLevel() && directory.getSolution()
                    > issues.getSolutionNumber(Index.fromOneBased(directory.getSolution()))) {
                throw new IllegalValueException("Refer to non-existent directory.");
            }
            currentDirectory = directory;
        } catch (IllegalValueException e) {
            e.getMessage();
        }
    }

    /**
     * Update the current sortType.
     */
    public void setSortType(Comparator<Issue> sortType) {
        currentSortType = sortType;
    }

    /**
     * Resets the existing data of this {@code SaveIt} with {@code newData}.
     */
    public void resetData(ReadOnlySaveIt newData) {
        requireNonNull(newData);
        setIssues(newData.getIssueList());
        setCurrentDirectory(newData.getCurrentDirectory());
        setSortType(newData.getCurrentSortType());
    }

    //// issue-level operations

    /**
     * Returns true if an issue with the same identity as {@code issue} exists in the saveIt.
     */
    public boolean hasIssue(Issue issue) {
        requireNonNull(issue);
        return issues.contains(issue);
    }

    /**
     * Returns true if the targeted issue has the same solution exists in the saveIt.
     */
    public boolean hasSolution(Index index, Solution solution) {
        requireAllNonNull(index, solution);
        return issues.getIssue(index).getSolutions().contains(solution);
    }

    /**
     * Adds an issue to the saveIt.
     * The issue must not already exist in the saveIt.
     */
    public void addIssue(Issue p) {
        issues.add(p);
    }

    /**
     * Adds an issue to the saveIt.
     * The issue must not already exist in the saveIt.
     */
    public void addSolution(Index index, Solution solution) {
        Issue issueToEdit = issues.getIssue(index);
        List<Solution> solutionsToUpdate = new ArrayList<>(issueToEdit.getSolutions());
        solutionsToUpdate.add(solution);

        Issue updateIssue = new Issue(issueToEdit.getStatement(), issueToEdit.getDescription(),
                solutionsToUpdate, issueToEdit.getTags(), issueToEdit.getFrequency());
        updateIssue(issueToEdit, updateIssue);
    }


    /**
     * Replaces the given issue {@code target} in the list with {@code editedIssue}.
     * {@code target} must exist in the saveIt.
     * The issue identity of {@code editedIssue} must not be the same as another existing issue in the saveIt.
     */
    public void updateIssue(Issue target, Issue editedIssue) {
        requireNonNull(editedIssue);

        issues.setIssue(target, editedIssue);
    }

    /**
     * Adds tag(s) to the existing data of this {@code SaveIt} with {@code tagList} for a range of {@code index} issue.
     */
    public void addTag(Set<Issue> issues, Set<Tag> tagList) {
        requireNonNull(tagList);
        Iterator<Issue> issueIterator = issues.iterator();
        boolean added = false;

        while (issueIterator.hasNext()) {
            Issue issueToEdit = issueIterator.next();
            Set<Tag> currentTags = new LinkedHashSet<>(issueToEdit.getTags());
            Set<Tag> updateTags = new LinkedHashSet<>();
            updateTags.addAll(currentTags);
            updateTags.addAll(tagList);

            if (currentTags.size() != updateTags.size()) {
                added = true;
                updateTags(issueToEdit, updateTags);
            }
        }

        if (!added) {
            throw new IssueNotFoundException();
        }
    }

    /**
     * Adds tag(s) to the existing data of this {@code SaveIt} issue with {@code tagList} for {@code index} issue.
     */
    public boolean refactorTag(Tag oldTag, Tag newTag) {
        boolean isEdit = false;
        requireNonNull(oldTag);
        for (Issue issueToUpdate : issues) {
            Set<Tag> tagsToUpdate = new LinkedHashSet<>(issueToUpdate.getTags());
            if (tagsToUpdate.remove(oldTag)) {
                tagsToUpdate.add(newTag);
                isEdit = true;
                updateTags(issueToUpdate, tagsToUpdate);
            }

        }
        return isEdit;
    }

    /**
     * remove the {@code oldTag} of {@code SaveIt} for all issue entries.
     */
    public boolean refactorTag(Tag tag) {
        boolean isEdit = false;
        requireNonNull(tag);
        for (Issue issueToUpdate : issues) {
            Set<Tag> tagsToUpdate = new LinkedHashSet<>(issueToUpdate.getTags());
            if (tagsToUpdate.remove(tag)) {
                isEdit = true;
                updateTags(issueToUpdate, tagsToUpdate);
            }
        }
        return isEdit;
    }

    private void updateTags(Issue issueToUpdate, Set<Tag> tagsToUpdate) {
        Issue updateIssue = new Issue(issueToUpdate.getStatement(), issueToUpdate.getDescription(),
            issueToUpdate.getSolutions(), tagsToUpdate, issueToUpdate.getFrequency());
        updateIssue(issueToUpdate, updateIssue);
    }

    /**
     * Removes {@code key} from this {@code SaveIt}.
     * {@code key} must exist in the saveIt.
     */
    public void removeIssue(Issue key) {
        issues.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return issues.asUnmodifiableObservableList().size() + " issues";
        // TODO: refine later
    }

    @Override
    public ObservableList<Issue> getIssueList() {
        return issues.asUnmodifiableObservableList();
    }

    @Override
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public Comparator getCurrentSortType() {
        return currentSortType;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SaveIt // instanceof handles nulls
                && issues.equals(((SaveIt) other).issues));
    }

    @Override
    public int hashCode() {
        return issues.hashCode();
    }
}
