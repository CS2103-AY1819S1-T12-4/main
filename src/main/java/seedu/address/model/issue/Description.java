package seedu.address.model.issue;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents a Issue's description in the saveIt.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {


    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
        "Issue description can take any values, but it should not be blank";
    public static final String DESCRIPTION_VALIDATION_REGEX = "[^\\s].*";
    public final String value;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description.
     */
    public Description(String description) {
        requireNonNull(description);
        AppUtil.checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        value = description;
    }

    /**
     * Returns true if a given string is a valid descriptions number.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
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
