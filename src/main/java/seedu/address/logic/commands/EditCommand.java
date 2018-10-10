package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Issue;
import seedu.address.model.Model;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Remark;
import seedu.address.model.issue.Tag;

/**
 * Edits the details of an existing issue in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the issue identified "
            + "by the index number used in the displayed issue list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_STATEMENT + "ISSUE_STATEMENT] "
            + "[" + PREFIX_DESCRIPTION + "PHONE] "
            + "[" + PREFIX_REMARK + "REMARK] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DESCRIPTION + "91234567 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Issue: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This issue already exists in the address book.";

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
        this.editIssueDescriptor = new EditIssueDescriptor(editIssueDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Issue> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Issue issueToEdit = lastShownList.get(index.getZeroBased());
        Issue editedIssue = createEditedPerson(issueToEdit, editIssueDescriptor);

        if (!issueToEdit.isSameIssue(editedIssue) && model.hasPerson(editedIssue)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.updatePerson(issueToEdit, editedIssue);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.commitSaveIt();
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedIssue));
    }

    /**
     * Creates and returns a {@code Issue} with the details of {@code issueToEdit}
     * edited with {@code editIssueDescriptor}.
     */
    private static Issue createEditedPerson(Issue issueToEdit, EditIssueDescriptor editIssueDescriptor) {
        assert issueToEdit != null;

        IssueStatement updatedName = editIssueDescriptor.getName().orElse(issueToEdit.getStatement());
        Description updatedDescription = editIssueDescriptor.getDescription().orElse(issueToEdit.getDescription());
        Remark updatedAddress = editIssueDescriptor.getAddress().orElse(issueToEdit.getAddress());
        Set<Tag> updatedTags = editIssueDescriptor.getTags().orElse(issueToEdit.getTags());

        return new Issue(updatedName, updatedDescription, updatedAddress, updatedTags);
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
     * Stores the details to edit the issue with. Each non-empty field value will replace the
     * corresponding field value of the issue.
     */
    public static class EditIssueDescriptor {
        private IssueStatement name;
        private Description description;
        private Remark address;
        private Set<Tag> tags;

        public EditIssueDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditIssueDescriptor(EditIssueDescriptor toCopy) {
            setName(toCopy.name);
            setDescription(toCopy.description);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, description, address, tags);
        }

        public void setName(IssueStatement name) {
            this.name = name;
        }

        public Optional<IssueStatement> getName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setAddress(Remark address) {
            this.address = address;
        }

        public Optional<Remark> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
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

            return getName().equals(e.getName())
                    && getDescription().equals(e.getDescription())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}
