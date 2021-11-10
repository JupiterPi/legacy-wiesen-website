package de.jupiterpi.wiesen.website.pages;

import de.jupiterpi.wiesen.website.files.Files;
import de.jupiterpi.wiesen.website.pages.rendering.PageRenderer;
import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.TextFile;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Loader {
    private PageRenderer pageRenderer = new PageRenderer();

    /* pages */

    private final Path pagesDirectory = Files.pagesDir.copy();

    public String loadPage(String category, String name) {
        String source = new TextFile(pagesDirectory.copy().subdir(category).file(name+".md")).getFileForOutput();
        String content = pageRenderer.render(source);
        return constructPage(content);
    }

    private String constructPage(String content) {
        Path frameDirectory = Files.resourcesDir.copy();
        String frame = new TextFile(frameDirectory.file("frame.html")).getFileForOutput();
        return frame.replace("<!--{content}-->", content);
    }

    public String loadInternalPage(String fileName) {
        TextFile pageFile = new TextFile(Files.resourcesDir.copy().file(fileName));
        return pageFile.getFileForOutput();
    }

    /* resources */

    private final Path resourcesDirectory = Files.resourcesDir.copy();

    public String getResource(String fileName) {
        return new TextFile(resourcesDirectory.file(fileName)).getFileForOutput();
    }
}