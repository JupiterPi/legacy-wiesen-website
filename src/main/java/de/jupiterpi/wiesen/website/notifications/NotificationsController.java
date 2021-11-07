package de.jupiterpi.wiesen.website.notifications;

import de.jupiterpi.wiesen.website.App;
import de.jupiterpi.wiesen.website.notifications.messages.Message;
import de.jupiterpi.wiesen.website.notifications.messages.MessageDTO;
import de.jupiterpi.wiesen.website.notifications.messages.MessagesStorage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotificationsController {
    NotificationsBot bot = App.bot;
    MessagesStorage storage = new MessagesStorage();

    @PostMapping("/message")
    public void message(@RequestBody MessageDTO msg) {
        Message message = new Message(msg);
        storage.addMessage(message);
        if (bot != null) bot.notifyMessage(message);
    }
}