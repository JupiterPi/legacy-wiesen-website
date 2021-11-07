package de.jupiterpi.wiesen.website.analytics;

import de.jupiterpi.wiesen.website.App;
import de.jupiterpi.wiesen.website.notifications.NotificationsBot;
import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.TextFile;
import jupiterpi.tools.files.csv.CSVCastable;
import jupiterpi.tools.files.csv.CSVObjectsFile;
import jupiterpi.tools.util.AppendingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Visit implements CSVCastable {
    private static final Path visitsFile = Path.getRunningDirectory().subdir("storage").file("visits.csv");
    private static final Path formattedVisitsFile = Path.getRunningDirectory().subdir("storage").file("formatted-visits.csv");
    private static NotificationsBot bot = App.bot;

    private String profilingId;
    private Date time;
    private String page;
    private boolean isMobile;

    public Visit(String profilingId, Date time, String page, boolean isMobile) {
        this.profilingId = profilingId;
        this.time = time;
        this.page = page;
        this.isMobile = isMobile;
    }

    public static Visit createVisit(String profilingId, String page, boolean isMobile, boolean isNew) throws TextFile.DoesNotExistException {
        Visit visit = new Visit(profilingId, new Date(), page, isMobile);
        CSVObjectsFile<Visit> file = new CSVObjectsFile<>(new TextFile(visitsFile, true), Visit.class);
        List<Visit> visits = file.getObjects();
        visits.add(visit);
        file.writeObjects(visits);
        writeFormattedVisits(visits);

        if (bot != null) bot.notifyVisit(visit, isMobile, isNew);
        return visit;
    }

    private static void writeFormattedVisits(List<Visit> visits) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        List<String> lines = new ArrayList<>();
        for (Visit visit : visits) {
            AppendingList fields = new AppendingList();
            fields.add(visit.getProfilingId());
            fields.add(dateFormat.format(visit.getTime()));
            fields.add(visit.getPage());
            fields.add(visit.isMobile ? "MOBILE" : "-");
            lines.add(fields.render(";"));
        }
        TextFile formattedFile = new TextFile(formattedVisitsFile);
        formattedFile.setFile(lines);
        formattedFile.saveFile();
    }

    /* getters */

    public String getProfilingId() {
        return profilingId;
    }
    public Date getTime() {
        return time;
    }
    public String getPage() {
        return page;
    }

    /* csv */

    public Visit(String[] f) {
        profilingId = f[0];
        time = new Date(Long.parseLong(f[1]));
        page = f[2];
        isMobile = Boolean.parseBoolean(f[3]);
    }

    @Override
    public String[] toCSV() {
        return new String[]{
                profilingId,
                Long.toString(time.getTime()),
                page,
                Boolean.toString(isMobile)
        };
    }
}