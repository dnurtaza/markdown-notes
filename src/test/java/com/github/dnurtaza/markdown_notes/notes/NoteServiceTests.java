package com.github.dnurtaza.markdown_notes.notes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NoteServiceTests {

    private NoteRepository repository;
    private NoteService service;

    @BeforeEach
    void setUp()
    {
        repository = mock(NoteRepository.class);
        when(repository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));
        service = new NoteService(repository);
    }

    @Test
    void createsNoteFromMarkdownFile()
    {
        Note note = service.create("todo.md", "# Tasks");

        assertThat(note.getTitle()).isEqualTo("todo");
        assertThat(note.getContent()).isEqualTo("# Tasks");
        verify(repository).save(any(Note.class));
    }

    @Test
    void rejectsNonMarkdownFile()
    {
        assertThatThrownBy(() -> service.create("photo.png", "content"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void rejectsMissingFilename()
    {
        assertThatThrownBy(() -> service.create(null, "content"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsEmptyFile()
    {
        assertThatThrownBy(() -> service.create("todo.md", "   "))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void stripsPathFromFilename()
    {
        assertThat(service.create("C:\\docs\\todo.md", "text").getTitle()).isEqualTo("todo");
        assertThat(service.create("../../etc/todo.md", "text").getTitle()).isEqualTo("todo");
    }

    @Test
    void sanitizesDangerousFilename()
    {
        Note note = service.create("<script>alert(1)</script>.md", "text");

        assertThat(note.getTitle())
                .doesNotContain("<")
                .doesNotContain(">");
    }

    @Test
    void limitsTitleLengthWithoutLosingCharacters()
    {
        Note note = service.create("a".repeat(150) + ".md", "text");

        assertThat(note.getTitle()).isEqualTo("a".repeat(100));
    }

    @Test
    void createsNoteFromText()
    {
        Note note = service.createFromText("shopping", "- milk");

        assertThat(note.getTitle()).isEqualTo("shopping");
        assertThat(note.getContent()).isEqualTo("- milk");
    }

    @Test
    void rejectsEmptyText()
    {
        assertThatThrownBy(() -> service.createFromText("title", ""))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void sanitizesTitleFromText()
    {
        Note note = service.createFromText("<script>alert(1)</script>", "text");

        assertThat(note.getTitle()).isEqualTo("scriptalert1script");
    }

    @Test
    void usesUntitledWhenTitleMissing()
    {
        assertThat(service.createFromText(null, "text").getTitle()).isEqualTo("untitled");
        assertThat(service.createFromText("<>", "text").getTitle()).isEqualTo("untitled");
    }

    @Test
    void keepsCyrillicTitle()
    {
        assertThat(service.createFromText("мои заметки", "text").getTitle()).isEqualTo("мои заметки");
    }

    @Test
    void throwsWhenNoteNotFound()
    {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("999");
    }
}
