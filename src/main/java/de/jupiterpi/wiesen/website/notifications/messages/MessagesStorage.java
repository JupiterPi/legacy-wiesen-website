package de.jupiterpi.wiesen.website.notifications.messages;

import de.jupiterpi.wiesen.website.files.Storage;
import jupiterpi.tools.files.csv.CSVObjectsFile;

import java.util.List;

public class MessagesStorage {
    private final CSVObjectsFile<Message> messagesFile = new CSVObjectsFile<>(Storage.messagesFile, Message.class);

    public List<Message> getMessages() {
        return messagesFile.getObjects();
    }

    public Message getMessage(String id) {
        List<Message> messages = getMessages();
        for (Message message : messages) {
            if (message.getId().equals(id)) return message;
        }
        return null;
    }

    public void addMessage(Message message) {
        List<Message> messages = getMessages();
        messages.add(message);
        messagesFile.writeObjects(messages);
    }
}