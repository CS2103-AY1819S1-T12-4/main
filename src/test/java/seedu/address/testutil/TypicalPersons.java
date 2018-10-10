package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.Issue;
import seedu.address.model.SaveIt;

/**
 * A utility class containing a list of {@code Issue} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Issue ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withDescription("94351253")
            .withTags("friends").build();
    public static final Issue BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withTags("owesMoney", "friends").build();
    public static final Issue CARL = new PersonBuilder().withName("Carl Kurz").withDescription("95352563")
            .withAddress("wall street").build();
    public static final Issue DANIEL = new PersonBuilder().withName("Daniel Meier").withDescription("87652533")
            .withAddress("10th street").withTags("friends").build();
    public static final Issue ELLE = new PersonBuilder().withName("Elle Meyer").withDescription("9482224")
            .withAddress("michegan ave").build();
    public static final Issue FIONA = new PersonBuilder().withName("Fiona Kunz").withDescription("9482427")
            .withAddress("little tokyo").build();
    public static final Issue GEORGE = new PersonBuilder().withName("George Best").withDescription("9482442")
            .withAddress("4th street").build();

    // Manually added
    public static final Issue HOON = new PersonBuilder().withName("Hoon Meier").withDescription("8482424")
            .withAddress("little india").build();
    public static final Issue IDA = new PersonBuilder().withName("Ida Mueller").withDescription("8482131")
            .withAddress("chicago ave").build();

    // Manually added - Issue's details found in {@code CommandTestUtil}
    public static final Issue AMY = new PersonBuilder().withName(VALID_NAME_AMY).withDescription(VALID_DESCRIPTION_AMY)
            .withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Issue BOB = new PersonBuilder().withName(VALID_NAME_BOB).withDescription(VALID_DESCRIPTION_BOB)
            .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code SaveIt} with all the typical persons.
     */
    public static SaveIt getTypicalSaveIt() {
        SaveIt ab = new SaveIt();
        for (Issue issue : getTypicalPersons()) {
            ab.addPerson(issue);
        }
        return ab;
    }

    public static List<Issue> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
