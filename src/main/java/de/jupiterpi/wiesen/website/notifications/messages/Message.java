package de.jupiterpi.wiesen.website.notifications.messages;

import jupiterpi.tools.files.csv.CSVCastable;

import java.util.Date;
import java.util.UUID;

public class Message implements CSVCastable {
    private static final String NEWLINE_CHARACTER = "%n";

    private String id;
    private Date sent;
    private String email;
    private String content;

    public Message(String id, Date sent, String email, String content) {
        this.id = id;
        this.sent = sent;
        this.email = email;
        setContent(content);
    }

    public Message(MessageDTO dto) {
        id = UUID.randomUUID().toString();
        sent = new Date();
        email = dto.getEmail();
        setContent(dto.getMessage());
    }

    private void setContent(String content) {
        this.content = "";
        for (String part : content.split("\n")) {
            this.content += part + NEWLINE_CHARACTER;
        }
        if (this.content.length() >= NEWLINE_CHARACTER.length()) this.content = this.content.substring(0, this.content.length()-NEWLINE_CHARACTER.length());
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getContentForOutput() {
        String returning = "";
        for (String part : content.split(NEWLINE_CHARACTER)) {
            returning += part + "\n";
        }
        return returning.substring(0, returning.length()-"\n".length());
    }

    /* csv */

    public Message(String[] fields) {
        id = fields[0];
        sent = new Date(Long.parseLong(fields[1]));
        email = fields[2];
        content = fields[3];
    }

    @Override
    public String[] toCSV() {
        return new String[]{
                id,
                Long.toString(sent.getTime()),
                email,
                content
        };
    }
}