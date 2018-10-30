package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Tag;

/**
 * To rename or remove a spcific tag for all entries with that tag.
 */
public class RefactorTagCommand extends Command {

    public static final String COMMAND_WORD = "refactortag";
    public static final String COMMAND_ALIAS = "rt";

    public static final String MESSAGE_REFACTOR_TAG_SUCCESS = "Refactoring tag success";
    public static final String MESSAGE_REFACTOR_TAG_FAILURE = "Refactoring tag is unsuccessful due to no such tag";

    public static final String MESSAGE_USAGE =
        COMMAND_WORD + ": To rename or remove a specific tag for all entries with that tag.\n"
            + "Parameters: t/OLD_TAG [nt/NEW_TAG] \n"
            + "Example: " + COMMAND_WORD + " t/python n/java";

    public static final String DUMMY_TAG = "dummyTag";

    private final Tag oldTag;
    private final Tag newTag;

    private Issue editedIssue;
    private boolean isEidt;

    /**
     * @param oldTag the tag will be replaced
     * @param newTag the new tag that will replace original one
     */
    public RefactorTagCommand(Tag oldTag, Tag newTag) {
        requireNonNull(oldTag);
        this.oldTag = oldTag;
        this.newTag = newTag;
        editedIssue = null;
        this.isEidt = false;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Issue> lastShownList = model.getFilteredIssueList();

        for (Issue issue : lastShownList) {
            Set<Tag> updatedTags = new HashSet<Tag>(issue.getTags());
            if (updatedTags.contains(oldTag)) {
                updatedTags.remove(oldTag);
                if (!newTag.tagName.equals(DUMMY_TAG)) {
                    updatedTags.add(newTag);
                }
                isEidt = true;
            }

            editedIssue = new Issue(issue.getStatement(), issue.getDescription(),
                issue.getSolutions(), updatedTags);
            model.updateIssue(issue, editedIssue);
            model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
            model.commitSaveIt();
        }
        if (isEidt) {
            return new CommandResult(MESSAGE_REFACTOR_TAG_SUCCESS);
        }
        return new CommandResult(MESSAGE_REFACTOR_TAG_FAILURE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RefactorTagCommand) // instanceof handles nulls
            || (editedIssue.equals(((RefactorTagCommand) other).editedIssue)); //state check
    }
}

