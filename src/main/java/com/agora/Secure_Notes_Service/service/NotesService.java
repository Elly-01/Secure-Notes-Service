package com.agora.Secure_Notes_Service.service;

import com.agora.Secure_Notes_Service.model.Notes;
import com.agora.Secure_Notes_Service.repository.NotesRepository;
import com.agora.Secure_Notes_Service.util.EncryptionUtil;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotesService {
    private final NotesRepository noteRepository;

    // Constructor
    public NotesService(NotesRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

/**
 * The function `validateString` takes an object, validates if it is a
 * non-blank string within a certain length limit, and returns an optional
 * string.
 * 
 * @param rawValue The `rawValue` parameter is an `Object` type that
 * represents the value to be validated as a string.
 * @return The `validateString` method returns an `Optional<String>`
 * object.
 */
    private Optional<String> validateString(Object rawValue) {
        return Optional.ofNullable(rawValue)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(s -> !s.isBlank() && s.length() <= 255);
    }

/**
 * The function `createNote` validates and creates a new note with a title
 * and content, encrypts the content, saves it to a repository, and returns
 * the decrypted note.
 * 
 * @param note The `createNote` method takes a `Map<String, Object>` as a
 * parameter, where the keys are "title" and "content" representing the
 * title and content of a note, respectively. The method performs
 * validation on the title and content before creating a new `Notes`
 * object.
 * @return The `createNote` method returns an `Optional` containing a
 * `Notes` object if the input map `note` contains valid title and content
 * values that pass the validation checks. If either the title or content
 * is invalid or missing, the method returns an empty `Optional`.
 */
    public Optional<Notes> createNote(Map<String, Object> note) {
        //validation
        Object rawTitle = note.get("title");
        Optional<String> title = validateString(rawTitle);
        if (title.isEmpty()) {
            return Optional.empty();
        }

        Object rawContent = note.get("content");
        Optional<String> content = validateString(rawContent);
        if (content.isEmpty()) {
            return Optional.empty();
        }

        String encryptedContent = EncryptionUtil.encrypt(content.get());
        Notes newNote = new Notes(title.get(), encryptedContent);
        noteRepository.save(newNote);

        newNote.setContent(EncryptionUtil.decrypt(newNote.getContent()));
        return Optional.of(newNote);
    }

/**
 * This function retrieves all notes from a repository, decrypts their
 * content using EncryptionUtil, and returns the list of notes.
 * 
 * @return The `getAllNotes` method returns a list of `Notes` objects after
 * decrypting the content of each note using the `EncryptionUtil.decrypt`
 * method.
 */
    public List<Notes> getAllNotes() {
        List<Notes> notes = noteRepository.findAll();
        notes.forEach(n ->
                n.setContent(EncryptionUtil.decrypt(n.getContent()))
        );
        return notes;
    }

/**
 * This function retrieves a note by its ID from a repository, decrypts its
 * content using EncryptionUtil, and returns it as an Optional.
 * 
 * @param id The `id` parameter is of type `Long` and represents the unique
 * identifier of the note that is being retrieved.
 * @return The method `getNoteById` is returning an `Optional<Notes>`
 * object.
 */
    public Optional<Notes> getNoteById(Long id) {
        Optional<Notes> note = noteRepository.findById(id);
        if (note.isPresent()) {
            note.get().setContent(EncryptionUtil.decrypt(note.get().getContent()));
        }
        return note;
    }

/**
 * This Java function updates a note by its ID with specified changes after
 * validating and encrypting the content.
 * 
 * @param id The `id` parameter is the unique identifier of the note that
 * you want to update. It is used to find the existing note in the database
 * based on this identifier.
 * @param updates The `updates` parameter in the `updateNoteById` method is
 * a `Map<String, Object>` that contains the fields to be updated for a
 * specific note. The keys in the map represent the fields (e.g., "title",
 * "content") and the values are the new values to be
 * @return The method `updateNoteById` is returning an `Optional<Notes>`
 * object.
 */
    public Optional<Notes> updateNoteById(Long id, Map<String, Object> updates) {

        Optional<Notes> existing = noteRepository.findById(id);
        if (existing.isEmpty()) {return Optional.empty();}

        //validation
        Object rawTitle = updates.get("title");
        validateString(rawTitle)
                .ifPresent(title -> existing.get().setTitle(title));

        Object rawContent = updates.get("content");
        validateString(rawContent)
                .ifPresent(content -> existing.get().setContent(EncryptionUtil.encrypt(content)));


        noteRepository.save(existing.get());
        existing.get().setContent(EncryptionUtil.decrypt(existing.get().getContent()));
        return existing;
    }


/**
 * This Java function deletes a note by its ID if it exists in the note
 * repository.
 * 
 * @param id The `id` parameter is of type `Long` and represents the unique
 * identifier of a note that needs to be deleted from the repository.
 * @return The method `deleteNoteById` returns a boolean value - `true` if
 * a note with the specified id exists and is successfully deleted, and
 * `false` if no note with the specified id exists.
 */
    public boolean deleteNoteById(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
