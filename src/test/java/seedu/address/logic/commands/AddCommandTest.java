package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Issue;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.SaveIt;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Issue validIssue = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_ISSUE_SUCCESS, validIssue), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validIssue), modelStub.personsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        Issue validIssue = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validIssue);
        ModelStub modelStub = new ModelStubWithPerson(validIssue);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Issue alice = new PersonBuilder().withName("Alice").build();
        Issue bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different issue -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addIssue(Issue issue) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlySaveIt newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlySaveIt getSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasIssue(Issue issue) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteIssue(Issue target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateIssue(Issue target, Issue editedIssue) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Issue> getFilteredIssueList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredIssueList(Predicate<Issue> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitSaveIt() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single issue.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Issue issue;

        ModelStubWithPerson(Issue issue) {
            requireNonNull(issue);
            this.issue = issue;
        }

        @Override
        public boolean hasIssue(Issue issue) {
            requireNonNull(issue);
            return this.issue.isSameIssue(issue);
        }
    }

    /**
     * A Model stub that always accept the issue being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Issue> personsAdded = new ArrayList<>();

        @Override
        public boolean hasIssue(Issue issue) {
            requireNonNull(issue);
            return personsAdded.stream().anyMatch(issue::isSameIssue);
        }

        @Override
        public void addIssue(Issue issue) {
            requireNonNull(issue);
            personsAdded.add(issue);
        }

        @Override
        public void commitSaveIt() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlySaveIt getSaveIt() {
            return new SaveIt();
        }
    }

}
