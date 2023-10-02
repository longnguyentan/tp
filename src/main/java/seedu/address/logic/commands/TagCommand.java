package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

import java.util.List;
import java.util.Set;

/**
 * Adds or modifies tags for a person in the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds or modifies tags for a person in the address book. "
            + "Parameters: INDEX "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_TAG + "friends " + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "Tags added/modified for person: %1$s";

    private final Index targetIndex;
    private final Set<Tag> tagsToAdd;

    /**
     * Creates a TagCommand to add or modify tags for the specified person at the given index.
     */
    public TagCommand(Index targetIndex, Set<Tag> tagsToAdd) {
        this.targetIndex = targetIndex;
        this.tagsToAdd = tagsToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getOneBased() < 0 || targetIndex.getOneBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getOneBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getAddress(), personToEdit.getTags());

        // Add or modify tags
        editedPerson.getTags().addAll(tagsToAdd);

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return targetIndex == otherTagCommand.targetIndex
                && tagsToAdd.equals(otherTagCommand.tagsToAdd);
    }
}
