package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Solution;

/**
 * Adds an issue to the saveIt.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an issue to the saveIt. "
            + "Parameters: "
            + PREFIX_STATEMENT + "ISSUE_STATEMENT "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_TAG + "algorithm "
            + PREFIX_TAG + "java\n"
            + "_____________________________\n"
            + COMMAND_WORD
            + ": Adds a solution to a existing issue. "
            + "Parameters: "
            + PREFIX_SOLUTION_LINK_STRING + "SOLUTION_LINK"
            + PREFIX_REMARK_STRING + "SOLUTION_REMARK";
    public static final String MESSAGE_ISSUE_SUCCESS = "New issue added: %1$s";
    public static final String MESSAGE_DUPLICATE_ISSUE = "This issue already exists in the saveIt";
    public static final String MESSAGE_DUPLICATE_SOLUTION = "This solution already exists in the given issue";
    public static final String DUMMY_STATEMENT = "dummyStatement";
    public static final String DUMMY_DESCRIPTION = "dummyDescription";

    private static final String MESSAGE_FAILED_ISSUE =
            "Issue has to be selected first before adding solution";
    private static final String MESSAGE_WRONG_DIRECTORY = "Wrong directory, please check!";
    private static final String MESSAGE_SOLUTION_SUCCESS = "New solution added: %1$s";
    private boolean addSolution;
    private final Solution solutionToBeAdded;
    private final Issue toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Issue}
     */
    public AddCommand(Issue issue) {
        if (issue.getStatement().issue.equals(DUMMY_STATEMENT) && issue.getDescription().value
                .equals(DUMMY_DESCRIPTION)) {
            addSolution = true;
            assert (issue.getSolutions().size() == 1);
            solutionToBeAdded = issue.getSolutions().get(0);
            toAdd = null;
        } else {
            addSolution = false;
            requireNonNull(issue);
            toAdd = issue;
            solutionToBeAdded = null;
        }
    }

    /**
     * Add a solution to a existing issue in the issue list
     */
    private void addSolutionToIssue(Model model, Index index) throws CommandException {
        if (model.hasSolution(index, solutionToBeAdded)) {
            throw new CommandException(MESSAGE_DUPLICATE_SOLUTION);
        }
        model.addSolution(index, solutionToBeAdded);
        model.commitSaveIt();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (addSolution) {
            if (model.getCurrentDirectory().isIssueLevel()) {
                Index issueIndex = Index.fromOneBased(model.getCurrentDirectory().getIssue());
                addSolutionToIssue(model, issueIndex);
                return new CommandResult(String.format(MESSAGE_SOLUTION_SUCCESS, solutionToBeAdded));
            } else {
                throw new CommandException(MESSAGE_FAILED_ISSUE);
            }
        }

        if (model.getCurrentDirectory().isIssueLevel()) {
            throw new CommandException(MESSAGE_WRONG_DIRECTORY);
        }

        if (model.hasIssue(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ISSUE);
        }

        model.addIssue(toAdd);
        model.commitSaveIt();
        addSolution = false;
        return new CommandResult(String.format(MESSAGE_ISSUE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
