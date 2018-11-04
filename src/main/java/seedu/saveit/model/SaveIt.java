package seedu.saveit.model;

import static java.util.Objects.requireNonNull;
import static seedu.saveit.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.commons.exceptions.IllegalValueException;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * Wraps all data at the saveit-book level
 * Duplicates are not allowed (by .isSameIssue comparison)
 */
public class SaveIt implements ReadOnlySaveIt {

    private static final String DUMMY_TAG = "dummyTag";
    private final UniqueIssueList issues;
    private Directory currentDirectory;


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
     * Resets the existing data of this {@code SaveIt} with {@code newData}.
     */
    public void resetData(ReadOnlySaveIt newData) {
        requireNonNull(newData);

        setIssues(newData.getIssueList());
        setCurrentDirectory(newData.getCurrentDirectory());
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
     * Adds tag(s) to the existing data of this {@code SaveIt} issue with {@code tagList} for {@code index} issue.
     */
    public void addTag(Index index, Set<Tag> tagList) {
        requireNonNull(tagList);
        Issue issueToEdit = issues.getIssue(index);
        Set<Tag> tagsToUpdate = new HashSet<>(issueToEdit.getTags());
        tagsToUpdate.addAll(tagList);

        Issue updateIssue = new Issue(issueToEdit.getStatement(), issueToEdit.getDescription(),
            issueToEdit.getSolutions(), tagsToUpdate, issueToEdit.getFrequency());
        updateIssue(issueToEdit, updateIssue);
    }

    /**
     * Adds tag(s) to the existing data of this {@code SaveIt} issue with {@code tagList} for {@code index} issue.
     */
    public boolean refactorTag(Tag oldTag, Tag newTag) {
        boolean isEdit = false;
        requireNonNull(oldTag);
        for (Issue issueToUpdate : issues) {
            Set<Tag> tagsToUpdate = new HashSet<>(issueToUpdate.getTags());
            if (tagsToUpdate.contains(oldTag)) {
                tagsToUpdate.remove(oldTag);
                if (!newTag.tagName.equals(DUMMY_TAG)) {
                    tagsToUpdate.add(newTag);
                }
                isEdit = true;
                Issue updateIssue = new Issue(issueToUpdate.getStatement(), issueToUpdate.getDescription(),
                    issueToUpdate.getSolutions(), tagsToUpdate, issueToUpdate.getFrequency());
                updateIssue(issueToUpdate, updateIssue);
            }

        }
        return isEdit;
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
