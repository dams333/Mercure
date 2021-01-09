package ch.dams333.mercure.core.listener.events.jdaCustomEvents;

import ch.dams333.mercure.core.bots.Bot;
import ch.dams333.mercure.core.listener.events.MercureEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ReceiveMessageEvent extends MercureEvent{
    private Bot trigerer;
    private User author;
    private Guild guild;
    private TextChannel textChannel;
    private String messageID;
    private Message content;

    public Bot getTrigerer() {
        return this.trigerer;
    }

    public User getAuthor() {
        return this.author;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public TextChannel getTextChannel() {
        return this.textChannel;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public Message getContent() {
        return this.content;
    }

    public ReceiveMessageEvent(Bot trigerer, User author, Guild guild, TextChannel textChannel, String messageID, Message content) {
        this.trigerer = trigerer;
        this.author = author;
        this.guild = guild;
        this.textChannel = textChannel;
        this.messageID = messageID;
        this.content = content;
    }
}
