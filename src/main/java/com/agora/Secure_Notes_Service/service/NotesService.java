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

        String encryptedContent = EncryptionUtil.encrypt(content);
        Notes newNote = new Notes(title, encryptedContent);
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

        if (updates.get("title") != null && updates.get("title") instanceof String newTitle) {
            existing.get().setTitle(newTitle);
        }
        if (updates.get("content") != null && updates.get("content") instanceof String newContent) {
            existing.get().setContent(EncryptionUtil.encrypt(newContent));
        }

        Notes saved = noteRepository.save(existing.get());
        if (existing.isPresent()) {
            existing.get().setContent(EncryptionUtil.decrypt(existing.get().getContent()));
        }
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
