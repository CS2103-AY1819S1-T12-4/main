package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Issue;
import seedu.address.model.Model;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddCommandSystemTest extends SaveItSystemTest {

    @Test
    @Ignore
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list
        ----------------------------- */

        /* Case: add an issue without tags to a non-empty address book, command with leading spaces and
        trailing spaces
         * -> added
         */
        Issue toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + DESCRIPTION_DESC_AMY
            + " " + ADDRESS_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addIssue(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add an issue with all fields same as another issue in the address book except name -> added */
        toAdd = new PersonBuilder(AMY).withName(VALID_NAME_BOB).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + DESCRIPTION_DESC_AMY
            + ADDRESS_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add an issue with all fields same as another issue in the address book except description
         * -> added
         */
        toAdd = new PersonBuilder(AMY).withDescription(VALID_DESCRIPTION_BOB).build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty address book -> added */
        deleteAllPersons();
        assertCommandSuccess(ALICE);

        /* Case: add an issue with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + DESCRIPTION_DESC_BOB + ADDRESS_DESC_BOB
            + NAME_DESC_BOB + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, toAdd);

        /* Case: add an issue, missing tags -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list
        ------------------------------ */

        /* Case: filters the issue list before adding -> added */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while an issue card is selected
        --------------------------- */

        /* Case: selects first card in the issue list, add an issue -> added, card selection remains
        unchanged */
        selectPerson(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations
        --------------------------------------- */

        /* Case: add a duplicate issue -> rejected */
        command = PersonUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate issue except with different description -> rejected */
        toAdd = new PersonBuilder(HOON).withDescription(VALID_DESCRIPTION_BOB).build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate issue except with different address -> rejected */
        toAdd = new PersonBuilder(HOON).withDescription(VALID_ADDRESS_BOB).build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate issue except with different tags -> rejected */
        command = PersonUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + DESCRIPTION_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing description -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + PersonUtil.getPersonDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + DESCRIPTION_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);

        /* Case: invalid descriptions -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_DESCRIPTION_DESC + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DESCRIPTION_DESC_AMY + ADDRESS_DESC_AMY
            + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br> 1. Command box
     * displays an empty string.<br> 2. Command box has the default style class.<br> 3. Result display box displays the
     * success message of executing {@code AddCommand} with the details of {@code toAdd}.<br> 4. {@code Storage} and
     * {@code PersonListPanel} equal to the corresponding components in the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br> 6. Status bar's sync status changes.<br> Verifications 1,
     * 3 and 4 are performed by {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Issue toAdd) {
        assertCommandSuccess(PersonUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Issue)}. Executes {@code command} instead.
     *
     * @see AddCommandSystemTest#assertCommandSuccess(Issue)
     */
    private void assertCommandSuccess(String command, Issue toAdd) {
        Model expectedModel = getModel();
        expectedModel.addIssue(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_ISSUE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Issue)} except asserts that the,<br> 1.
     * Result display box displays {@code expectedResultMessage}.<br> 2. {@code Storage} and {@code PersonListPanel}
     * equal to the corresponding components in {@code expectedModel}.<br>
     *
     * @see AddCommandSystemTest#assertCommandSuccess(String, Issue)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br> 1. Command box displays {@code command}.<br> 2. Command box
     * has the error style class.<br> 3. Result display box displays {@code expectedResultMessage}.<br> 4. {@code
     * Storage} and {@code PersonListPanel} remain unchanged.<br> 5. Browser url, selected card and status bar remain
     * unchanged.<br> Verifications 1, 3 and 4 are performed by
     * {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
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
