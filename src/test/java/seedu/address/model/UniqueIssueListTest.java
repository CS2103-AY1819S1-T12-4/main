package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.issue.exceptions.DuplicateIssueException;
import seedu.address.model.issue.exceptions.IssueNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class UniqueIssueListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueIssueList uniqueIssueList = new UniqueIssueList();

    @Test
    public void contains_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.contains(null);
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniqueIssueList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniqueIssueList.add(ALICE);
        assertTrue(uniqueIssueList.contains(ALICE));
    }

    @Test
    @Ignore
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniqueIssueList.add(ALICE);
        Issue editedAlice = new PersonBuilder(ALICE).withDescription(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueIssueList.contains(editedAlice));
    }

    @Test
    public void add_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.add(null);
    }

    @Test
    public void add_duplicateIssue_throwsDuplicateIssueException() {
        uniqueIssueList.add(ALICE);
        thrown.expect(DuplicateIssueException.class);
        uniqueIssueList.add(ALICE);
    }

    @Test
    public void setIssue_nullTargetIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssue(null, ALICE);
    }

    @Test
    public void setIssue_nullEditedIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssue(ALICE, null);
    }

    @Test
    public void setIssue_targetIssueNotInList_throwsIssueNotFoundException() {
        thrown.expect(IssueNotFoundException.class);
        uniqueIssueList.setIssue(ALICE, ALICE);
    }

    @Test
    @Ignore
    public void setIssue_editedIssueIsSameIssue_success() {
        uniqueIssueList.add(ALICE);
        uniqueIssueList.setIssue(ALICE, ALICE);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(ALICE);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    @Ignore
    public void setIssue_editedIssueHasSameIdentity_success() {
        uniqueIssueList.add(ALICE);
        Issue editedAlice = new PersonBuilder(ALICE).withDescription(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueIssueList.setIssue(ALICE, editedAlice);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(editedAlice);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssue_editedIssueHasDifferentIdentity_success() {
        uniqueIssueList.add(ALICE);
        uniqueIssueList.setIssue(ALICE, BOB);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(BOB);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssue_editedIssueHasNonUniqueIdentity_throwsDuplicateIssueException() {
        uniqueIssueList.add(ALICE);
        uniqueIssueList.add(BOB);
        thrown.expect(DuplicateIssueException.class);
        uniqueIssueList.setIssue(ALICE, BOB);
    }

    @Test
    public void remove_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.remove(null);
    }

    @Test
    public void remove_personDoesNotExist_throwsIssueNotFoundException() {
        thrown.expect(IssueNotFoundException.class);
        uniqueIssueList.remove(ALICE);
    }

    @Test
    public void remove_existingIssue_removesIssue() {
        uniqueIssueList.add(ALICE);
        uniqueIssueList.remove(ALICE);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssues_nullUniqueIssueList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssues((UniqueIssueList) null);
    }

    @Test
    public void setIssues_uniqueIssueList_replacesOwnListWithProvidedUniqueIssueList() {
        uniqueIssueList.add(ALICE);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(BOB);
        uniqueIssueList.setIssues(expectedUniqueIssueList);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssues_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssues((List<Issue>) null);
    }

    @Test
    public void setIssues_list_replacesOwnListWithProvidedList() {
        uniqueIssueList.add(ALICE);
        List<Issue> personList = Collections.singletonList(BOB);
        uniqueIssueList.setIssues(personList);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(BOB);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssues_listWithDuplicateIssues_throwsDuplicateIssueException() {
        List<Issue> listWithDuplicateIssues = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateIssueException.class);
        uniqueIssueList.setIssues(listWithDuplicateIssues);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueIssueList.asUnmodifiableObservableList().remove(0);
    }
}
