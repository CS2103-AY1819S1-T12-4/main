package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ISSUES;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Issue;
import seedu.address.model.Model;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class EditCommandSystemTest extends SaveItSystemTest {

    @Test
    @Ignore
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown
        ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between
        each field
         * -> edited
         */
        Index index = INDEX_FIRST_ISSUE;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  "
                + NAME_DESC_BOB + "  " + DESCRIPTION_DESC_BOB + " "
                + ADDRESS_DESC_BOB + " " + TAG_DESC_HUSBAND + " ";
        Issue editedIssue = new PersonBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: undo editing the last issue in the list -> last issue restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last issue in the list -> last issue edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateIssue(
            getModel().getFilteredIssueList().get(INDEX_FIRST_ISSUE.getZeroBased()), editedIssue);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit an issue with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + DESCRIPTION_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit an issue with new values same as another issue's values but with different name ->
        edited */
        assertTrue(getModel().getSaveIt().getIssueList().contains(BOB));
        index = INDEX_SECOND_PERSON;
        assertNotEquals(getModel().getFilteredIssueList().get(index.getZeroBased()), BOB);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + DESCRIPTION_DESC_BOB
            + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedIssue = new PersonBuilder(BOB).withName(VALID_NAME_AMY).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: edit an issue with new values same as another issue's values but with different description
         * -> edited
         */
        index = INDEX_SECOND_PERSON;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + DESCRIPTION_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedIssue = new PersonBuilder(BOB).withDescription(VALID_DESCRIPTION_AMY).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_ISSUE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Issue issueToEdit = getModel().getFilteredIssueList().get(index.getZeroBased());
        editedIssue = new PersonBuilder(issueToEdit).withTags().build();
        assertCommandSuccess(command, index, editedIssue);

        /* ------------------ Performing edit operation while a filtered list is being shown
        ------------------------ */

        /* Case: filtered issue list, edit index within bounds of address book and issue list -> edited */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_ISSUE;
        assertTrue(index.getZeroBased() < getModel().getFilteredIssueList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        issueToEdit = getModel().getFilteredIssueList().get(index.getZeroBased());
        editedIssue = new PersonBuilder(issueToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: filtered issue list, edit index within bounds of address book but out of bounds of issue list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getSaveIt().getIssueList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
            Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while an issue card is selected
        -------------------------- */

        /* Case: selects first card in the issue list, edit an issue -> edited, card selection remains
        unchanged but
         * browser url changes
         */
        showAllPersons();
        index = INDEX_FIRST_ISSUE;
        selectPerson(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + DESCRIPTION_DESC_AMY
            + ADDRESS_DESC_AMY + TAG_DESC_FRIEND;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new issue's name
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation
        -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredIssueList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
            Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased(),
            EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(
            EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased() + INVALID_NAME_DESC,
            IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);

        /* Case: invalid description -> rejected */
        assertCommandFailure(
            EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased() + INVALID_DESCRIPTION_DESC,
            Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(
            EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased() + INVALID_TAG_DESC,
            Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit an issue with new values same as another issue's values -> rejected */
        executeCommand(PersonUtil.getAddCommand(BOB));
        assertTrue(getModel().getSaveIt().getIssueList().contains(BOB));
        index = INDEX_FIRST_ISSUE;
        assertFalse(getModel().getFilteredIssueList().get(index.getZeroBased()).equals(BOB));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + DESCRIPTION_DESC_BOB
            + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);

        /* Case: edit an issue with new values same as another issue's values but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + DESCRIPTION_DESC_BOB
            + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);

        /* Case: edit an issue with new values same as another issue's values but with different address -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + DESCRIPTION_DESC_BOB
            + ADDRESS_DESC_AMY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);

        /* Case: edit an issue with new values same as another issue's values but with different description ->
         rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + DESCRIPTION_DESC_AMY
            + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Issue, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Issue, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Issue editedIssue) {
        assertCommandSuccess(command, toEdit, editedIssue, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br> 2. Asserts
     * that the model related components are updated to reflect the issue at index {@code toEdit} being updated to
     * values specified {@code editedIssue}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Issue editedIssue,
        Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.updateIssue(expectedModel.getFilteredIssueList().get(toEdit.getZeroBased()),
            editedIssue);
        expectedModel.updateFilteredIssueList(PREDICATE_SHOW_ALL_ISSUES);

        assertCommandSuccess(command, expectedModel,
            String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue),
            expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br> 1. Asserts that the command box displays an empty string.<br> 2.
     * Asserts that the result display box displays {@code expectedResultMessage}.<br> 3. Asserts that the browser url
     * and selected card update accordingly depending on the card at {@code expectedSelectedCardIndex}.<br> 4. Asserts
     * that the status bar's sync status changes.<br> 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String,
     * Model)}.<br>
     *
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see SaveItSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
        Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredIssueList(PREDICATE_SHOW_ALL_ISSUES);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br> 1. Asserts that the command box displays {@code command}.<br> 2.
     * Asserts that result display box displays {@code expectedResultMessage}.<br> 3. Asserts that the browser url,
     * selected card and status bar remain unchanged.<br> 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String,
     * Model)}.<br>
     *
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
