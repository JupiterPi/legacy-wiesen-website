package de.jupiterpi.wiesen.website.tables;

import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.csv.CSVFile;

import java.util.List;

public class Table {
    private List<String[]> table;

    public Table(Path path) {
        CSVFile file = new CSVFile(path);
        table = file.get();
    }

    public List<String[]> get() {
        for (String[] row : table) {
            for (int i = 0; i < row.length; i++) {
                String field = row[i];
                row[i] = field
                        .replace("#a", "ä")
                        .replace("#o", "ö")
                        .replace("#u", "ü")
                        .replace("#s", "ß")
                        .replace("#c", "č");
            }
        }
        return table;
    }
}