package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Issue;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.SaveIt;
import seedu.address.model.issue.Description;
import seedu.address.model.issue.IssueStatement;
import seedu.address.model.issue.Solution;
import seedu.address.model.issue.Tag;

/**
 * Contains utility methods for populating {@code SaveIt} with sample data.
 */
public class SampleDataUtil {

    public static Issue[] getSamplePersons() {
        return new Issue[]{
            new Issue(new IssueStatement("Java NullPointer"), new Description("cannot find object"),
                    getSolutionSet("StackOverflow newSolution"), getTagSet("solved")),
            new Issue(new IssueStatement("StackOverflow"), new Description("Cannot run"),
                    getSolutionSet("IVLE newBug", "Wikipedia remark1"), getTagSet("newBug", "notSolved")),
            new Issue(new IssueStatement("ArrayIndexOutOfBounds"), new Description("invalid input"),
                    getSolutionSet("ZhiHu newSolution"), getTagSet("notSolved")),
            new Issue(new IssueStatement("ClassNotFoundException"), new Description("WrongPackage"),
                    getSolutionSet("StackOverflow new"), getTagSet("urgent")),
            new Issue(new IssueStatement("ExceptionNotHandled"), new Description("Mistake"),
                    getSolutionSet("Forum solution", "Oracle remark1"), getTagSet("solved")),
            new Issue(new IssueStatement("UnknownBug"), new Description("Unknown"),
                    getSolutionSet("NoSolution NoRemark"), getTagSet("Dead"))
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
     * Returns a solution set containing the list of strings given.
     */
    public static Set<Solution> getSolutionSet(String... strings) {
        Set<Solution> solutionSet = new HashSet<>();
        for (String solutions : strings) {
            String link = solutions.substring(0, solutions.indexOf(' '));
            String remark = solutions.substring(solutions.indexOf(' ') + 1);
            solutionSet.add(new Solution(link, remark));
        }
        return solutionSet;
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
