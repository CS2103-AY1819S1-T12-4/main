package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.Issue;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Remark;
import seedu.address.model.issue.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Issue objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private IssueStatement name;
    private Description description;
    private Remark address;
    private Set<Tag> tags;

    public PersonBuilder() {
        name = new IssueStatement(DEFAULT_NAME);
        description = new Description(DEFAULT_PHONE);
        address = new Remark(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code issueToCopy}.
     */
    public PersonBuilder(Issue issueToCopy) {
        name = issueToCopy.getStatement();
        description = issueToCopy.getDescription();
        address = issueToCopy.getAddress();
        tags = new HashSet<>(issueToCopy.getTags());
    }

    /**
     * Sets the {@code IssueStatement} of the {@code Issue} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new IssueStatement(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Issue} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code Issue} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Remark(address);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Issue} that we are building.
     */
    public PersonBuilder withPhone(String description) {
        this.description = new Description(description);
        return this;
    }

    public Issue build() {
        return new Issue(name, description, address, tags);
    }

}
