package de.jupiterpi.wiesen.website.notifications.messages;

import jupiterpi.tools.files.Path;
import jupiterpi.tools.files.csv.CSVObjectsFile;

import java.util.List;

public class MessagesStorage {
    private final Path storageDir = Path.getRunningDirectory().subdir("storage");
    private final CSVObjectsFile<Message> messagesFile = new CSVObjectsFile<>(storageDir.copy().file("messages.csv"), Message.class);

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