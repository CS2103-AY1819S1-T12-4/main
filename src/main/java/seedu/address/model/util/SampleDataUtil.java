package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Issue;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.SaveIt;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Remark;
import seedu.address.model.issue.Tag;

/**
 * Contains utility methods for populating {@code SaveIt} with sample data.
 */
public class SampleDataUtil {
    public static Issue[] getSamplePersons() {
        return new Issue[] {
            new Issue(new IssueStatement("Alex Yeoh"), new Description("87438807"),
                new Remark("Blk 30 Geylang Street 29, #06-40"), getTagSet("friends")),
            new Issue(new IssueStatement("Bernice Yu"), new Description("99272758"),
                new Remark("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), getTagSet("colleagues", "friends")),
            new Issue(new IssueStatement("Charlotte Oliveiro"), new Description("93210283"),
                    new Remark("Blk 11 Ang Mo Kio Street 74, #11-04"), getTagSet("neighbours")),
            new Issue(new IssueStatement("David Li"), new Description("91031282"),
                new Remark("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family")),
            new Issue(new IssueStatement("Irfan Ibrahim"), new Description("92492021"),
                new Remark("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("classmates")),
            new Issue(new IssueStatement("Roy Balakrishnan"), new Description("92624417"),
                new Remark("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlySaveIt getSampleSaveIt() {
        SaveIt sampleAb = new SaveIt();
        for (Issue sampleIssue : getSamplePersons()) {
            sampleAb.addPerson(sampleIssue);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
