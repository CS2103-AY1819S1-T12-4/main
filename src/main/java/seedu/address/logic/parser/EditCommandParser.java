package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.util.StringUtil.arePrefixesNotPresent;
import static seedu.address.commons.util.StringUtil.arePrefixesPresent;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditIssueDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.issue.Solution;
import seedu.address.model.issue.Tag;


/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand and returns an EditCommand object
     * for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
            ArgumentTokenizer
                .tokenize(args, PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_SOLUTION_LINK, PREFIX_REMARK, PREFIX_TAG);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        EditIssueDescriptor editIssueDescriptor = new EditIssueDescriptor();

        // check if the command is correct
        if (arePrefixesPresent(args, PREFIX_STATEMENT, PREFIX_DESCRIPTION) && arePrefixesNotPresent(args,
            PREFIX_SOLUTION_LINK, PREFIX_REMARK)) {
            if (argMultimap.getValue(PREFIX_STATEMENT).isPresent()) {
                editIssueDescriptor
                    .setStatement(ParserUtil.parseStatement(argMultimap.getValue(PREFIX_STATEMENT).get()));
            }

            if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
                editIssueDescriptor
                    .setDescription(ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get()));
            }

            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editIssueDescriptor::setTags);

            return new EditCommand(index, editIssueDescriptor);

        } else if (arePrefixesNotPresent(args, PREFIX_STATEMENT, PREFIX_DESCRIPTION) && arePrefixesPresent(args,
            PREFIX_SOLUTION_LINK, PREFIX_REMARK)) {
            EditIssueDescriptor editIssueDescriptorForSolution = null;
            if (argMultimap.getValue(PREFIX_SOLUTION_LINK).isPresent() && argMultimap.getValue(PREFIX_REMARK)
                .isPresent()) {
                Solution solution = parseSolutionForEdit(argMultimap.getValue(PREFIX_SOLUTION_LINK).get(),
                    argMultimap.getValue(PREFIX_REMARK).get());
                editIssueDescriptorForSolution = new EditIssueDescriptor(index, solution);


            } else if (argMultimap.getValue(PREFIX_SOLUTION_LINK).isPresent()) {
                Solution solution = parseSolutionForEdit("dummySolutionLink",
                    argMultimap.getValue(PREFIX_REMARK).get());
                editIssueDescriptorForSolution = new EditIssueDescriptor(index, solution);


            } else if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
                Solution solution = parseSolutionForEdit(argMultimap.getValue(PREFIX_SOLUTION_LINK).get(),
                    "dummySolutionRemark");
                editIssueDescriptorForSolution = new EditIssueDescriptor(index, solution);
            }
            return new EditCommand(index, editIssueDescriptorForSolution);
        } else {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        // TODO: Check Exception
    }

    /**
     * Parses {@code Collection<String> solutions} into a {@code Set<Solution>} if {@code solutions} is non-empty. If
     * {@code solutions} contain only one element which is an empty string, it will be parsed into a {@code
     * Set<Solution>} containing zero solutions.
     */
    private Solution parseSolutionForEdit(String solutionLink, String solutionRemark) throws ParseException {
        Solution solution = ParserUtil.parseSolution(solutionLink, solutionRemark);
        return solution;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty. If {@code tags}
     * contain only one element which is an empty string, it will be parsed into a {@code Set<Tag>} containing zero
     * tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
