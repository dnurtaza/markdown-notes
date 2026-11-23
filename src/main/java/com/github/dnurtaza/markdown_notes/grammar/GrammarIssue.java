package com.github.dnurtaza.markdown_notes.grammar;

import java.util.List;

public record GrammarIssue(
        String message,
        String fragment,
        List<String> suggestions
) {
}
