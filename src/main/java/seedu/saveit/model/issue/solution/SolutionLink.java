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
        "[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

    public final String value;

    /**
     * Construct a new solution link. Since the the link value will always be checked before creating new solution link,
     * MalformedURLException is redundant.
     *
     * @param link url to the solution website.
     */
    public SolutionLink(String link) {
        requireNonNull(link);
        AppUtil.checkArgument(isValidLink(link), MESSAGE_SOLUTION_LINK_CONSTRAINTS);
        this.value = link;
    }

    /**
     * Returns if a given string is a valid URL. Not sure if this method is needed. Depends on later implementation.
     */
    public static boolean isValidLink(String test) {
        return test.matches(SOLUTION_LINK_VALIDATION_REGEX);
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
