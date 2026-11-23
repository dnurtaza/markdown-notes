package com.github.dnurtaza.markdown_notes.grammar;

import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Service
public class GrammarService {
    private static final int MAX_SUGGESTIONS = 5;
    private final Language language;

    GrammarService(Language language) {
        this.language = language;
    }

    public List<GrammarIssue> check(String text) {
        JLanguageTool tool = new JLanguageTool(language);
        try {
            return tool.check(text).stream()
                    .map(match -> toIssue(match, text))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Grammar check failed", e);
        }
    }

    private static GrammarIssue toIssue(RuleMatch match, String text) {
        List<String> suggestions = match.getSuggestedReplacements().stream()
                .limit(MAX_SUGGESTIONS)
                .toList();

        return new GrammarIssue(
                match.getMessage(),
                text.substring(match.getFromPos(), match.getToPos()),
                suggestions
        );
    }
}
