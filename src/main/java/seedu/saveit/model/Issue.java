package seedu.saveit.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.saveit.commons.util.CollectionUtil;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueSearchFrequency;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.PrimarySolution;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * Represents a Issue in the remark book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Issue {

    private static final int RESET_PRIMARY_SOLUTION = -1;

    // Identity fields
    private final IssueStatement statement;

    // Data fields
    private final List<Solution> solutions = new ArrayList<>();
    private final Description description;
    private final IssueSearchFrequency frequency;
    private final Timestamp lastModifiedTime;
    private final Set<Tag> tags = new LinkedHashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Issue(IssueStatement statement, Description description, List<Solution> solutions, Set<Tag> tags) {
        CollectionUtil.requireAllNonNull(statement, description, solutions, tags);
        this.statement = statement;
        this.description = description;
        this.solutions.addAll(solutions);
        this.tags.addAll(tags);
        this.frequency = new IssueSearchFrequency(0);
        this.lastModifiedTime = new Timestamp(new Date().getTime());
    }

    /**
     * Overloaded constructor with additional {@code frequency} field
     */
    public Issue(IssueStatement statement, Description description, List<Solution> solutions,
            Set<Tag> tags, IssueSearchFrequency frequency) {
        CollectionUtil.requireAllNonNull(statement, description, solutions, tags);
        this.statement = statement;
        this.description = description;
        this.solutions.addAll(solutions);
        this.tags.addAll(tags);
        this.frequency = frequency;
        this.lastModifiedTime = new Timestamp(new Date().getTime());
    }

    /**
     * Overloaded constructor with additional {@code frequency} and field
     */
    public Issue(IssueStatement statement, Description description, List<Solution> solutions,
                 Set<Tag> tags, IssueSearchFrequency frequency, Timestamp lastModifiedTime) {
        CollectionUtil.requireAllNonNull(statement, description, solutions, tags);
        this.statement = statement;
        this.description = description;
        this.solutions.addAll(solutions);
        this.tags.addAll(tags);
        this.frequency = frequency;
        this.lastModifiedTime = lastModifiedTime;
    }

    public IssueStatement getStatement() {
        return statement;
    }

    /**
     * Returns an immutable solution List, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Solution> getSolutions() {
        return Collections.unmodifiableList(solutions);
    }

    /**
     * Returns ab observableList of solutions.
     */
    public ObservableList<Solution> getObservableSolutions() {
        return FXCollections.observableList(solutions);
    }

    public Description getDescription() {
        return description;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns the IssueSearchFrequency of the Issue
     */
    public IssueSearchFrequency getFrequency() {
        return frequency;
    }

    /**
     * Returns the Timestamp of the Issue
     */
    public Timestamp getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * Updates the search frequency of the current issue
     */
    public void updateFrequency() {
        frequency.increment();
    }

    /**
     * Set the primary solution.
     */
    public Issue setPrimarySolution(int index) {
        return updatePrimarySolution(index);
    }

    /**
     * Reset the primary solution.
     */
    public Issue resetPrimarySolution() {
        return updatePrimarySolution(RESET_PRIMARY_SOLUTION);
    }

    /**
     * Updates the primary solution.
     */
    public Issue updatePrimarySolution(int index) {
        List<Solution> newSolutions = new ArrayList<>(this.solutions);

        for (int i = 0; i < newSolutions.size(); i++) {
            Solution s = newSolutions.get(i);
            if (s.isPrimarySolution()) {
                newSolutions.set(i, new Solution(s));
            }
        }

        if (index != RESET_PRIMARY_SOLUTION) {
            PrimarySolution newPrimarySolution = new PrimarySolution(this.solutions.get(index));
            newSolutions.set(index, newPrimarySolution);
        }
        return new Issue(this.statement, this.description, newSolutions, this.tags, this.frequency);
    }

    /**
     * Returns true if both issues of the same statement have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two issues.Solution
     */
    public boolean isSameIssue(Issue otherIssue) {
        if (otherIssue == this) {
            return true;
        }

        return otherIssue != null
                && otherIssue.getStatement().equals(getStatement())
                && otherIssue.getDescription().equals(getDescription());
    }

    /**
     * Returns true if both issues have the same identity and data fields.
     * This defines a stronger notion of equality between two issues.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Issue)) {
            return false;
        }

        Issue otherIssue = (Issue) other;
        return otherIssue.getStatement().equals(getStatement())
                && otherIssue.getDescription().equals(getDescription());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(statement, description, solutions, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getStatement())
                .append(" Description: ")
                .append(getDescription())
                .append(" Solutions: ");
        getSolutions().forEach(builder::append);
        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
