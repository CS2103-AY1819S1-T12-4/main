package seedu.saveit.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.saveit.commons.core.LogsCenter;
import seedu.saveit.commons.events.model.DirectoryChangedEvent;
import seedu.saveit.commons.events.model.SortTypeChangedEvent;
import seedu.saveit.commons.events.ui.NewResultAvailableEvent;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "ResultDisplay.fxml";
    private static final String ROOT_DIRECTORY = "../SaveIt";
    private static final String DEFAULT_SORT_TYPE = "Sort By: Default";

    private final StringProperty displayed = new SimpleStringProperty("");
    private final StringProperty currentDirectory = new SimpleStringProperty(ROOT_DIRECTORY);
    private final StringProperty currentSortType = new SimpleStringProperty(DEFAULT_SORT_TYPE);

    @FXML
    private TextArea resultDisplay;

    @FXML
    private Label directory;

    @FXML
    private Label sortType;

    public ResultDisplay() {
        super(FXML);
        resultDisplay.textProperty().bind(displayed);
        directory.textProperty().bind(currentDirectory);
        sortType.textProperty().bind(currentSortType);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> displayed.setValue(event.message));
    }


    @Subscribe
    private void handleChangeDirectoryRequestEvent(DirectoryChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> currentDirectory.setValue(event.directory.toString()));
    }

    @Subscribe
    private void handleChangeSortTypeRequestEvent(SortTypeChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> currentSortType.setValue(event.sortType.toString()));
    }
}
