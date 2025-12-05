package com.agora.Secure_Notes_Service.service;

import com.agora.Secure_Notes_Service.model.Notes;
import com.agora.Secure_Notes_Service.repository.NotesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotesService {
    private final NotesRepository noteRepository;

    public NotesService(NotesRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Optional<Notes> createNote(Map<String, Object> note) {
        String title;
        String content;

        //validation
        if (note.get("title") != null && note.get("title") instanceof String newTitle) {
            title = newTitle;
        } else {
            return Optional.empty();
        }
        if (note.get("content") != null && note.get("content") instanceof String newContent) {
            content = newContent;
        } else {
            return Optional.empty();
        }

        Notes newNote = new Notes(title, content);
        noteRepository.save(newNote);
        return Optional.of(newNote);
    }

    public List<Notes> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Notes> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Optional<Notes> updateNoteById(Long id, Map<String, Object> updates) {
        String title;
        String content;

        Optional<Notes> existing = noteRepository.findById(id);
        if (existing.isEmpty()) {return Optional.empty();}

        if (updates.get("title") != null && updates.get("title") instanceof String newTitle) {
            title = newTitle;
            existing.get().setTitle(title);
        }
        if (updates.get("content") != null && updates.get("content") instanceof String newContent) {
            content = newContent;
            existing.get().setContent(content);
        }

        Notes saved = noteRepository.save(existing.get());
        return Optional.of(saved);
    }


    public boolean deleteNoteById(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
