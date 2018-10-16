package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        if (argMultimap.getValue(PREFIX_STATEMENT).isPresent()) {
            editIssueDescriptor
                .setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_STATEMENT).get()));
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            editIssueDescriptor
                .setDescription(ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        parseSolutionsForEdit(argMultimap.getAllValues(PREFIX_SOLUTION_LINK))
                .ifPresent(editIssueDescriptor::setSolutions);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editIssueDescriptor::setTags);

        if (!editIssueDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editIssueDescriptor);
    }

    /**
     * Parses {@code Collection<String> solutions} into a {@code Set<Solution>} if {@code solutions} is non-empty. If
     * {@code solutions} contain only one element which is an empty string, it will be parsed into a {@code
     * Set<Solution>} containing zero solutions.
     */
    private Optional<List<Solution>> parseSolutionsForEdit(Collection<String> solutions) throws ParseException {
        assert solutions != null;

        if (solutions.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> solutionList =
            solutions.size() == 1 && solutions.contains("") ? Collections.emptySet() : solutions;
        //TODO:
        return Optional.of(ParserUtil.parseSolutions("ew","ewe"));
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
