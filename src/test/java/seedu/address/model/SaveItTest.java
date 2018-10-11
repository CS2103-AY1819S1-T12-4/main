package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SOLUTION_STACKOVERLOW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalSaveIt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.issue.exceptions.DuplicateIssueException;
import seedu.address.testutil.PersonBuilder;

public class SaveItTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final SaveIt saveIt = new SaveIt();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), saveIt.getPersonList());
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
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two issues with the same identity fields
        Issue editedAlice = new PersonBuilder(ALICE).withSolutions(VALID_SOLUTION_STACKOVERLOW)
                .withTags(VALID_TAG_HUSBAND).build();
        List<Issue> newIssues = Arrays.asList(ALICE, editedAlice);
        SaveItStub newData = new SaveItStub(newIssues);

        thrown.expect(DuplicateIssueException.class);
        saveIt.resetData(newData);
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveIt.hasPerson(null);
    }

    @Test
    public void hasPerson_personNotInSaveIt_returnsFalse() {
        assertFalse(saveIt.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInSaveIt_returnsTrue() {
        saveIt.addPerson(ALICE);
        assertTrue(saveIt.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInSaveIt_returnsTrue() {
        saveIt.addPerson(ALICE);
        Issue editedAlice = new PersonBuilder(ALICE).withSolutions(VALID_SOLUTION_STACKOVERLOW)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(saveIt.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        saveIt.getPersonList().remove(0);
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
        public ObservableList<Issue> getPersonList() {
            return issues;
        }
    }

}
