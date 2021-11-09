package de.jupiterpi.wiesen.website.tables;

import de.jupiterpi.wiesen.website.files.Files;
import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.WrongPathTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/tables")
public class TablesController {
    private TablesLoader loader = new TablesLoader();

    private final Path tablesDir = Files.tablesDir.copy();

    @GetMapping("/{filename}/download")
    public byte[] getTableDownload(@PathVariable String filename, HttpServletResponse response) {
        try {
            Path file = tablesDir.copy().file(filename);
            return loader.getDownloadZip(response, file);
        } catch (IOException | WrongPathTypeException ignored) {}
        return null;
    }

    @GetMapping("/{filename}")
    public String[][] getTable(@PathVariable String filename) {
        Path file = tablesDir.copy().file(filename);
        return loader.getTable(file);
    }
}