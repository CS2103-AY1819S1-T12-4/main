package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
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
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Issue editedIssue = new PersonBuilder().build();
        EditCommand.EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Issue lastIssue = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastIssue);
        Issue editedIssue = personInList.withName(VALID_NAME_BOB).withDescription(VALID_DESCRIPTION_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withDescription(VALID_DESCRIPTION_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updatePerson(lastIssue, editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditCommand.EditIssueDescriptor());
        Issue editedIssue = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Issue issueInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Issue editedIssue = new PersonBuilder(issueInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Issue firstIssue = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(firstIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit issue in filtered list into a duplicate in address book
        Issue issueInList = model.getSaveIt().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(issueInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditCommand.EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getSaveIt().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Issue editedIssue = new PersonBuilder().build();
        Issue issueToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updatePerson(issueToEdit, editedIssue);
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
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

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
        Issue editedIssue = new PersonBuilder().build();
        EditIssueDescriptor descriptor = new EditPersonDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Issue issueToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.updatePerson(issueToEdit, editedIssue);
        expectedModel.commitSaveIt();

        // edit -> edits second issue in unfiltered issue list / first issue in filtered issue list
        editCommand.execute(model, commandHistory);

        // undo -> reverts address back to previous state and filtered issue list to show all persons
        expectedModel.undoSaveIt();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), issueToEdit);
        // redo -> edits same second issue in unfiltered issue list
        expectedModel.redoSaveIt();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditIssueDescriptor copyDescriptor = new EditIssueDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
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
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

}
