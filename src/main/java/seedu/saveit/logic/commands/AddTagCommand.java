package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Set;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Tag;

/**
 * To rename or remove a spcific tag for all entries with that tag.
 */
public class AddTagCommand extends Command {

    public static final String COMMAND_WORD = "addtag";
    public static final String COMMAND_ALIAS = "at";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Add tags success";

    public static final String MESSAGE_USAGE =
        COMMAND_WORD + ": To add tags for an issue.\n"
            + "Parameters: INDEX t/NEW_TAG [t/NEW_TAG2]... \n"
            + "Example: " + COMMAND_WORD + " 2 t/python t/java";

    private final Set<Tag> tagList;
    private final Index index;


    /**
     * @param index the issue index that tag will be add
     * @param tagList the new tag(s) to be added to saveIt
     */
    public AddTagCommand(Index index, Set<Tag> tagList) {
        requireNonNull(tagList);
        this.index = index;
        this.tagList = tagList;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.addTag(index, tagList);
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        model.commitSaveIt();

        return new CommandResult(MESSAGE_ADD_TAG_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RefactorTagCommand); // instanceof handles nulls
    }
}

