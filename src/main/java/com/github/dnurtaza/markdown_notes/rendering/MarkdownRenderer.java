package com.github.dnurtaza.markdown_notes.rendering;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownRenderer {
    private final Parser parser;
    private final HtmlRenderer renderer;
    private final TextContentRenderer textRenderer;

    public MarkdownRenderer(Parser parser, HtmlRenderer renderer, TextContentRenderer textRenderer)
    {
        this.parser = parser;
        this.renderer = renderer;
        this.textRenderer = textRenderer;
    }

    public String render(String markdown)
    {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    public String renderPlainText(String markdown)
    {
        return textRenderer.render(parser.parse(markdown));
    }
}
