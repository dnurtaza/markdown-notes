package com.github.dnurtaza.markdown_notes.notes;

import java.time.Instant;

public record NoteResponse(Long id, String title, String content, Instant createdAt) {

    static NoteResponse from(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt()
        );
    }
}
