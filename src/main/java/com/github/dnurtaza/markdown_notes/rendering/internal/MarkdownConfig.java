package com.github.dnurtaza.markdown_notes.rendering.internal;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarkdownConfig {
    @Bean
    Parser markdownParser()
    {
        return Parser.builder().build();
    }

    @Bean
    HtmlRenderer htmlRenderer()
    {
        return HtmlRenderer.builder()
                .escapeHtml(true)
                .sanitizeUrls(true)
                .build();
    }
}
