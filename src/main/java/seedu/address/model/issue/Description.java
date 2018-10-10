package seedu.address.model.issue;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents a Issue's description number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Description {


    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Description numbers should only contain numbers, and it should be at least 3 digits long";
    public static final String PHONE_VALIDATION_REGEX = "\\d{3,}";
    public final String value;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description number.
     */
    public Description(String description) {
        requireNonNull(description);
        AppUtil.checkArgument(isValidPhone(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        value = description;
    }

    /**
     * Returns true if a given string is a valid description number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(PHONE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && value.equals(((Description) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
