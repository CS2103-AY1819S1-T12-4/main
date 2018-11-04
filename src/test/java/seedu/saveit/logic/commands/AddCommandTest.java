package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ReadOnlySaveIt;
import seedu.saveit.model.SaveIt;
import seedu.saveit.model.issue.IssueSort;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.testutil.IssueBuilder;

public class AddCommandTest {

    private static final int ROOT_LEVEL_INDEX = 0;
    private static final int EMPTY_SOLUTION_LIST_INDEX = 0;
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private static final Directory HOME_DIRECTORY = new Directory(ROOT_LEVEL_INDEX, EMPTY_SOLUTION_LIST_INDEX);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_issueAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingIssueAdded modelStub = new ModelStubAcceptingIssueAdded();
        Issue validIssue = new IssueBuilder().build();

        CommandResult commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_ISSUE_SUCCESS, validIssue), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validIssue), modelStub.issuesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateIssue_throwsCommandException() throws Exception {
        Issue validIssue = new IssueBuilder().build();
        AddCommand addCommand = new AddCommand(validIssue);
        ModelStub modelStub = new ModelStubWithIssue(validIssue);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_ISSUE);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Issue alice = new IssueBuilder().withStatement("Alice").build();
        Issue bob = new IssueBuilder().withStatement("Bob").build();
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
     * A default model stub that have all of the methods failing except method getCurrentDirectory();
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
        public void resetDirectory(Directory currentDirectory) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Directory getCurrentDirectory() {
            return HOME_DIRECTORY;
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
        public boolean hasSolution(Index index, Solution solution) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteIssue(Issue target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addSolution(Index index, Solution solution) {
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
        public ObservableList<Solution> getFilteredSolutionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Issue> getFilteredAndSortedIssueList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void filterIssues(Predicate<Issue> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredIssueList(Predicate<Issue> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortIssues(IssueSort sortType) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAndSortedIssueList(Comparator<Issue> sortType) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TreeSet<String> getCurrentTagSet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TreeSet<String> getCurrentIssueStatementSet() {
            throw new AssertionError("This method should not be called.");
        }

        public void addTag(Index index, Set<Tag> tagList) {
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

        @Override
        public boolean refactorTag(Tag oldTag, Tag newTag) {
            return false;
        }
    }

    /**
     * A Model stub that contains a single issue.
     */
    private class ModelStubWithIssue extends ModelStub {

        private final Issue issue;

        ModelStubWithIssue(Issue issue) {
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
    private class ModelStubAcceptingIssueAdded extends ModelStub {

        final ArrayList<Issue> issuesAdded = new ArrayList<>();

        @Override
        public boolean hasIssue(Issue issue) {
            requireNonNull(issue);
            return issuesAdded.stream().anyMatch(issue::isSameIssue);
        }

        @Override
        public void addIssue(Issue issue) {
            requireNonNull(issue);
            issuesAdded.add(issue);
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
