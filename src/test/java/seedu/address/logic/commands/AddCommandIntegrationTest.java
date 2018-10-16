package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalSaveIt;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Issue;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Issue validIssue = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getSaveIt(), new UserPrefs());
        expectedModel.addIssue(validIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(new AddCommand(validIssue), model, commandHistory,
                String.format(AddCommand.MESSAGE_ISSUE_SUCCESS, validIssue), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Issue issueInList = model.getSaveIt().getIssueList().get(0);
        assertCommandFailure(new AddCommand(issueInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
