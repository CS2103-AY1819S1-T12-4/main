package seedu.saveit.commons.events.model;

import seedu.saveit.commons.events.BaseEvent;
import seedu.saveit.model.Issue;

import java.util.Comparator;

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
