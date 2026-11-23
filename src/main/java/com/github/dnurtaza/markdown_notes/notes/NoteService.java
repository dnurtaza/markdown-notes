package com.github.dnurtaza.markdown_notes.notes;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository repository;

    NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public Note create(String filename, String content) {
        if (filename == null || !filename.toLowerCase().endsWith(".md")) {
            throw new IllegalArgumentException("Only .md files are allowed");
        }
        if (content.isBlank()) {
            throw new IllegalArgumentException("File is empty");
        }
        return save(titleFromFilename(filename), content);
    }

    public Note createFromText(String title, String content) {
        return save(sanitizeTitle(title), content);
    }

    private Note save(String title, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Note content is empty");
        }

        return repository.save(new Note(title, content));
    }

    private static String titleFromFilename(String filename) {
        String name = filename.replace('\\', '/');
        name = name.substring(name.lastIndexOf('/') + 1);

        name = name.substring(0, name.length() - 3);

        return sanitizeTitle(filename);
    }

    private static String sanitizeTitle(String title) {
        if (title == null) {
            return "untitled";
        }

        String name = title.replaceAll("[^\\p{L}\\p{N} ._-]", "").strip();
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }

        return name.isEmpty() ? "untitled" : name;
    }

    public Note findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public List<Note> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
}
