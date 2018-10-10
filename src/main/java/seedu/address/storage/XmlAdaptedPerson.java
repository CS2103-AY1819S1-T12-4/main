package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Issue;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Remark;
import seedu.address.model.issue.Tag;

/**
 * JAXB-friendly version of the Issue.
 */
public class XmlAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Issue's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String address;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given issue details.
     */
    public XmlAdaptedPerson(String name, String description, String address, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.description = description;
        this.address = address;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Issue into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(Issue source) {
        name = source.getStatement().issue;
        description = source.getDescription().value;
        address = source.getAddress().value;
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted issue object into the model's Issue object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted issue
     */
    public Issue toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                IssueStatement.class.getSimpleName()));
        }
        if (!IssueStatement.isValidIssueStatement(name)) {
            throw new IllegalValueException(IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);
        }
        final IssueStatement modelName = new IssueStatement(name);

        if (description == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName()));
        }
        if (!Remark.isValidRemark(address)) {
            throw new IllegalValueException(Remark.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Remark modelAddress = new Remark(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Issue(modelName, modelDescription, modelAddress, modelTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedPerson otherPerson = (XmlAdaptedPerson) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(description, otherPerson.description)
                && Objects.equals(address, otherPerson.address)
                && tagged.equals(otherPerson.tagged);
    }
}
