package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.commons.util.CollectionUtil;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * Edits the details of an existing issue in the saveIt.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "e";
    public static final String MESSAGE_DUPLICATE_ISSUE = "This issue already exists in the saveIt.";
    public static final String MESSAGE_EDIT_ISSUE_SUCCESS = "Edited Issue: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " command format: \n"
        + "Edit issue by the index number used in the displayed list: \n"
        + "******  " + COMMAND_WORD + " INDEX (must be a positive integer) "
        + "[" + PREFIX_STATEMENT + "ISSUE_STATEMENT] "
        + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
        + "[" + PREFIX_TAG + "TAG]...\n"
        + "Edit solution by the index number used in the displayed solution list: \n"
        + "******  " + COMMAND_WORD + " INDEX (must be a positive integer) "
        + "[" + PREFIX_SOLUTION_LINK + "NEW_SOLUTION_LINK] "
        + "[" + PREFIX_REMARK + "NEW_SOLUTION_REMARK] \n";


    public static final String DUMMY_SOLUTION_REMARK = "dummySolutionRemark";
    public static final String DUMMY_SOLUTION_LINK = "https://www.dummySolutionLink.com";

    private final Index index;
    private final EditIssueDescriptor editIssueDescriptor;

    /**
     * @param index of the issue in the filtered issue list to edit
     * @param editIssueDescriptor details to edit the issue with
     */
    public EditCommand(Index index, EditIssueDescriptor editIssueDescriptor) {
        requireNonNull(index);
        requireNonNull(editIssueDescriptor);
        this.index = index;
        this.editIssueDescriptor = editIssueDescriptor;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        Issue issueToEdit;
        requireNonNull(model);
        List<Issue> lastShownList = model.getFilteredAndSortedIssueList();
        Directory currentDirectory = model.getCurrentDirectory();

        if (currentDirectory.isRootLevel() && editIssueDescriptor.isAnyIssueFieldEdited()) {
            issueToEdit = getIssueToEdit(lastShownList, lastShownList.size(), index.getZeroBased());
        } else if ((currentDirectory.isIssueLevel() || currentDirectory.isSolutionLevel()) && editIssueDescriptor
            .isAnySolutionFieldEdited()) {
            int solutionListSize = lastShownList.get(currentDirectory.getIssue() - 1).getSolutions().size();
            issueToEdit = getIssueToEdit(lastShownList, solutionListSize, currentDirectory.getIssue() - 1);
        } else {
            throw new CommandException(Messages.MESSAGE_WRONG_DIRECTORY);
        }
        Issue editedIssue = createEditedIssue(issueToEdit, editIssueDescriptor);

        if (!issueToEdit.isSameIssue(editedIssue) && model.hasIssue(editedIssue)) {
            throw new CommandException(MESSAGE_DUPLICATE_ISSUE);
        }

        model.updateIssue(issueToEdit, editedIssue);
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        model.commitSaveIt();
        return new CommandResult(String.format(MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue));
    }

    private Issue getIssueToEdit(List<Issue> lastShownList, int listSize, int issueIndex)
        throws CommandException {
        Issue issueToEdit;
        if (index.getZeroBased() < listSize) {
            issueToEdit = lastShownList.get(issueIndex);
        } else {
            throw new CommandException(Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
        }
        return issueToEdit;
    }

    /**
     * Creates and returns a {@code Issue} with the details of {@code issueToEdit} edited with {@code
     * editIssueDescriptor}.
     */
    private static Issue createEditedIssue(Issue issueToEdit, EditIssueDescriptor editIssueDescriptor)
        throws CommandException {
        assert issueToEdit != null;

        List<Solution> updatedSolutions;
        if (editIssueDescriptor.getIndex() != -1) {
            updatedSolutions = new ArrayList<>(issueToEdit.getSolutions());
            assert (editIssueDescriptor.getSolution() != null);
            Solution updatedSolution = processNewSolution(editIssueDescriptor.getIndex(), issueToEdit,
                editIssueDescriptor.getSolution().get());
            updatedSolutions.set(editIssueDescriptor.getIndex(), updatedSolution);
        } else {
            updatedSolutions = editIssueDescriptor.getSolutions().orElse(issueToEdit.getSolutions());
        }
        IssueStatement updatedName = editIssueDescriptor.getStatement().orElse(issueToEdit.getStatement());
        Description updatedDescription = editIssueDescriptor.getDescription().orElse(issueToEdit.getDescription());
        Set<Tag> updatedTags = editIssueDescriptor.getTags().orElse(issueToEdit.getTags());

        return new Issue(updatedName, updatedDescription, updatedSolutions, updatedTags);
    }

    /**
     * Creates and returns a {@code index} with the details of {@code issueToEdit} edited with {@code
     * editIssueDescriptor}.
     */
    private static Solution processNewSolution(int index, Issue issueToEdit, Solution newSolution)
        throws CommandException {
        // if in the home directory, should not process this
        if (index >= issueToEdit.getSolutions().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
        } else {
            Solution oldSolution = issueToEdit.getSolutions().get(index);
            Solution updatedSolution;

            String updatedSolutionLink =
                newSolution.getLink().getValue().equals(DUMMY_SOLUTION_LINK) ? oldSolution.getLink().getValue()
                    : newSolution.getLink().getValue();
            String updatedSolutionRemark =
                newSolution.getRemark().getValue().equals(DUMMY_SOLUTION_REMARK) ? oldSolution.getRemark().getValue()
                    : newSolution.getRemark().getValue();

            updatedSolution = new Solution(updatedSolutionLink, updatedSolutionRemark);

            return updatedSolution;
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
            && editIssueDescriptor.equals(e.editIssueDescriptor);
    }

    /**
     * Stores the details to edit the issue with. Each non-empty field value will replace the corresponding field value
     * of the issue.
     */
    public static class EditIssueDescriptor {

        private IssueStatement statement;
        private List<Solution> solutions;
        private Description description;
        private Set<Tag> tags;
        private int index = -1;
        private Solution solution;

        public EditIssueDescriptor() {
        }

        public EditIssueDescriptor(Index index, Solution solution) {
            this.solution = solution;
            this.index = index.getZeroBased();
        }

        /**
         * Copy constructor. A defensive copy of {@code tags} is used internally.
         */
        public EditIssueDescriptor(EditIssueDescriptor toCopy) {
            this.index = toCopy.getIndex();
            setStatement(toCopy.statement);
            setSolutions(toCopy.solutions);
            setDescription(toCopy.description);
            setTags(toCopy.tags);
        }

        public int getIndex() {
            return index;
        }

        /**
         * Returns true if at least one field of issue level is edited.
         */
        public boolean isAnyIssueFieldEdited() {
            return CollectionUtil.isAnyNonNull(statement, description, tags);
        }

        /**
         * Returns true if solution field is edited.
         */
        public boolean isAnySolutionFieldEdited() {
            return CollectionUtil.isAnyNonNull(solution);
        }


        public void setStatement(IssueStatement statement) {
            this.statement = statement;
        }

        public Optional<IssueStatement> getStatement() {
            return Optional.ofNullable(statement);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Solution> getSolution() {
            return Optional.ofNullable(solution);
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setSolutions(List<Solution> solutions) {
            this.solutions = (solutions != null) ? new ArrayList<>(solutions) : null;
        }

        public Optional<List<Solution>> getSolutions() {
            return (solutions != null) ? Optional.of(Collections.unmodifiableList(solutions)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}. A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException} if modification is
         * attempted. Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditIssueDescriptor)) {
                return false;
            }

            // state check
            EditIssueDescriptor e = (EditIssueDescriptor) other;

            return getStatement().equals(e.getStatement())
                && getDescription().equals(e.getDescription())
                && getSolutions().equals(e.getSolutions())
                && getTags().equals(e.getTags());
        }
    }
}
