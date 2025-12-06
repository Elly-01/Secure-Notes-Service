package com.agora.Secure_Notes_Service.controller;

import com.agora.Secure_Notes_Service.model.Notes;
import com.agora.Secure_Notes_Service.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

/**
 * This Java function creates a new note based on the provided request body
 * and returns a ResponseEntity with the created note or a bad request
 * response.
 * 
 * @param note The `createNote` method in the code snippet is a POST
 * mapping that takes a request body in the form of a Map<String, Object>
 * named `note`. The method then attempts to create a new note using the
 * `notesService.createNote` method and returns a ResponseEntity containing
 * the created note if
 * @return The method is returning a `ResponseEntity` object with the
 * created note if it is present, or a bad request response if the note was
 * not created successfully.
 */
    @PostMapping
    public ResponseEntity<Notes> createNote(@RequestBody Map<String, Object> note) {
        Optional<Notes> createdNote = notesService.createNote(note);
        if (createdNote.isPresent()) {
            return ResponseEntity.ok(createdNote.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

/**
 * This function retrieves a note by its ID and returns it in a
 * ResponseEntity, handling the case where the note is not found.
 * 
 * @param id The `id` parameter in the `getNoteById` method is a path
 * variable of type `Long`. It is used to specify the unique identifier of
 * the note that the user wants to retrieve.
 * @return The `getNoteById` method returns a `ResponseEntity` containing a
 * `Notes` object with the specified ID if it exists. If the note is found,
 * it returns a response with status code 200 (OK) and the note object. If
 * the note is not found, it returns a response with status code 404 (Not
 * Found).
 */
    @GetMapping("/{id}")
    public ResponseEntity<Notes> getNoteById(@PathVariable Long id) {
        Optional<Notes> note = notesService.getNoteById(id);
        if (note.isPresent()) {
            return ResponseEntity.ok(note.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

/**
 * This Java function retrieves all notes and returns them in a
 * ResponseEntity, handling the case where no notes are found.
 * 
 * @return A `ResponseEntity` object containing a list of `Notes` is being
 * returned. If the list of notes is not empty, a response with status code
 * 200 (OK) and the list of notes is returned. If the list of notes is
 * empty, a response with status code 404 (Not Found) is returned.
 */
    @GetMapping
    public ResponseEntity<List<Notes>> getAllNotes() {
        List<Notes> notes = notesService.getAllNotes();
        if (!notes.isEmpty()) {
            return ResponseEntity.ok(notes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

 /**
  * This Java function updates a note by its ID using a map of updates and
  * returns a ResponseEntity with the updated note or a 404 status if the
  * note is not found.
  * 
  * @param id The `id` parameter in the `updateNote` method is a `Long`
  * type variable representing the unique identifier of the note that needs
  * to be updated. This `id` is extracted from the path variable in the URL
  * mapping of the `@PutMapping("/{id}")` annotation. It is
  * @param updates The `updates` parameter in the `updateNote` method is a
  * `Map<String, Object>` that contains the fields and values to be updated
  * for a specific note. The keys in the map represent the fields of the
  * note that need to be updated, and the corresponding values are the new
  * values for
  * @return The `updateNote` method returns a `ResponseEntity` containing
  * either the updated note with a status code of 200 if the note was
  * successfully updated, or a 404 status code if the note was not found.
  */
    @PutMapping("/{id}")
    public ResponseEntity<Notes> updateNote(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Optional<Notes> updatedNote = notesService.updateNoteById(id, updates);

        if (updatedNote.isPresent()) {
            return ResponseEntity.ok(updatedNote.get()); // 200 with updated note
        } else {
            return ResponseEntity.notFound().build();    // 404 if not found
        }
    }

/**
 * This Java function deletes a note by its ID and returns a response based
 * on whether the deletion was successful or not.
 * 
 * @param id The `id` parameter in the `deleteNoteById` method is a `Long`
 * type variable representing the unique identifier of the note that needs
 * to be deleted. This `id` is extracted from the path variable in the URL
 * when a DELETE request is made to the endpoint mapped to this method.
 * @return The method `deleteNoteById` is returning a
 * `ResponseEntity<Void>`. If the note with the specified `id` is
 * successfully deleted, a response with status code 204 (no content) is
 * returned using `ResponseEntity.noContent().build()`. If the note is not
 * found or unable to be deleted, a response with status code 404 (not
 * found) is returned using `Response
 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        boolean deleted = notesService.deleteNoteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
