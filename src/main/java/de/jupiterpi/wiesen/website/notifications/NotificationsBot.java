package de.jupiterpi.wiesen.website.notifications;

import de.jupiterpi.wiesen.website.ConfigFile;
import de.jupiterpi.wiesen.website.analytics.Visit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationsBot {
    private static final String TOKEN = ConfigFile.getProperty("bot-token");
    private static final String GUILD_ID = ConfigFile.getProperty("guild-id");
    private static final String PING_CHANNEL_ID = ConfigFile.getProperty("ping-channel-id");
    private static final String MESSAGES_CHANNEL_ID = ConfigFile.getProperty("messages-channel-id");
    private static final String VISITS_CHANNEL_ID = ConfigFile.getProperty("visits-channel-id");
    private static final String NEW_VISITS_CHANNEL_ID = ConfigFile.getProperty("new-visits-channel-id");

    private TextChannel pingChannel;
    private TextChannel messagesChannel;
    private TextChannel visitsChannel;
    private TextChannel newVisitsChannel;

    private boolean notifyVisits = ConfigFile.getProperty("notify-visits").equals("true");

    public NotificationsBot() {
        try {
            JDA jda = JDABuilder.createDefault(TOKEN).addEventListeners(new Listener()).build();
            jda.awaitReady();
            Guild guild = jda.getGuildById(GUILD_ID);

            pingChannel = guild.getTextChannelById(PING_CHANNEL_ID);
            messagesChannel = guild.getTextChannelById(MESSAGES_CHANNEL_ID);
            visitsChannel = guild.getTextChannelById(VISITS_CHANNEL_ID);
            newVisitsChannel = guild.getTextChannelById(NEW_VISITS_CHANNEL_ID);
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyMessage(de.jupiterpi.wiesen.website.notifications.messages.Message message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Neue Nachricht");
        builder.setColor(Color.WHITE);
        builder.addField("Absender", message.getEmail(), true);
        builder.addField("", message.getContentForOutput(), false);

        messagesChannel.sendMessage(builder.build()).complete();
    }

    public void notifyVisit(Visit visit, boolean isMobile, boolean isNew) {
        if (!notifyVisits) return;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle((isNew ? "Neuer Aufruf!" : "Aufruf") + (isMobile ? " VON MOBILE" : ""));
        builder.setColor(Color.WHITE);
        builder.addField("Profiling ID", visit.getProfilingId(), true);
        builder.addField("Zeit", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(visit.getTime()), true);
        builder.addField("Seite", visit.getPage(), true);
        builder.addField("Mobile", isMobile ? "VON MOBILE" : "nope", true);

        MessageEmbed embed = builder.build();
        visitsChannel.sendMessage(embed).queue();
        if (isNew) newVisitsChannel.sendMessage(embed).queue();
    }

    private class Listener extends ListenerAdapter {
        @Override
        public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
            if (event.getChannel().equals(pingChannel)) {
                if (event.getAuthor().isBot()) return;

                final Message msg = pingChannel.sendMessage("Server is on and responsive. ").complete();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            event.getMessage().delete().queue();
                            msg.delete().queue();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            if (event.getChannel().equals(messagesChannel)) {
                if (event.getAuthor().isBot()) return;
                if (!event.getMessage().getContentRaw().equalsIgnoreCase("!empty")) return;

                List<Message> messages = messagesChannel.getHistoryFromBeginning(50).complete().getRetrievedHistory();
                messagesChannel.deleteMessages(messages).queue();
            }
        }
    }
}