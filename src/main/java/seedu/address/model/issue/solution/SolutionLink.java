package seedu.address.model.issue.solution;

/**
 * Represents a Issue's solution link in saveit.
 */
public class SolutionLink {

    public static final String MESSAGE_SOLUTION_LINK_CONSTRAINTS =
            "Remarks can take any values, and it should not be blank";

    public static final String LINK_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Construct a new solution link.
     *
     * @param value url to the solution website.
     */
    public SolutionLink(String value) {
        this.value = value;
    }

    /**
     * Returns if a given string is a valid URL.
     * Not sure if this method is needed. Depends on later implementation.
     */
    public static boolean isValidLink(String test) {
        return test.matches(LINK_VALIDATION_REGEX);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SolutionLink // instanceof handles nulls
                && value.equals(((SolutionLink) other).getValue())); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
