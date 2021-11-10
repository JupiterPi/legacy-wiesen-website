package de.jupiterpi.wiesen.website.pages.rendering;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.*;

public class ImageGalleryRenderer implements NodeRenderer {
    private final HtmlNodeRendererContext context;
    private final HtmlWriter html;

    public ImageGalleryRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        HashSet<Class<? extends Node>> set = new HashSet<>();
        set.add(BlockQuote.class);
        return set;
    }

    private class Receptor {
        public boolean receipted = false;
        public boolean isGallery = false;
    }

    @Override
    public void render(Node node) {
        BlockQuote quote = (BlockQuote) node;

        final Receptor receptor = new Receptor();
        quote.getFirstChild().getFirstChild().accept(new AbstractVisitor() {
            @Override
            public void visit(Text text) {
                boolean isGallery = text.getLiteral().equals("gallery");
                receptor.receipted = true;
                receptor.isGallery = isGallery;
            }
        });

        if (receptor.isGallery) {

            html.line();
            Map<String, String> divAttributes = new HashMap<>();
            divAttributes.put("class", "img-gallery");
            html.tag("div", divAttributes);
            //html.raw("visiting children...");

            visitChildren(quote);

            //html.raw("visited children.");
            html.tag("/div");
            html.line();

        } else {

            html.line();

            Map<String, String> divAttributes = new HashMap<>();
            divAttributes.put("class", "quote");
            html.tag("div", divAttributes);

            Node currentNode = quote.getFirstChild();
            while (currentNode != null) {
                Node next = currentNode.getNext();
                //html.raw("((");
                context.render(currentNode);
                //html.raw("))");
                currentNode = next;
            }

            html.tag("/div");

        }
    }

    protected void visitChildren(Node parent) {

        Node child = parent.getFirstChild();

        Node node = child.getFirstChild().getNext();
        while (node != null) {
            Node next = node.getNext();
            //html.raw("((");
            context.render(node);
            //html.raw("))");
            node = next;
        }

    }
}
