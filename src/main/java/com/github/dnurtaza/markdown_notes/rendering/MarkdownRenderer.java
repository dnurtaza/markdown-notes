package com.github.dnurtaza.markdown_notes.rendering;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownRenderer {
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownRenderer(Parser parser, HtmlRenderer renderer)
    {
        this.parser = parser;
        this.renderer = renderer;
    }

    public String render(String markdown)
    {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
