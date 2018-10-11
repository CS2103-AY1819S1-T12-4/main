package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SOLUTION_STACKOVERLOW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.testutil.PersonBuilder;

public class IssueTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Issue issue = new PersonBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        issue.getTags().remove(0);
    }

    @Test
    public void isSameIssue() {
        // same object -> returns true
        assertTrue(ALICE.isSameIssue(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameIssue(null));

        // different description -> returns false
        Issue editedAlice = new PersonBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).build();
        assertFalse(ALICE.isSameIssue(editedAlice));

        // different name -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameIssue(editedAlice));

        // same name, same description, different attributes -> returns true
        editedAlice = new PersonBuilder(ALICE).withSolutions(VALID_SOLUTION_STACKOVERLOW)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameIssue(editedAlice));
    }

    @Test
    @Ignore
    public void equals() {
        // same values -> returns true
        Issue aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different issue -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Issue editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different description -> returns false
        editedAlice = new PersonBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
