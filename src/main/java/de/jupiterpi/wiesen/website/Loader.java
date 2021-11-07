package de.jupiterpi.wiesen.website;

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
    private final Path rootDirectory = Path.getRunningDirectory();

    /* pages */

    private final Path pagesDirectory = rootDirectory.copy().subdir("pages");

    public String loadPage(String category, String name) {
        return constructPage(new TextFile(pagesDirectory.copy().subdir(category).file(name+".html")).getFileForOutput());
    }

    private String constructPage(String content) {
        /*String content = "";
        Path frameDirectory = pagesDirectory.copy();//.subdir("frame");

        // frame top
        content += new TextFile(frameDirectory.copy().file("frame-top.html")).getFileForOutput() + "\n";

        // content
        content += contentString;

        // frame bottom
        content += "\n" + new TextFile(frameDirectory.copy().file("frame-bottom.html")).getFileForOutput();

        return content;*/

        Path frameDirectory = pagesDirectory.copy();
        String frame = new TextFile(frameDirectory.file("frame.html")).getFileForOutput();
        return frame.replace("<!--{content}-->", content);
    }

    public String loadInternalPage(String fileName) {
        TextFile pageFile = new TextFile(pagesDirectory.copy().subdir("internal").file(fileName));
        return pageFile.getFileForOutput();
    }

    /* resources */

    private final Path resourcesDirectory = rootDirectory.copy().subdir("resources");

    public String getTextResource(String fileName) {
        return new TextFile(resourcesDirectory.copy().subdir("text").file(fileName)).getFileForOutput();
    }

    public ResponseEntity<byte[]> getPictureResource(String dir, String fileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            InputStream in = new FileInputStream(resourcesDirectory.copy().subdir(dir).file(fileName).file());
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}