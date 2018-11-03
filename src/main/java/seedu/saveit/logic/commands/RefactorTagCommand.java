package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Tag;

/**
 * To rename or remove a specified tag for all entries with that tag.
 */
public class RefactorTagCommand extends Command {

    public static final String COMMAND_WORD = "refactortag";
    public static final String COMMAND_ALIAS = "rt";

    public static final String MESSAGE_REFACTOR_TAG_SUCCESS = "Refactoring tag success";
    public static final String MESSAGE_REFACTOR_TAG_FAILURE = "Refactoring tag is unsuccessful due to no such tag";

    public static final String MESSAGE_USAGE =
        COMMAND_WORD + ": To rename or remove a specific tag for all entries with that tag.\n"
            + "Parameters: t/OLD_TAG [n/NEW_TAG] \n"
            + "Example: " + COMMAND_WORD + " t/python n/java";

    public static final String DUMMY_TAG = "dummyTag";

    private final Tag oldTag;
    private final Tag newTag;

    private boolean isEdit;

    /**
     * @param oldTag the tag will be replaced
     * @param newTag the new tag that will replace original one
     */
    public RefactorTagCommand(Tag oldTag, Tag newTag) {
        requireNonNull(oldTag);
        this.oldTag = oldTag;
        this.newTag = newTag;
        this.isEdit = false;
    }

    /**
     * @param oldTag the tag will be replaced
     */
    public RefactorTagCommand(Tag oldTag) {
        requireNonNull(oldTag);
        this.oldTag = oldTag;
        this.isEdit = false;
        this.newTag = null;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        isEdit = newTag != null ? model.refactorTag(oldTag, newTag) : model.refactorTag(oldTag);
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        model.commitSaveIt();
        if (isEdit) {
            return new CommandResult(MESSAGE_REFACTOR_TAG_SUCCESS);
        }
        return new CommandResult(MESSAGE_REFACTOR_TAG_FAILURE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RefactorTagCommand) // instanceof handles nulls
            || (oldTag.equals(((RefactorTagCommand) other).oldTag)) //state check
            || (newTag.equals(((RefactorTagCommand) other).newTag));
    }
}

