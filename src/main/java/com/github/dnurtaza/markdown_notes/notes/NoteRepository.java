package com.github.dnurtaza.markdown_notes.notes;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface NoteRepository extends JpaRepository<Note, Long>
{
    List<Note> findAllByOrderByCreatedAtDesc();
}
