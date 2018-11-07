package seedu.saveit.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.ALICE;
import static seedu.saveit.testutil.TypicalIssues.BENSON;
import static seedu.saveit.testutil.TypicalIssues.CARL;
import static seedu.saveit.testutil.TypicalIssues.DANIEL;
import static seedu.saveit.testutil.TypicalIssues.ELLE;
import static seedu.saveit.testutil.TypicalIssues.FIONA;
import static seedu.saveit.testutil.TypicalIssues.GEORGE;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueContainsKeywordsPredicate;
import seedu.saveit.model.issue.IssueSort;
import seedu.saveit.model.issue.IssueStatement;

public class SortCommandTest {
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
        expectedModel = new ModelManager(model.getSaveIt(), new UserPrefs());
    }

    @Test
    public void execute_sortIsNotFiltered_success() {
        IssueSort issueSort = prepareIssueSort(IssueSort.TAG_SORT);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, issueSort.getSortType());
        expectedModel.updateFilteredAndSortedIssueList(issueSort.getComparator());
        SortCommand command = new SortCommand(issueSort);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA, GEORGE, DANIEL, BENSON, ALICE),
                model.getFilteredAndSortedIssueList());
    }

    @Test
    public void execute_sortIsFiltered_success() {
        String[] keywordArray = {"Alice", "Benson", "Daniel", "Meyer"};
        filterIssueList(keywordArray);

        IssueSort issueSort = prepareIssueSort(IssueSort.TAG_SORT);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, issueSort.getSortType());
        expectedModel.updateFilteredAndSortedIssueList(issueSort.getComparator());
        SortCommand command = new SortCommand(issueSort);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ELLE, DANIEL, BENSON, ALICE), model.getFilteredAndSortedIssueList());
    }

    @Test
    public void execute_sortAfterUpdate_success() {
        IssueSort issueSort = prepareIssueSort(IssueSort.TAG_SORT);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, issueSort.getSortType());
        Issue issue = new Issue(new IssueStatement("new C++ problem"),
                new Description("only for test"), new ArrayList<>(), new HashSet<>());

        expectedModel.updateFilteredAndSortedIssueList(issueSort.getComparator());
        expectedModel.addIssue(issue);
        model.addIssue(issue);
        SortCommand command = new SortCommand(issueSort);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA, GEORGE, issue, DANIEL, BENSON, ALICE),
                model.getFilteredAndSortedIssueList());
    }

    @Test
    public void execute_notUnderRootLevel_failure() {
        model.resetDirectory(Directory.formDirectory(INDEX_FIRST_ISSUE.getOneBased(), 0));
        IssueSort issueSort = prepareIssueSort(IssueSort.TAG_SORT);
        SortCommand command = new SortCommand(issueSort);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_WRONG_DIRECTORY);
    }

    /**
     * Parses {@code userInput} into a {@code Comparator<Issue>}.
     */
    private IssueSort prepareIssueSort(String userInput) {
        return new IssueSort(userInput);
    }

    /**
     * Filter issue lists in both model and expectedModel by the provided keywords.
     * @param keywordArray
     */
    private void filterIssueList(String[] keywordArray) {
        List<String> keywords = Arrays.asList(keywordArray);
        IssueContainsKeywordsPredicate predicate = new IssueContainsKeywordsPredicate(keywords);
        model.updateFilteredIssueList(predicate);
        expectedModel.updateFilteredIssueList(predicate);
    }
}
