package seedu.saveit.logic;

import static org.junit.Assert.assertEquals;
import static seedu.saveit.commons.core.Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX;
import static seedu.saveit.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.saveit.testutil.TypicalIndexes.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.logic.commands.ClearCommand;
import seedu.saveit.logic.commands.CommandResult;
import seedu.saveit.logic.commands.DangerCommand;
import seedu.saveit.logic.commands.HistoryCommand;
import seedu.saveit.logic.commands.ListCommand;
import seedu.saveit.logic.commands.SelectCommand;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.testutil.DirectoryBuilder;
import seedu.saveit.testutil.IssueBuilder;
import seedu.saveit.testutil.SolutionBuilder;


public class LogicManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager();
    private Logic logic = new LogicManager(model);

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
        assertHistoryCorrect(invalidCommand);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
        assertHistoryCorrect(deleteCommand);
    }

    @Test
    public void execute_validCommand_success() {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
        assertHistoryCorrect(listCommand);
    }


    @Test
    public void execute_confirmedDangerCommand_success() {
        String clearCommand = ClearCommand.COMMAND_WORD;
        assertCommandSuccess(clearCommand,
                String.format(DangerCommand.ASK_FOR_CONFIRMATION, ClearCommand.COMMAND_WORD), model);
        assertCommandSuccess(LogicManager.CONFIRM_ALIAS, ClearCommand.MESSAGE_SUCCESS, model);
        assertHistoryCorrect(LogicManager.CONFIRM_ALIAS, clearCommand);
    }

    @Test
    public void execute_unconfirmedDangerCommand_success() {
        String clearCommand = ClearCommand.COMMAND_WORD;
        String input = "any other word";
        assertCommandSuccess(clearCommand,
                String.format(DangerCommand.ASK_FOR_CONFIRMATION, ClearCommand.COMMAND_WORD), model);
        assertCommandSuccess(input, String.format(DangerCommand.CONFIRMATION_FAILED, ClearCommand.COMMAND_WORD), model);
        assertHistoryCorrect(input, clearCommand);
    }

    @Test
    public void getFilteredAndSortedIssueList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        logic.getFilteredAndSortedIssueList().remove(0);
    }

    @Test
    public void getFilteredSolutionList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        model.addIssue(new IssueBuilder().build());
        assertCommandSuccess(SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased(),
                String.format(SelectCommand.MESSAGE_SELECT_ISSUE_SUCCESS, INDEX_FIRST_ISSUE.getOneBased()), model);
        logic.getFilteredSolutionList().add(new SolutionBuilder().build());
    }

    @Test
    public void resetValidDirectory_success() {
        // reset to the root directory
        Directory rootDirectory = Directory.formRootDirectory();
        logic.resetDirectory(rootDirectory);
        assertEquals(model.getCurrentDirectory(), rootDirectory);

        // reset to valid Issue directory
        model.addIssue(new IssueBuilder().build());
        Directory validIssueDirectory = new DirectoryBuilder().withIssueIndex(INDEX_FIRST_ISSUE).build();
        logic.resetDirectory(validIssueDirectory);
        assertEquals(model.getCurrentDirectory(), validIssueDirectory);

        // reset to valid Solution directory
        model.addSolution(model.getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased()),
                new SolutionBuilder().build());
        Directory validSolutionDirectory = new DirectoryBuilder().withIssueIndex(INDEX_FIRST_ISSUE)
                .withSolutionIndex(INDEX_FIRST_SOLUTION).build();
        logic.resetDirectory(validSolutionDirectory);
        assertEquals(model.getCurrentDirectory(), validSolutionDirectory);
    }

    @Test
    public void resetInvalidDirectory_success() {
        Directory rootDirectory = Directory.formRootDirectory();
        // reset to invalid Issue directory
        Directory invalidIssueDirectory = new DirectoryBuilder().withIssueIndex(INDEX_FIRST_ISSUE).build();
        logic.resetDirectory(invalidIssueDirectory);
        assertEquals(model.getCurrentDirectory(), rootDirectory);

        // reset to valid Solution directory
        model.addIssue(new IssueBuilder().build());
        Directory invalidSolutionDirectory = new DirectoryBuilder().withIssueIndex(INDEX_FIRST_ISSUE)
                .withSolutionIndex(INDEX_FIRST_SOLUTION).build();
        logic.resetDirectory(invalidSolutionDirectory);
        assertEquals(model.getCurrentDirectory(), rootDirectory);
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedModel} is as specified.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<?> expectedException, String expectedMessage) {
        Model expectedModel = new ModelManager(model.getSaveIt(), new UserPrefs());
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     *      - the internal model manager data are same as those in the {@code expectedModel} <br>
     *      - {@code expectedModel}'s saveit book was saved to the storage file.
     */
    private void assertCommandBehavior(Class<?> expectedException, String inputCommand,
                                           String expectedMessage, Model expectedModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertEquals(expectedException, null);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, model);
    }

    /**
     * Asserts that the result display shows all the {@code expectedCommands} upon the execution of
     * {@code HistoryCommand}.
     */
    private void assertHistoryCorrect(String... expectedCommands) {
        try {
            CommandResult result = logic.execute(HistoryCommand.COMMAND_WORD);
            String expectedMessage = String.format(
                    HistoryCommand.MESSAGE_SUCCESS, String.join("\n", expectedCommands));
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (ParseException | CommandException e) {
            throw new AssertionError("Parsing and execution of HistoryCommand.COMMAND_WORD should succeed.", e);
        }
    }
}
