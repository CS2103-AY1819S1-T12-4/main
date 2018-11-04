package seedu.saveit.model.issue.solution;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.util.AppUtil;

/**
 * Represents a Issue's solution link in saveit.
 */
public class SolutionLink {

    public static final String MESSAGE_SOLUTION_LINK_CONSTRAINTS =
        "SolutionLinks can only take a validate url.";

    public static final String SOLUTION_LINK_VALIDATION_REGEX =
            "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

    private static final String linkPrefix = "https://";
    private final String value;

    /**
     * Construct a new solution link.
     * @param value url to the solution website.
     */
    public SolutionLink(String value) {
        requireNonNull(value);
        value = appendUrlPrefix(value);
        AppUtil.checkArgument(isValidLink(value), MESSAGE_SOLUTION_LINK_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns if a given string is a valid URL.
     */
    public static boolean isValidLink(String test) {
        test = appendUrlPrefix(test);
        return test.matches(SOLUTION_LINK_VALIDATION_REGEX);
    }

    public String getValue() {
        return this.value;
    }

    private static String appendUrlPrefix(String value) {
        return value.startsWith("http") ? value : linkPrefix + value;
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
