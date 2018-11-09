package seedu.saveit.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.model.Model.PREDICATE_SHOW_ALL_ISSUES;
import static seedu.saveit.testutil.TypicalIssues.ALICE;
import static seedu.saveit.testutil.TypicalIssues.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.saveit.model.issue.IssueContainsKeywordsPredicate;
import seedu.saveit.testutil.SaveItBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasIssue_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasIssue(null);
    }

    @Test
    public void hasIssue_issueNotInSaveIt_returnsFalse() {
        assertFalse(modelManager.hasIssue(ALICE));
    }

    @Test
    public void hasIssue_issueInSaveIt_returnsTrue() {
        modelManager.addIssue(ALICE);
        assertTrue(modelManager.hasIssue(ALICE));
    }

    @Test
    public void getFilteredAndSortedIssueList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredAndSortedIssueList().remove(0);
    }

    @Test
    public void equals() {
        SaveIt saveIt = new SaveItBuilder().withIssue(ALICE).withIssue(BENSON).build();
        SaveIt differentSaveIt = new SaveIt();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(saveIt, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(saveIt, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different saveIt -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentSaveIt, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getStatement().getValue().split("\\s+");
        modelManager.updateFilteredIssueList(new IssueContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(saveIt, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredIssueList(PREDICATE_SHOW_ALL_ISSUES);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setSaveItFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(saveIt, differentUserPrefs)));
    }
}
