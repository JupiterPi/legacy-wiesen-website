package de.jupiterpi.wiesen.website.files;

import jupiterpi.tools.files.Path;

public class Files {
    public static Path contentsDir = Path.getRunningDirectory().subdir("contents");

    public static Path headerPicsDir = contentsDir.copy().subdir("header-pic");
    public static Path pagesDir = contentsDir.copy().subdir("pages");
    public static Path picsDir = contentsDir.copy().subdir("pic");
    public static Path tablesDir = contentsDir.copy().subdir("tables");

    public static Path resourcesDir = Path.getRunningDirectory().subdir("resources");
}
