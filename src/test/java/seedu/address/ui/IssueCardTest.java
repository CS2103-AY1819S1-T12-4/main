package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Ignore;
import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.model.Issue;
import seedu.address.testutil.PersonBuilder;

public class IssueCardTest extends GuiUnitTest {

    @Test
    @Ignore
    public void display() {
        // no tags
        Issue issueWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(issueWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, issueWithNoTags, 1);

        // with tags
        Issue issueWithTags = new PersonBuilder().build();
        personCard = new PersonCard(issueWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, issueWithTags, 2);
    }

    @Test
    public void equals() {
        Issue issue = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(issue, 0);

        // same issue, same index -> returns true
        PersonCard copy = new PersonCard(issue, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different issue, same index -> returns false
        Issue differentIssue = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentIssue, 0)));

        // same issue, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(issue, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedIssue} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Issue expectedIssue, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify issue details are displayed correctly
        assertCardDisplaysPerson(expectedIssue, personCardHandle);
    }
}
