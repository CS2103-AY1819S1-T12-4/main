package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.Issue;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Remark;
import seedu.address.model.issue.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code issue}'s details
     */
    public EditPersonDescriptorBuilder(Issue issue) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(issue.getStatement());
        descriptor.setDescription(issue.getDescription());
        descriptor.setAddress(issue.getAddress());
        descriptor.setTags(issue.getTags());
    }

    /**
     * Sets the {@code IssueStatement} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new IssueStatement(name));
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withDescription(String descriptions) {
        descriptor.setDescription(new Description(descriptions));
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Remark(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
