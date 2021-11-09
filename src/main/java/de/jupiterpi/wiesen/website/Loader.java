package de.jupiterpi.wiesen.website;

import de.jupiterpi.wiesen.website.files.Files;
import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.TextFile;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Loader {
    /* pages */

    private final Path pagesDirectory = Files.pagesDir.copy();

    public String loadPage(String category, String name) {
        return constructPage(new TextFile(pagesDirectory.copy().subdir(category).file(name+".html")).getFileForOutput());
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