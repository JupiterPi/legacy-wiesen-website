package de.jupiterpi.wiesen.website.files;

import jupiterpi.tools.files.Path;

import java.io.IOException;

public class Storage {
    public static Path storageDir = Path.getRunningDirectory().subdir("storage");
    public static Path messagesFile = storageDir.copy().subdir("messages.csv");
    public static Path visitsFile = storageDir.copy().subdir("visits.csv");
    public static Path formattedVisitsFile = storageDir.copy().subdir("formatted-visits.csv");

    static {
        try {
            storageDir.file().mkdir();
            messagesFile.file().createNewFile();
            visitsFile.file().createNewFile();
            formattedVisitsFile.file().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
