# Markdown Note-taking App

A REST API that lets users upload Markdown notes, check their grammar, save them, and render them as HTML.

Solution for the [Markdown Note-taking App](https://roadmap.sh/projects/markdown-note-taking-app) project on roadmap.sh.

## Features

- Upload `.md` files or send Markdown text and store them as notes
- List all saved notes (newest first)
- Get the raw Markdown of a note
- Render a note as HTML (with XSS protection)
- Check the grammar of a note with LanguageTool

## Tech Stack

- Java 25
- Spring Boot 4.2 (Web MVC, Data JPA)
- H2 (file-based database)
- [commonmark-java](https://github.com/commonmark/commonmark-java) — Markdown parsing and rendering
- [LanguageTool](https://languagetool.org/) — grammar checking
- Spring Modulith — module structure verification
- JUnit 5, AssertJ, Mockito

## Getting Started

### Requirements

- JDK 25

Maven is not required: the project includes the Maven Wrapper.

### Run

```bash
git clone https://github.com/dnurtaza/markdown-notes.git
cd markdown-notes
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`.

The app starts on **http://localhost:2244**. Opening this address in a browser shows a simple upload form.

Notes are stored in `data/notes.mv.db` and persist between restarts.

### Run tests

```bash
./mvnw test
```

## API

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/notes` | Upload a Markdown file (`multipart/form-data`, field `file`) |
| `POST` | `/notes?title=...` | Save Markdown text from the request body (`text/markdown`) |
| `GET` | `/notes` | List all notes |
| `GET` | `/notes/{id}` | Get a note with its Markdown content |
| `GET` | `/notes/{id}/html` | Get a note rendered as HTML |
| `GET` | `/notes/{id}/grammar` | Check the grammar of a note |

### Upload a note

```bash
curl -i -F "file=@todo.md" http://localhost:2244/notes
```

```http
HTTP/1.1 201 Created
Location: /notes/1
Content-Type: application/json

{
  "id": 1,
  "title": "todo",
  "content": "# My tasks\n\nHe go to school.",
  "createdAt": "2026-11-23T11:45:00Z"
}
```

Only `.md` files are accepted. The title is taken from the file name.

### Save Markdown text

```bash
curl -i -X POST "http://localhost:2244/notes?title=shopping" \
     -H "Content-Type: text/markdown; charset=UTF-8" \
     --data-binary @todo.md
```

The request body is the Markdown text itself. The `title` parameter is optional (defaults to `untitled`). Use `--data-binary` rather than `-d`, otherwise curl strips line breaks.

### List notes

```bash
curl http://localhost:2244/notes
```

### Render as HTML

```bash
curl http://localhost:2244/notes/1/html
```

```html
<h1>My tasks</h1>
<p>He go to school.</p>
```

Raw HTML inside Markdown is escaped and unsafe links (such as `javascript:`) are removed.

### Check grammar

```bash
curl http://localhost:2244/notes/1/grammar
```

```json
[
  {
    "message": "The pronoun 'He' is usually used with a third-person or a past tense verb.",
    "fragment": "go",
    "suggestions": ["goes", "went"]
  }
]
```

Markdown syntax is stripped before checking, so headings, links and formatting don't produce false errors. An empty array means no issues were found. The first check after startup takes a few seconds while LanguageTool loads its rules.

### Errors

Errors are returned in the [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) `ProblemDetail` format:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Note with id 999 is not found",
  "instance": "/notes/999"
}
```

| Status | When |
|---|---|
| `400` | The file is not `.md`, the note is empty, or the `file` field is missing |
| `404` | The note does not exist |
| `413` | The file is larger than 1 MB |
| `415` | The request is neither `multipart/form-data` nor `text/markdown` |

## Project Structure

The code is split into modules by feature:

```
src/main/java/com/github/dnurtaza/markdown_notes/
├── notes/       — notes: entity, repository, service, REST controller
├── rendering/   — Markdown → HTML and Markdown → plain text
├── grammar/     — grammar checking with LanguageTool
└── web/         — global exception handling
```

Each module exposes a small public API (`NoteService`, `MarkdownRenderer`, `GrammarService`); library configuration lives in `internal` packages. Module boundaries are verified by a Spring Modulith test.

## License

[MIT](LICENSE)
