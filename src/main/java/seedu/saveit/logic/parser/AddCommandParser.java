package seedu.saveit.logic.parser;

import static seedu.saveit.commons.util.StringUtil.arePrefixesNotPresent;
import static seedu.saveit.commons.util.StringUtil.arePrefixesPresent;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.logic.commands.AddCommand;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    private static final String DUMMY_SOLUTION_LINK = "https://www.dummySolutionLink.com";
    private static final String DUMMY_SOLUTION_REMARK = "dummySolutionRemark";
    private static final String NO_LINK_EXCEPTION = "Please enter solution link";
    private static final String NO_REMARK_EXCEPTION = "Please enter solution remark";
    private static final String NO_STATEMENT_EXCEPTION = "Please enter statement";
    private static final String NO_DESCRIPTION_EXCEPTION = "Please enter description";

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

        if (arePrefixesPresent(args, PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_TAG) && argMultimap
                .getPreamble().isEmpty() && arePrefixesNotPresent(args, PREFIX_SOLUTION_LINK,
                PREFIX_REMARK)) {
            return handleAddIssueParser(argMultimap);
        } else if (arePrefixesPresent(args, PREFIX_SOLUTION_LINK, PREFIX_REMARK)
                && arePrefixesNotPresent(args, PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_TAG)) {
            return handleAddSolutionParser(argMultimap);
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Handles parsing the add command which intends to add a new issue.
     */
    private AddCommand handleAddIssueParser(ArgumentMultimap argMultimap) throws ParseException {
        IssueStatement statement;

        if (argMultimap.getValue(PREFIX_STATEMENT).isPresent()) {
            statement = ParserUtil.parseStatement(argMultimap.getValue(PREFIX_STATEMENT).get());
        } else {
            throw new ParseException(NO_STATEMENT_EXCEPTION);
        }

        Description description;

        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        } else {
            throw new ParseException(NO_DESCRIPTION_EXCEPTION);
        }

        List<Solution> solutionList = ParserUtil.parseSolutions(DUMMY_SOLUTION_LINK, DUMMY_SOLUTION_REMARK);
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Issue issue = new Issue(statement, description, solutionList, tagList);

        return new AddCommand(issue);
    }

    /**
     * Handles parsing the add command which intends to add solution.
     */
    private AddCommand handleAddSolutionParser(ArgumentMultimap argMultimap) throws ParseException {

        String solutionLink;

        if (argMultimap.getValue(PREFIX_SOLUTION_LINK).isPresent()) {
            solutionLink = ParserUtil.parseSolutionLink(argMultimap.getValue(PREFIX_SOLUTION_LINK).get()).getValue();
        } else {
            throw new ParseException(NO_LINK_EXCEPTION);
        }

        String solutionRemark;

        if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
            solutionRemark = ParserUtil.parseSolutionRemark(argMultimap.getValue(PREFIX_REMARK).get()).getValue();
        } else {
            throw new ParseException(NO_REMARK_EXCEPTION);
        }

        List<Solution> solutionList = new ArrayList<>();
        solutionList.add(new Solution(solutionLink, solutionRemark));

        Set<Tag> dummyTagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Issue issue = new Issue(new IssueStatement(AddCommand.DUMMY_STATEMENT),
                new Description(AddCommand.DUMMY_DESCRIPTION),
                solutionList, dummyTagList);

        return new AddCommand(issue);
    }
}
