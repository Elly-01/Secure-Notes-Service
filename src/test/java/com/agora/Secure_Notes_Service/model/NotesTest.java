package com.agora.Secure_Notes_Service.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotesTest {
    @Test
    void constructorShouldInitializeFields() {
        Notes note = new Notes("Title", "Content");

        assertNull(note.getId());
        assertEquals("Title", note.getTitle());
        assertEquals("Content", note.getContent());
        assertNotNull(note.getCreatedAt());
        assertNotNull(note.getUpdatedAt());
    }

    @Test
    void setTitleShouldUpdateTitleAndTimestamp() throws InterruptedException {
        Notes note = new Notes("Old Title", "Content");

        LocalDateTime beforeUpdate = note.getUpdatedAt();
        Thread.sleep(5); // ensure timestamp difference

        note.setTitle("New Title");

        assertEquals("New Title", note.getTitle());
        assertTrue(note.getUpdatedAt().isAfter(beforeUpdate));
    }

    @Test
    void setContentShouldUpdateContentAndTimestamp() throws InterruptedException {
        Notes note = new Notes("Title", "Old Content");

        LocalDateTime beforeUpdate = note.getUpdatedAt();
        Thread.sleep(5); // ensure timestamp difference

        note.setContent("New Content");

        assertEquals("New Content", note.getContent());
        assertTrue(note.getUpdatedAt().isAfter(beforeUpdate));
    }
}
