package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Issue;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Solution;
import seedu.address.model.issue.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    private final String dummyStatement = "dummyStatement";
    private final String dummyDescription = "dummyDescription";

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand and returns an AddCommand
     * object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_SOLUTION_LINK,
                        PREFIX_REMARK, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_SOLUTION_LINK, PREFIX_REMARK)
                || !argMultimap.getPreamble().isEmpty()) {
            if (arePrefixesPresent(argMultimap, PREFIX_SOLUTION_LINK, PREFIX_REMARK) && !arePrefixesPresent(argMultimap,
                    PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_TAG)) {
                List<Solution> solutionList = ParserUtil
                        .parseSolutions(argMultimap.getValue(PREFIX_SOLUTION_LINK).get(),
                                argMultimap.getValue(PREFIX_REMARK).get());
                Set<Tag> dummyTagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

                Issue issue = new Issue(new IssueStatement(dummyStatement), new Description(dummyDescription), solutionList, dummyTagList);

                return new AddCommand(issue);
            }
            else {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }
        }

        IssueStatement statement = ParserUtil.parseName(argMultimap.getValue(PREFIX_STATEMENT).get());
        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        List<Solution> solutionList = ParserUtil
                .parseSolutions(argMultimap.getValue(PREFIX_SOLUTION_LINK).get(),
                        argMultimap.getValue(PREFIX_REMARK).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Issue issue = new Issue(statement, description, solutionList, tagList);

        return new AddCommand(issue);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given {@code
     * ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
