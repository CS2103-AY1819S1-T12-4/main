package seedu.saveit.commons.events.model;

import java.util.Comparator;

import seedu.saveit.commons.events.BaseEvent;
import seedu.saveit.model.Issue;

/**
 * Indicates model sortType was changed.
 */
public class SortTypeChangedEvent extends BaseEvent {
    public final Comparator<Issue> sortType;

    public SortTypeChangedEvent(Comparator<Issue> sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
