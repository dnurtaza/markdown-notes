package com.github.dnurtaza.markdown_notes.grammar;

import org.junit.jupiter.api.Test;
import org.languagetool.Languages;

import static org.assertj.core.api.Assertions.assertThat;

class GrammarServiceTests {

    private final GrammarService service = new GrammarService(Languages.getLanguageForShortCode("en-US"));

    @Test
    void findsAgreementError()
    {
        assertThat(service.check("He go to school."))
                .isNotEmpty();
    }

    @Test
    void acceptsCorrectText()
    {
        assertThat(service.check("He goes to school."))
                .isEmpty();
    }

    @Test
    void returnsFragmentAndSuggestions()
    {
        GrammarIssue issue = service.check("He go to school.").getFirst();

        assertThat(issue.fragment()).isNotBlank();
        assertThat(issue.suggestions()).isNotEmpty();
    }
}
