package seedu.saveit.logic;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.logic.commands.CommandResult;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.Solution;

/**
 * API of the Logic component
 * used to handle the logic regarding the execution of commands
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();

    /** Returns an unmodifiable view of the filtered list of issues */
    ObservableList<Issue> getFilteredAndSortedIssueList();

    /** Returns an unmodifiable view of the filtered list of solutions of the selected issue */
    ObservableList<Solution> getFilteredSolutionList();

    /** Reset the directory of current model */
    void resetDirectory(Directory directory);
}
