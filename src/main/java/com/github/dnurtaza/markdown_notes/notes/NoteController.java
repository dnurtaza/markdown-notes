package com.github.dnurtaza.markdown_notes.notes;

import com.github.dnurtaza.markdown_notes.grammar.GrammarIssue;
import com.github.dnurtaza.markdown_notes.grammar.GrammarService;
import com.github.dnurtaza.markdown_notes.rendering.MarkdownRenderer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/notes")
class NoteController {
    private final NoteService service;
    private final MarkdownRenderer markdownRenderer;
    private final GrammarService grammarService;

    NoteController(NoteService service, MarkdownRenderer markdownRenderer, GrammarService grammarService) {
        this.service = service;
        this.markdownRenderer = markdownRenderer;
        this.grammarService = grammarService;
    }

    @PostMapping(consumes = MediaType.TEXT_MARKDOWN_VALUE)
    ResponseEntity<NoteResponse> createFromText(
            @RequestParam(defaultValue = "untitled") String title,
            @RequestBody String content
    ) {
        return created(service.createFromText(title, content));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<NoteResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        Note note = service.create(file.getOriginalFilename(), content);

        return created(note);
    }

    private static ResponseEntity<NoteResponse> created(Note note) {
        return ResponseEntity
                .created(URI.create("/notes/" + note.getId()))
                .body(NoteResponse.from(note));
    }

    @GetMapping
    List<NoteResponse> list() {
        return service.findAll().stream()
                .map(NoteResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    NoteResponse get(@PathVariable Long id) {
        return NoteResponse.from(service.findById(id));
    }

    @GetMapping(value = "/{id}/html", produces = MediaType.TEXT_HTML_VALUE)
    String getHtml(@PathVariable Long id) {
        Note note = service.findById(id);
        return markdownRenderer.render(note.getContent());
    }

    @GetMapping("/{id}/grammar")
    List<GrammarIssue> checkGrammar(@PathVariable Long id) {
        Note note = service.findById(id);
        String plainText = markdownRenderer.renderPlainText(note.getContent());
        return grammarService.check(plainText);
    }
}
