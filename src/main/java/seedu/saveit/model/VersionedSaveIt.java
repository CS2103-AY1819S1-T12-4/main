package seedu.saveit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code SaveIt} that keeps track of its own history.
 */
public class VersionedSaveIt extends SaveIt {

    private final List<ReadOnlySaveIt> saveItStateList;
    private int currentStatePointer;

    public VersionedSaveIt(ReadOnlySaveIt initialState) {
        super(initialState);

        saveItStateList = new ArrayList<>();
        saveItStateList.add(new SaveIt(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code SaveIt} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        SaveIt saveIt = new SaveIt(this);
        saveItStateList.add(saveIt);
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        saveItStateList.subList(currentStatePointer + 1, saveItStateList.size()).clear();
    }

    /**
     * Restores the saveIt to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        ReadOnlySaveIt saveIt = saveItStateList.get(currentStatePointer);
        resetData(saveIt);
    }

    /**
     * Restores the saveIt to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(saveItStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has saveIt states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has saveIt states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < saveItStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedSaveIt)) {
            return false;
        }

        VersionedSaveIt otherVersionedSaveIt = (VersionedSaveIt) other;

        // state check
        return super.equals(otherVersionedSaveIt)
                && saveItStateList.equals(otherVersionedSaveIt.saveItStateList)
                && currentStatePointer == otherVersionedSaveIt.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of saveItState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of saveItState list, unable to redo.");
        }
    }
}
