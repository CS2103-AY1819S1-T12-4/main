package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.Issue;
import seedu.address.storage.XmlSerializableSaveIt;

public class IssueListPanelTest extends GuiUnitTest {
    private static final ObservableList<Issue> TYPICAL_ISSUES =
            FXCollections.observableList(getTypicalPersons());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_PERSON);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private PersonListPanelHandle personListPanelHandle;

    @Test
    @Ignore
    public void display() {
        initUi(TYPICAL_ISSUES);

        for (int i = 0; i < TYPICAL_ISSUES.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_ISSUES.get(i));
            Issue expectedIssue = TYPICAL_ISSUES.get(i);
            PersonCardHandle actualCard = personListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedIssue, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    @Ignore
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_ISSUES);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        PersonCardHandle expectedPerson = personListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        PersonCardHandle selectedPerson = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code PersonListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    @Ignore
    public void performanceTest() throws Exception {
        ObservableList<Issue> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of issue cards exceeded time limit");
    }

    /**
     * Returns a list of persons containing {@code personCount} persons that is used to populate the
     * {@code PersonListPanel}.
     */
    private ObservableList<Issue> createBackingList(int personCount) throws Exception {
        Path xmlFile = createXmlFileWithPersons(personCount);
        XmlSerializableSaveIt xmlSaveIt =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableSaveIt.class);
        return FXCollections.observableArrayList(xmlSaveIt.toModelType().getPersonList());
    }

    /**
     * Returns a .xml file containing {@code personCount} persons. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithPersons(int personCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<saveIt>\n");
        for (int i = 0; i < personCount; i++) {
            builder.append("<persons>\n");
            builder.append("<name>").append(i).append("a</name>\n");
            builder.append("<description>000</description>\n");
            builder.append("<address>a</address>\n");
            builder.append("</persons>\n");
        }
        builder.append("</saveIt>\n");

        Path manyPersonsFile = Paths.get(TEST_DATA_FOLDER + "manyPersons.xml");
        FileUtil.createFile(manyPersonsFile);
        FileUtil.writeToFile(manyPersonsFile, builder.toString());
        manyPersonsFile.toFile().deleteOnExit();
        return manyPersonsFile;
    }

    /**
     * Initializes {@code personListPanelHandle} with a {@code PersonListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code PersonListPanel}.
     */
    private void initUi(ObservableList<Issue> backingList) {
        PersonListPanel personListPanel = new PersonListPanel(backingList);
        uiPartRule.setUiPart(personListPanel);

        personListPanelHandle = new PersonListPanelHandle(getChildNode(personListPanel.getRoot(),
                PersonListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}
