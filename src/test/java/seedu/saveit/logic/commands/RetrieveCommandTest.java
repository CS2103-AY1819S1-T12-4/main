package seedu.saveit.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_SECOND_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

import org.junit.Before;
import org.junit.Test;

import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.parser.ParserUtil;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.model.issue.solution.SolutionLink;

public class RetrieveCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
        expectedModel = new ModelManager(model.getSaveIt(), new UserPrefs());
    }

    @Test
    public void execute_retrieveValidIndex_success() {
        try {
            Index issueIndex = ParserUtil.parseIndex("3");
            model.resetDirectory(new Directory(issueIndex.getOneBased(), 0));
            Index solutionIndex = ParserUtil.parseIndex("1");
            String expectedMessage = String.format(RetrieveCommand.MESSAGE_RETRIEVE_LINK_SUCCESS,
                    solutionIndex.getOneBased());
            RetrieveCommand retrieveCommand = new RetrieveCommand(solutionIndex);
            assertCommandSuccess(retrieveCommand, model, commandHistory, expectedMessage, model);
            SolutionLink link = new SolutionLink((String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor));
            SolutionLink expectedSolutionLink = model.getFilteredAndSortedIssueList().get(2)
                    .getSolutions().get(0).getLink();
            assertEquals(expectedSolutionLink, link);
            assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
        } catch (Exception e) {
            throw new AssertionError("There should not be an error retrieving the solution link", e);
        }
    }

    @Test
    public void execute_retrieveInvalidIndex_failure() {
        Index solutionIndex;
        try {
            Index issueIndex = ParserUtil.parseIndex("3");
            model.resetDirectory(new Directory(issueIndex.getOneBased(), 0));
            solutionIndex = ParserUtil.parseIndex("2");
        } catch (ParseException e) {
            throw new AssertionError("There should not be any parse exception", e);
        }
        String expectedMessage = RetrieveCommand.MESSAGE_FAILED_SOLUTION;
        RetrieveCommand retrieveCommand = new RetrieveCommand(solutionIndex);
        assertCommandFailure(retrieveCommand, model, commandHistory, expectedMessage);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_retrieveInvalidDirectory_failure() {
        Index solutionIndex;
        try {
            solutionIndex = ParserUtil.parseIndex("2");
        } catch (ParseException e) {
            throw new AssertionError("There should not be any parse exception", e);
        }
        String expectedMessage = RetrieveCommand.MESSAGE_FAILED_SELECTION;
        RetrieveCommand retrieveCommand = new RetrieveCommand(solutionIndex);
        assertCommandFailure(retrieveCommand, model, commandHistory, expectedMessage);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void equals() {
        final RetrieveCommand standardCommand = new RetrieveCommand(INDEX_FIRST_ISSUE);

        RetrieveCommand sameCommand = new RetrieveCommand(INDEX_FIRST_ISSUE);
        assertEquals(standardCommand, sameCommand);
        assertEquals(sameCommand, sameCommand);
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new RetrieveCommand(INDEX_SECOND_ISSUE)));
    }
}
