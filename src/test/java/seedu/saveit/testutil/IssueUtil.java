package seedu.saveit.testutil;

import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.saveit.logic.commands.AddCommand;
import seedu.saveit.logic.commands.EditCommand;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * A utility class for Issue.
 */
public class IssueUtil {

    /**
     * Returns an add command string for adding the {@code issue}.
     */
    public static String getAddCommand(Issue issue) {
        return AddCommand.COMMAND_WORD + " " + getIssueDetails(issue);
    }

    /**
     * Returns the part of command string for the given {@code issue}'s details.
     */
    public static String getIssueDetails(Issue issue) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_STATEMENT + issue.getStatement().issue + " ");
        sb.append(PREFIX_DESCRIPTION + issue.getDescription().value + " ");
        issue.getSolutions().stream().forEach(
            s -> sb.append(PREFIX_SOLUTION_LINK + s.getLink().getValue() + " " + PREFIX_REMARK
                    + s.getRemark().getValue() + " ")
        );
        issue.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditIssueDescriptor}'s details.
     */
    public static String getEditIssueDescriptorDetails(EditCommand.EditIssueDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getStatement().ifPresent(name -> sb.append(PREFIX_STATEMENT).append(name.issue).append(" "));
        descriptor.getDescription()
            .ifPresent(description -> sb.append(PREFIX_DESCRIPTION).append(description.value).append(" "));
        if (descriptor.getSolutions().isPresent()) {
            List<Solution> solutions = descriptor.getSolutions().get();
            if (solutions.isEmpty()) {
                sb.append(PREFIX_SOLUTION_LINK).append(" ");
            } else {
                solutions.forEach(
                    s -> sb.append(PREFIX_SOLUTION_LINK)
                            .append(s.getLink().getValue()).append(" ")
                            .append(PREFIX_REMARK)
                            .append(s.getRemark().getValue()).append(" ")
                );
            }
        }

        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
