package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditIssueDescriptor;
import seedu.address.model.Issue;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Tag;

/**
 * A utility class to help with building EditIssueDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditCommand.EditIssueDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditIssueDescriptor();
    }

    public EditPersonDescriptorBuilder(EditIssueDescriptor descriptor) {
        this.descriptor = new EditIssueDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditIssueDescriptor} with fields containing {@code issue}'s details
     */
    public EditPersonDescriptorBuilder(Issue issue) {
        descriptor = new EditIssueDescriptor();
        descriptor.setName(issue.getStatement());
        descriptor.setDescription(issue.getDescription());
        descriptor.setSolutions(issue.getSolutions());
        descriptor.setTags(issue.getTags());
    }

    /**
     * Sets the {@code IssueStatement} of the {@code EditIssueDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new IssueStatement(name));
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditIssueDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withDescription(String descriptions) {
        descriptor.setDescription(new Description(descriptions));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditIssueDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditCommand.EditIssueDescriptor build() {
        return descriptor;
    }
}
