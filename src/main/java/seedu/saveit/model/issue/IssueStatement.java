package seedu.saveit.model.issue;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.util.AppUtil;

/**
 * Represents a Issue's name in the saveIt.
 * Guarantees: immutable; is valid as declared in {@link #isValidIssueStatement(String)}
 */
public class IssueStatement {

    public static final String MESSAGE_ISSUE_STATEMENT_CONSTRAINTS =
            "Issue statement can take any values, but it should not be blank.\n"
                + "and should not be more than 25 characters.";

    public static final int LENGTH_LIMIT = 25;

    /*
     * The first character of the saveit must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ISSUE_STATEMENT_VALIDATION_REGEX = "[^\\s].*";

    private final String issue;

    /**
     * Constructs a {@code IssueStatement}.
     *
     * @param issue A valid name.
     */
    public IssueStatement(String issue) {
        requireNonNull(issue);
        AppUtil.checkArgument(isValidIssueStatement(issue), MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);
        this.issue = issue;
    }

    /**
     * Returns issue statement.
     */
    public String getValue() {
        return issue;
    }

    /**
     * Returns true if a given string is a valid issuestatement.
     */
    public static boolean isValidIssueStatement(String test) {
        return test.matches(ISSUE_STATEMENT_VALIDATION_REGEX) && test.length() <= LENGTH_LIMIT;
    }


    @Override
    public String toString() {
        return issue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IssueStatement // instanceof handles nulls
                && issue.equals(((IssueStatement) other).issue)); // state check
    }

    @Override
    public int hashCode() {
        return issue.hashCode();
    }

}
