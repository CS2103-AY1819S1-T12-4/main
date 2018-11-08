package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.core.EventsCenter;
import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.events.model.SortTypeChangedEvent;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.IssueSort;

/**
 * Lists all persons in the saveIt to the user.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String COMMAND_ALIAS = "sr";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sort all issues with index numbers based on "
            + "the provided sortType: freq (search frequency), chro (chronological), tag (tag names).\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD + " " + IssueSort.TAG_SORT;

    public static final String MESSAGE_SUCCESS = "Sorted issues by %s.";

    private final IssueSort sortType;

    public SortCommand(IssueSort sortType) {
        this.sortType = sortType;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        Directory currentDirectory = model.getCurrentDirectory();
        if (!currentDirectory.isRootLevel()) {
            throw new CommandException(Messages.MESSAGE_WRONG_DIRECTORY);
        }

        requireNonNull(model);
        model.sortIssues(sortType);
        EventsCenter.getInstance().post(
                new SortTypeChangedEvent(model.getCurrentSortType()));
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, sortType.getSortType()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortCommand // instanceof handles nulls
                && sortType.equals(((SortCommand) other).sortType)); // state check
    }
}
