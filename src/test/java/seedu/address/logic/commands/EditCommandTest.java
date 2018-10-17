package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalSaveIt;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditCommand.EditIssueDescriptor;
import seedu.address.model.Issue;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.SaveIt;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.IssueBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Issue editedIssue = new IssueBuilder().build();
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(model.getFilteredIssueList().get(0), editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredIssueList().size());
        Issue lastIssue = model.getFilteredIssueList().get(indexLastPerson.getZeroBased());

        IssueBuilder personInList = new IssueBuilder(lastIssue);
        Issue editedIssue = personInList.withStatement(VALID_STATEMENT_C).withDescription(VALID_DESCRIPTION_C)
                .withTags(VALID_TAG_UI).build();

        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder().withStatement(VALID_STATEMENT_C)
                .withDescription(VALID_DESCRIPTION_C).withTags(VALID_TAG_UI).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(lastIssue, editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, new EditIssueDescriptor());
        Issue editedIssue = model.getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_ISSUE);


        Issue issueInFilteredList = model.getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        Issue editedIssue = new IssueBuilder(issueInFilteredList).withStatement(VALID_STATEMENT_C).build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE,
                new EditPersonDescriptorBuilder().withStatement(VALID_STATEMENT_C).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(model.getFilteredIssueList().get(0), editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Issue firstIssue = model.getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(firstIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_ISSUE);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_ISSUE);

        // edit issue in filtered list into a duplicate in address book
        Issue issueInList = model.getSaveIt().getIssueList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE,
                new EditPersonDescriptorBuilder(issueInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_ISSUE);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredIssueList().size() + 1);
        EditCommand.EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withStatement(VALID_STATEMENT_C).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_ISSUE);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getSaveIt().getIssueList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withStatement(VALID_STATEMENT_C).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Issue editedIssue = new IssueBuilder().build();
        Issue issueToEdit = model.getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        EditCommand.EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, descriptor);
        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(issueToEdit, editedIssue);
        expectedModel.commitSaveIt();

        // edit -> first issue edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts address back to previous state and filtered issue list to show all persons
        expectedModel.undoSaveIt();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first issue edited again
        expectedModel.redoSaveIt();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredIssueList().size() + 1);
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder().withStatement(VALID_STATEMENT_C).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Issue} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited issue in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the issue object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        Issue editedIssue = new IssueBuilder().build();
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, descriptor);
        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Issue issueToEdit = model.getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        expectedModel.updateIssue(issueToEdit, editedIssue);
        expectedModel.commitSaveIt();

        // edit -> edits second issue in unfiltered issue list / first issue in filtered issue list
        editCommand.execute(model, commandHistory);

        // undo -> reverts address back to previous state and filtered issue list to show all persons
        expectedModel.undoSaveIt();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased()), issueToEdit);
        // redo -> edits same second issue in unfiltered issue list
        expectedModel.redoSaveIt();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_ISSUE, DESC_AMY);

        // same values -> returns true
        EditIssueDescriptor copyDescriptor = new EditIssueDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_ISSUE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_ISSUE, DESC_BOB)));
    }

}
