package com.github.dnurtaza.markdown_notes.rendering;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MarkdownRendererTests {
    private final MarkdownRenderer renderer = new MarkdownRenderer(
            Parser.builder().build(),
            HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build(),
            TextContentRenderer.builder().build()
    );

    @Test
    void rendersHeading()
    {
        assertThat(renderer.render("# Hello")).contains("<h1>Hello</h1>");
    }

    @Test
    void escapesScriptTags()
    {
        assertThat(renderer.render("<script>alert(1);</script>"))
                .doesNotContain("<script>");
    }

    @Test
    void removesJavascriptFromLinks()
    {
        assertThat(renderer.render("[click](javascript:alert('xss'))"))
                .contains("<a")
                .doesNotContain("javascript:");
    }

    @Test
    void removesJavascriptFromImages() {
        String html = renderer.render("![img](javascript:alert('xss'))");

        assertThat(html).doesNotContain("javascript:");
    }

    @Test
    void keepsNormalLinks() {
        String html = renderer.render("[Google](https://google.com)");

        assertThat(html).contains("href=\"https://google.com\"");
    }
}
