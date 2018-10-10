package seedu.address.model.issue;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents a Issue's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidIssueStatement(String)}
 */
public class IssueStatement {

    public static final String MESSAGE_ISSUE_STATEMENT_CONSTRAINTS =
            "Issue statement should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ISSUE_STATEMENT_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String issue;

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
     * Returns true if a given string is a valid issuestatement.
     */
    public static boolean isValidIssueStatement(String test) {
        return test.matches(ISSUE_STATEMENT_VALIDATION_REGEX);
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
