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

    @PostMapping
    public ResponseEntity<Notes> createNote(@RequestBody Map<String, Object> note) {
        Optional<Notes> createdNote = notesService.createNote(note);
        if (createdNote.isPresent()) {
            return ResponseEntity.ok(createdNote.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notes> getNoteById(@PathVariable Long id) {
        Optional<Notes> note = notesService.getNoteById(id);
        if (note.isPresent()) {
            return ResponseEntity.ok(note.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Notes>> getAllNotes() {
        List<Notes> notes = notesService.getAllNotes();
        if (!notes.isEmpty()) {
            return ResponseEntity.ok(notes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
