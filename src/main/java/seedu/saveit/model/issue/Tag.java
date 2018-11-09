package seedu.saveit.model.issue;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.util.AppUtil;

/**
 * Represents a Tag in the saveIt.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_TAG_CONSTRAINTS =
        "Tags names should not contain white space\n"
            + "and should not more than 20 characters.";
    public static final String TAG_VALIDATION_REGEX = "[\\S]+";

    public static final int LENGTH_LIMIT = 20;

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        AppUtil.checkArgument(isValidTagName(tagName), MESSAGE_TAG_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX) && test.length() <= LENGTH_LIMIT;
    }

    /**
     * Compare two Tag regarding to their tagName.
     */
    public int compare(Tag other) {
        if (other == null) {
            return -1;
        }
        return tagName.compareTo(other.tagName);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
