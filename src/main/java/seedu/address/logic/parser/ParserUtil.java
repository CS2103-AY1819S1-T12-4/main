package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Solution;
import seedu.address.model.issue.Tag;
import seedu.address.model.issue.solution.Remark;
import seedu.address.model.issue.solution.SolutionLink;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces
     * will be trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code IssueStatement}. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static IssueStatement parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!IssueStatement.isValidIssueStatement(trimmedName)) {
            throw new ParseException(IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);
        }
        return new IssueStatement(trimmedName);
    }

    /**
     * Parses a {@code String description} into a {@code Description}. Leading and trailing whitespaces will
     * be trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static Description parseDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!Description.isValidDescription(trimmedDescription)) {
            throw new ParseException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        return new Description(trimmedDescription);
    }

    /**
     * Parses a {@code String address} into an {@code Remark}. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Remark parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Remark.isValidRemark(trimmedAddress)) {
            throw new ParseException(Remark.MESSAGE_REMARK_CONSTRAINTS);
        }
        return new Remark(trimmedAddress);
    }

    /**
     * Parses {@code Collection<String> solutions} into a {@code Set<Solution>}.
     * @param solutionLink
     * @param solutionRemark
     */
    public static List<Solution> parseSolutions(String solutionLink, String solutionRemark) throws ParseException {
        requireAllNonNull(solutionLink, solutionRemark);
        final List<Solution> solutionList = new ArrayList<>();
        String trimmedSolutionLink = solutionLink.trim();
        String trimmedRemark = solutionRemark.trim();
        if (!SolutionLink.isValidLink(trimmedSolutionLink)) {
            throw new ParseException(SolutionLink.MESSAGE_SOLUTION_LINK_CONSTRAINTS);
        }
        if (!Remark.isValidRemark(trimmedRemark)) {
            throw new ParseException(Remark.MESSAGE_REMARK_CONSTRAINTS);
        }
        solutionList.add(new Solution (solutionLink, solutionRemark));
        return solutionList;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
