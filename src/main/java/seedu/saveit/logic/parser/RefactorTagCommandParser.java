package seedu.saveit.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.saveit.commons.util.StringUtil.arePrefixesPresent;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.saveit.logic.parser.ParserUtil.parseTag;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.logic.commands.RefactorTagCommand;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.issue.Tag;


/**
 * Parses input arguments and creates a new RefactorTagCommand object
 */
public class RefactorTagCommandParser implements Parser<RefactorTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RefactorTagCommand and returns an
     * RefactorTagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public RefactorTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
            ArgumentTokenizer
                .tokenize(args, PREFIX_TAG, PREFIX_NEW_TAG);

        Tag newTag;
        Tag oldTag;

        if (arePrefixesPresent(args, PREFIX_DESCRIPTION, PREFIX_STATEMENT, PREFIX_SOLUTION_LINK, PREFIX_REMARK)) {
            throw new ParseException(
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, RefactorTagCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            oldTag = parseTag(argMultimap.getValue(PREFIX_TAG).get());
        } else {
            throw new ParseException(
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, RefactorTagCommand.MESSAGE_USAGE));
        }
        if (argMultimap.getValue(PREFIX_NEW_TAG).isPresent()) {
            newTag = parseTag(argMultimap.getValue(PREFIX_NEW_TAG).get());
            return new RefactorTagCommand(oldTag, newTag);
        } else {
            return new RefactorTagCommand(oldTag);
        }

    }


}

