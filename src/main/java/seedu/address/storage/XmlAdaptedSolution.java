package seedu.address.storage;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.issue.Solution;
import seedu.address.model.issue.solution.Remark;
import seedu.address.model.issue.solution.SolutionLink;

/**
 * JAXB-friendly adapted version of the Solution.
 */
public class XmlAdaptedSolution {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Solution's %s field is missing!";

    @XmlValue
    private String solutionName;


    /**
     * Constructs an XmlAdaptedSolution. This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedSolution() {
    }

    /**
     * Constructs a {@code XmlAdaptedSolution} with the given {@code solutionName}.
     */
    public XmlAdaptedSolution(String solutionName) {
        this.solutionName = solutionName;
    }

    /**
     * Converts a given Solution into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedSolution(Solution source) {
        solutionName = source.getLink() + " " + source.getRemark();
    }

    /**
     * Converts this jaxb-friendly adapted solution object into the model's Solution object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted issue
     */
    public Solution toModelType() throws IllegalValueException {

        String link = solutionName.substring(0, solutionName.indexOf(' '));
        String remark = solutionName.substring(solutionName.indexOf(' ') + 1);

        if (!Remark.isValidRemark(remark)) {
            throw new IllegalValueException(Remark.MESSAGE_REMARK_CONSTRAINTS);
        }
        if (!SolutionLink.isValidLink(link)) {
            throw new IllegalValueException(SolutionLink.MESSAGE_SOLUTION_LINK_CONSTRAINTS);
        }

        return new Solution(link, remark);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedSolution)) {
            return false;
        }

        return solutionName.equals(((XmlAdaptedSolution) other).solutionName);
    }
}
