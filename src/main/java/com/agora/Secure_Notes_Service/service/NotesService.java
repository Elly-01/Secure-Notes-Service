package com.agora.Secure_Notes_Service.service;

import com.agora.Secure_Notes_Service.model.Notes;
import com.agora.Secure_Notes_Service.repository.NotesRepository;
import com.agora.Secure_Notes_Service.util.EncryptionUtil;
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
        //validation
        Object rawTitle = note.get("title");
        Optional<String> title = Optional.ofNullable(rawTitle)
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .map(String::trim)
            .filter(s -> !s.isBlank() && s.length() <= 255);
        if (title.isEmpty()) {
            return Optional.empty();
        }

        Object rawContent = note.get("content");
        Optional<String> content = Optional.ofNullable(rawContent)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(s -> !s.isBlank() && s.length() <= 255);
        if (content.isEmpty()) {
            return Optional.empty();
        }

        String encryptedContent = EncryptionUtil.encrypt(content.get());
        Notes newNote = new Notes(title.get(), encryptedContent);
        noteRepository.save(newNote);

        newNote.setContent(EncryptionUtil.decrypt(newNote.getContent()));
        return Optional.of(newNote);
    }

    public List<Notes> getAllNotes() {
        List<Notes> notes = noteRepository.findAll();
        notes.forEach(n ->
                n.setContent(EncryptionUtil.decrypt(n.getContent()))
        );
        return notes;
    }

    public Optional<Notes> getNoteById(Long id) {
        Optional<Notes> note = noteRepository.findById(id);
        if (note.isPresent()) {
            note.get().setContent(EncryptionUtil.decrypt(note.get().getContent()));
        }
        return note;
    }

    public Optional<Notes> updateNoteById(Long id, Map<String, Object> updates) {

        Optional<Notes> existing = noteRepository.findById(id);
        if (existing.isEmpty()) {return Optional.empty();}

        //validation
        Object rawTitle = updates.get("title");
        Optional.ofNullable(rawTitle)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(s -> !s.isBlank() && s.length() <= 255)
                .ifPresent(title -> existing.get().setTitle(title));

        Object rawContent = updates.get("content");
        Optional.ofNullable(rawContent)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(s -> !s.isBlank() && s.length() <= 255)
                .ifPresent(content -> existing.get().setContent(EncryptionUtil.encrypt(content)));


        noteRepository.save(existing.get());
        existing.get().setContent(EncryptionUtil.decrypt(existing.get().getContent()));
        return existing;
    }


    public boolean deleteNoteById(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
