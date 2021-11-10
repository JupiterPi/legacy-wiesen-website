package de.jupiterpi.wiesen.website.pages.rendering;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;

public class PageRenderer {
    private Parser parser;
    private HtmlRenderer renderer;

    public PageRenderer() {
        parser = Parser.builder().build();
        renderer = HtmlRenderer.builder()
                .nodeRendererFactory(new HtmlNodeRendererFactory() {
                    public NodeRenderer create(HtmlNodeRendererContext context) {
                        return new ImageNodeRenderer(context);
                    }
                })
                .nodeRendererFactory(new HtmlNodeRendererFactory() {
                    public NodeRenderer create(HtmlNodeRendererContext context) {
                        return new ImageGalleryRenderer(context);
                    }
                })
                .build();
    }

    public String render(String source) {
        Node document = parser.parse(source);
        String html = renderer.render(document);
        return html;
    }
}
