package com.github.dnurtaza.markdown_notes.notes;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(Long id) {
        super("Note with id " + id + " is not found");
    }
}
