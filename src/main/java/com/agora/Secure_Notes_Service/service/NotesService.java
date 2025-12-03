package com.agora.Secure_Notes_Service.service;

import com.agora.Secure_Notes_Service.model.Notes;
import com.agora.Secure_Notes_Service.repository.NotesRepository;
import org.springframework.stereotype.Service;

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
        if (note.get("title") != null) {
            String newTitle = note.get("title").toString();
            if (!Objects.equals(newTitle, "")) {
                title = newTitle;
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
        if (note.get("content") != null) {
            String newContent = note.get("content").toString();
            if (!Objects.equals(newContent, "")) {
                content = newContent;
            } else {
                return Optional.empty();
            }
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

    public Notes updateNote(Notes note) {
        return noteRepository.save(note);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
