package seedu.saveit.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.*;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.ui.SolutionListPanel;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(IssueCardHandle expectedCard, IssueCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getStatement(), actualCard.getStatement());
        assertEquals(expectedCard.getDescription(), actualCard.getDescription());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedIssue}.
     */
    public static void assertCardDisplaysIssue(Issue expectedIssue, IssueCardHandle actualCard) {
        assertEquals(expectedIssue.getStatement().getValue(), actualCard.getStatement());
        assertEquals(expectedIssue.getDescription().getValue(), actualCard.getDescription());
        assertEquals(expectedIssue.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedSolution}.
     */
    public static void assertCardDisplaysSolution(Solution expectedSolution, SolutionCardHandle actualCard) {
        assertEquals(expectedSolution.getLink().getValue(), actualCard.getLink());
        assertEquals(expectedSolution.getRemark().toString(), actualCard.getRemark());
    }

    /**
     * Asserts that the list in {@code issueListPanelHandle} displays the details of {@code issues} correctly and
     * in the correct order.
     */
    public static void assertListMatching(IssueListPanelHandle issueListPanelHandle, Issue... issues) {
        for (int i = 0; i < issues.length; i++) {
            issueListPanelHandle.navigateToCard(i);
            assertCardDisplaysIssue(issues[i], issueListPanelHandle.getIssueCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code issueListPanelHandle} displays the details of {@code issues} correctly and
     * in the correct order.
     */
    public static void assertListMatching(IssueListPanelHandle issueListPanelHandle, List<Issue> issues) {
        assertListMatching(issueListPanelHandle, issues.toArray(new Issue[0]));
    }

    /**
     * Asserts that the list in {@code solutionListPanelHandle} displays the details of {@code solutions} correctly and
     * in the correct order.
     */
    public static void assertListMatching(SolutionListPanelHandle solutionListPanelHandle, Solution... solutions) {
        for (int i = 0; i < solutions.length; i++) {
            //solutionListPanelHandle.navigateToCard(i);
            assertCardDisplaysSolution(solutions[i], solutionListPanelHandle.getSolutionCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code solutionListPanelHandle} displays the details of {@code solutions} correctly and
     * in the correct order.
     */
    public static void assertListMatching(SolutionListPanelHandle solutionListPanelHandle, List<Solution> solutions) {
        assertListMatching(solutionListPanelHandle, solutions.toArray(new Solution[0]));
    }

    /**
     * Asserts the size of the list in {@code issueListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(IssueListPanelHandle issueListPanelHandle, int size) {
        int numberOfPeople = issueListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
