package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Issue;
import seedu.address.model.Model;

/**
 * Retrieves the solution link of an existing solution in a issue in the saveIt.
 */
public class RetrieveCommand extends Command {

    public static final String COMMAND_WORD = "retrieve";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Retrieves the solution which is identified by the index number in the selected issue and "
            + "copy it to the system clipboard.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private static final String MESSAGE_RETRIEVE_LINK_SUCCESS = "Solution link no. %1$s is pasted to the "
            + "clipboard.";
    private static final String MESSAGE_FAILED_SELECTION = "Issue has to be selected first before "
            + "retrieving solution link.";
    private static final String MESSAGE_FAILED_SOLUTION = "The solution index provided is invalid";

    private final Index targetedIndex;

    /**
     * @param targetedIndex of the solution in the selected issue.
     */
    public RetrieveCommand(Index targetedIndex) {
        this.targetedIndex = targetedIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history)
            throws CommandException {
        requireNonNull(model);

        int issueIndex = model.getCurrentDirectory();
        List<Issue> lastShownList = model.getFilteredIssueList();
        Issue selectedIssue;

        if (issueIndex > 0) {
            selectedIssue = lastShownList.get(issueIndex - 1);
            try {
                String selectedLink = selectedIssue.getSolutions().get(targetedIndex.getZeroBased())
                        .getLink().value;
                copyToClipBoard(selectedLink);
                return new CommandResult(
                        String.format(MESSAGE_RETRIEVE_LINK_SUCCESS, targetedIndex.getOneBased()));
            } catch (IndexOutOfBoundsException e) {
                throw new CommandException(MESSAGE_FAILED_SOLUTION);
            }
        } else {
            throw new CommandException(MESSAGE_FAILED_SELECTION);
        }
    }

    /**
     * Copy the solution link {@code String} to the system clipboard.
     */
    private void copyToClipBoard(String solution) {
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(solution);
        clipBoard.setContents(selection, null);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RetrieveCommand // instanceof handles nulls
                && targetedIndex.equals(((RetrieveCommand) other).targetedIndex));
    }
}
