package seedu.address.model.issue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid phone numbers
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only
        assertFalse(Description.isValidDescription("91")); // less than 3 numbers
        assertFalse(Description.isValidDescription("phone")); // non-numeric
        assertFalse(Description.isValidDescription("9011p041")); // alphabets within digits
        assertFalse(Description.isValidDescription("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Description.isValidDescription("911")); // exactly 3 numbers
        assertTrue(Description.isValidDescription("93121534"));
        assertTrue(Description.isValidDescription("124293842033123")); // long phone numbers
    }
}
