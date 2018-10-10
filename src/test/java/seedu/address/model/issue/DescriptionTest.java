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
        // null description number
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidPhone(null));

        // invalid description numbers
        assertFalse(Description.isValidPhone("")); // empty string
        assertFalse(Description.isValidPhone(" ")); // spaces only
        assertFalse(Description.isValidPhone("91")); // less than 3 numbers
        assertFalse(Description.isValidPhone("description")); // non-numeric
        assertFalse(Description.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Description.isValidPhone("9312 1534")); // spaces within digits

        // valid description numbers
        assertTrue(Description.isValidPhone("911")); // exactly 3 numbers
        assertTrue(Description.isValidPhone("93121534"));
        assertTrue(Description.isValidPhone("124293842033123")); // long description numbers
    }
}
