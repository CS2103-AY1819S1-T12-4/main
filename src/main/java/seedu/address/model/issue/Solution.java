package seedu.address.model.issue;

import java.util.Objects;

import seedu.address.model.issue.solution.Remark;
import seedu.address.model.issue.solution.SolutionLink;

/**
 * Represents a Issue's solution in saveit.
 */
public class Solution {

    public static final String MESSAGE_SOLUTION_CONSTRAINTS = "Solutions names should be alphanumeric";

    public static final String SOLUTION_VALIDATION_REGEX = "\\p{Alnum}+";

    public final String solutionName;

    public final SolutionLink solutionLink;

    public final Remark remark;

    /**
     * Construct a new Solution.
     *
     * @param solutionLink url link to solution website.
     * @param remark user's notes about this solution.
     */
    public Solution(String solutionLink, String remark) {
        this.solutionLink = new SolutionLink(solutionLink);
        this.remark = new Remark(remark);
        this.solutionName = solutionLink + " " + remark;
    }

    public SolutionLink getLink() {
        return solutionLink;
    }

    public Remark getRemark() {
        return remark;
    }

    /**
     * Returns true if a given string is a valid solution name.
     */
    public static boolean isValidSolution(String test) {
        return test.matches(SOLUTION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return '[' + " link: " + getLink() + " remark: " + getRemark() + ']';
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Solution // instanceof handles nulls
                && solutionLink.equals(((Solution) other).solutionLink)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(solutionLink, remark);
    }
}
