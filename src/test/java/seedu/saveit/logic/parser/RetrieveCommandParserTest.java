package seedu.saveit.logic.parser;

import static seedu.saveit.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.saveit.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;

import org.junit.Test;

import seedu.saveit.logic.commands.RetrieveCommand;

public class RetrieveCommandParserTest {
    private RetrieveCommandParser parser = new RetrieveCommandParser();

    @Test
    public void parse_validIndex_returnsRetrieveCommand() {
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "1", new RetrieveCommand(INDEX_FIRST_ISSUE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, RetrieveCommand.MESSAGE_USAGE));
    }
}
