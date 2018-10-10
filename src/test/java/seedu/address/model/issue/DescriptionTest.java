package seedu.address.model.issue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.testutil.Assert;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidDescription));
    }

    @Test
    @Ignore
    public void isValidDescription() {
        // null descriptions number
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid descriptions numbers
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only
        assertFalse(Description.isValidDescription("91")); // less than 3 numbers
        assertFalse(Description.isValidDescription("descriptions")); // non-numeric
        assertFalse(Description.isValidDescription("9011p041")); // alphabets within digits
        assertFalse(Description.isValidDescription("9312 1534")); // spaces within digits

        // valid descriptions numbers
        assertTrue(Description.isValidDescription("911")); // exactly 3 numbers
        assertTrue(Description.isValidDescription("93121534"));
        assertTrue(Description.isValidDescription("124293842033123")); // long descriptions numbers
    }
}
