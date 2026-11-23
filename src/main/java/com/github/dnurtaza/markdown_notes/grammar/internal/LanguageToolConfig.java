package com.github.dnurtaza.markdown_notes.grammar.intrenal;

import org.languagetool.Language;
import org.languagetool.Languages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LanguageToolConfig
{

    @Bean
    Language grammarLanguage()
    {
        return Languages.getLanguageForShortCode("en-US");
    }
}