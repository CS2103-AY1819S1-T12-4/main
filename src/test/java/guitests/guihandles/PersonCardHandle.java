package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.Issue;

/**
 * Provides a handle to a issue card in the issue list panel.
 */
public class PersonCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String DESCRIPTION_FIELD_ID = "#description";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label addressLabel;
    private final Label descriptionsLabel;
    private final List<Label> tagLabels;

    public PersonCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        nameLabel = getChildNode(NAME_FIELD_ID);
        addressLabel = getChildNode(ADDRESS_FIELD_ID);
        descriptionsLabel = getChildNode(DESCRIPTION_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getDescription() {
        return descriptionsLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code issue}.
     */
    public boolean equals(Issue issue) {
        return getName().equals(issue.getStatement().issue)
                && getAddress().equals(issue.getAddress().value)
                && getDescription().equals(issue.getDescription().value)
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(issue.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
