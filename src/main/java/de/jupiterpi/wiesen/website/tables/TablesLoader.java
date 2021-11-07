package de.jupiterpi.wiesen.website.tables;

import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.WrongPathTypeException;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TablesLoader {
    public byte[] getDownloadZip(HttpServletResponse response, Path path) throws IOException, WrongPathTypeException {
        response.addHeader("Content-Disposition", "attachment; filename=\"" + path.getFileName() + ".zip\"");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        List<File> files = new ArrayList<>();
        files.add(path.file());

        for (File file : files) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String[][] getTable(Path file) {
        Table table = new Table(file);

        List<String[]> rowsList = table.get();
        String[][] rows = new String[rowsList.size()][];
        for (int i = 0; i < rowsList.size(); i++) {
            rows[i] = rowsList.get(i);
        }
        return rows;
    }
}