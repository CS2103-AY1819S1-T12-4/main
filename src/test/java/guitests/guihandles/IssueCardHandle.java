package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.saveit.model.Issue;

/**
 * Provides a handle to an issue card in the issue list panel.
 */
public class IssueCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String STATEMENT_FIELD_ID = "#statement";
    private static final String DESCRIPTION_FIELD_ID = "#description";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label statementLabel;
    private final Label descriptionsLabel;
    private final List<Label> tagLabels;

    public IssueCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        statementLabel = getChildNode(STATEMENT_FIELD_ID);
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

    public String getStatement() {
        return statementLabel.getText();
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
        return getStatement().equals(issue.getStatement().issue)
                && getDescription().equals(issue.getDescription().value)
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(issue.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
