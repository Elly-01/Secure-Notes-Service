package com.agora.Secure_Notes_Service.service;

import com.agora.Secure_Notes_Service.model.Notes;
import com.agora.Secure_Notes_Service.repository.NotesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // rollback after each test
class NotesServiceTest {

    @Autowired
    private NotesService notesService;

    @Autowired
    private NotesRepository notesRepository;

    // Dynamically define required properties for Spring
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("NOTES_API_TOKEN", () -> "test-token");
        registry.add("notes.secret", () -> "1234567812345678");
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    void testCreateNoteValid() {
        Map<String, Object> input = Map.of(
                "title", "testTitle",
                "content", "testContent"
        );

        Optional<Notes> created = notesService.createNote(input);
        assertTrue(created.isPresent());

        Notes note = created.get();
        assertEquals("testTitle", note.getTitle());
        assertEquals("testContent", note.getContent());

        // persisted in DB
        Optional<Notes> fromDb = notesRepository.findById(note.getId());
        assertTrue(fromDb.isPresent());
        assertEquals("testTitle", fromDb.get().getTitle());
    }

    @Test
    void testCreateNoteMissingTitle() {
        Map<String, Object> input = Map.of("content", "testContent");
        Optional<Notes> created = notesService.createNote(input);

        assertTrue(created.isEmpty());
        assertEquals(0, notesRepository.count());
    }

    @Test
    void testCreateNoteEmptyTitle() {
        Map<String, Object> input = Map.of(
                "title", "   ",
                "content", "Some content"
        );

        Optional<Notes> created = notesService.createNote(input);
        assertTrue(created.isEmpty());
        assertEquals(0, notesRepository.count());
    }

    @Test
    void testCreateNoteTitleTooLong() {
        String longTitle = "A".repeat(300); // exceeds 255
        Map<String, Object> input = Map.of(
                "title", longTitle,
                "content", "Some content"
        );

        Optional<Notes> created = notesService.createNote(input);
        assertTrue(created.isEmpty());
        assertEquals(0, notesRepository.count());
    }

    @Test
    void testCreateNoteMissingContent() {
        Map<String, Object> input = Map.of("title", "Some title");
        Optional<Notes> created = notesService.createNote(input);

        assertTrue(created.isEmpty());
        assertEquals(0, notesRepository.count());
    }

    @Test
    void testCreateNoteEmptyContent() {
        Map<String, Object> input = Map.of(
                "title", "Some title",
                "content", "   "
        );

        Optional<Notes> created = notesService.createNote(input);
        assertTrue(created.isEmpty());
        assertEquals(0, notesRepository.count());
    }

    @Test
    void testCreateNoteContentTooLong() {
        String longContent = "C".repeat(300); // exceeds 255
        Map<String, Object> input = Map.of(
                "title", "Some title",
                "content", longContent
        );

        Optional<Notes> created = notesService.createNote(input);
        assertTrue(created.isEmpty());
        assertEquals(0, notesRepository.count());
    }
}
