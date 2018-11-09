package seedu.saveit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.testutil.TypicalIssues.ALICE;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;
import static seedu.saveit.testutil.TypicalSolutions.SOLUTION_STACKOVERFLOW;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.model.issue.exceptions.DuplicateIssueException;
import seedu.saveit.testutil.IssueBuilder;

public class SaveItTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final SaveIt saveIt = new SaveIt();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), saveIt.getIssueList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveIt.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlySaveIt_replacesData() {
        SaveIt newData = getTypicalSaveIt();
        saveIt.resetData(newData);
        assertEquals(newData, saveIt);
    }

    @Test
    public void resetData_withDuplicateIssues_throwsDuplicateIssueException() {
        // Two issues with the same identity fields
        Issue editedAlice = new IssueBuilder(ALICE)
                .withSolutions(SOLUTION_STACKOVERFLOW)
                .withTags(VALID_TAG_UI).build();
        List<Issue> newIssues = Arrays.asList(ALICE, editedAlice);
        SaveItStub newData = new SaveItStub(newIssues);

        thrown.expect(DuplicateIssueException.class);
        saveIt.resetData(newData);
    }

    @Test
    public void hasIssue_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveIt.hasIssue(null);
    }

    @Test
    public void hasIssue_issueNotInSaveIt_returnsFalse() {
        assertFalse(saveIt.hasIssue(ALICE));
    }

    @Test
    public void hasIssue_issueInSaveIt_returnsTrue() {
        saveIt.addIssue(ALICE);
        assertTrue(saveIt.hasIssue(ALICE));
    }

    @Test
    public void hasIssue_issueWithSameIdentityFieldsInSaveIt_returnsTrue() {
        saveIt.addIssue(ALICE);
        Issue editedAlice = new IssueBuilder(ALICE)
                .withSolutions(SOLUTION_STACKOVERFLOW)
                .withTags(VALID_TAG_UI).build();
        assertTrue(saveIt.hasIssue(editedAlice));
    }

    @Test
    public void getIssueList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        saveIt.getIssueList().remove(0);
    }

    /**
     * A stub ReadOnlySaveIt whose issues list can violate interface constraints.
     */
    private static class SaveItStub implements ReadOnlySaveIt {

        private final ObservableList<Issue> issues = FXCollections.observableArrayList();

        SaveItStub(Collection<Issue> issues) {
            this.issues.setAll(issues);
        }

        @Override
        public ObservableList<Issue> getIssueList() {
            return issues;
        }

        @Override
        public Directory getCurrentDirectory() {
            return new Directory(0, 0);
        }

        @Override
        public Comparator<Issue> getCurrentSortType() {
            return null;
        }
    }

}
