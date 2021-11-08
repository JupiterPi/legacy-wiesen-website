package de.jupiterpi.wiesen.website;

import de.jupiterpi.wiesen.website.files.ConfigFile;
import de.jupiterpi.wiesen.website.notifications.NotificationsBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static NotificationsBot bot = null;

    public static void main(String[] args) {
        if (ConfigFile.getProperty("activate-bot").equals("true")) {
            bot = new NotificationsBot();
        }
        SpringApplication.run(App.class, args);
    }
}