package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SOLUTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Issue;
import seedu.address.model.Model;

/**
 * Adds a issue to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an issue to the saveIt. "
        + "Parameters: "
        + PREFIX_STATEMENT + "ISSUE_STATEMENT "
        + PREFIX_DESCRIPTION + "DESCRIPTION "
        + "[" + PREFIX_SOLUTION + "SOLUTION_LINK REMARK]...\n"
        + "[" + PREFIX_TAG + "TAG]...\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_STATEMENT + "algorithm "
        + PREFIX_DESCRIPTION + "This is an algorithm problem "
        + PREFIX_SOLUTION + "StackOverflow new Solution"
        + PREFIX_TAG + "algorithm "
        + PREFIX_TAG + "java";
    public static final String MESSAGE_SUCCESS = "New issue added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This issue already exists in the saveIt";

    private final Issue toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Issue}
     */
    public AddCommand(Issue issue) {
        requireNonNull(issue);
        toAdd = issue;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        model.commitSaveIt();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddCommand // instanceof handles nulls
            && toAdd.equals(((AddCommand) other).toAdd));
    }
}
