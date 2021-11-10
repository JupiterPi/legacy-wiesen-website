package de.jupiterpi.wiesen.website.pages.rendering;

import org.commonmark.node.BlockQuote;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.*;

public class ImageNodeRenderer implements NodeRenderer {
    private final HtmlWriter html;

    public ImageNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        HashSet<Class<? extends Node>> set = new HashSet<>();
        set.add(Image.class);
        return set;
    }

    @Override
    public void render(Node node) {
        Image image = (Image) node;
        html.line();
        Map<String, String> divAttributes = new HashMap<>();
        divAttributes.put("class", "img-gallery-item");
        divAttributes.put("data-img", image.getDestination());
        divAttributes.put("data-caption", image.getTitle());
        html.tag("div", divAttributes);
        html.tag("/div");
        html.line();
    }
}
