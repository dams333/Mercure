package ch.dams333.mercure.core.listener.events;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class UserCommandEvent extends MercureEvent{
    
    private MercureCommand command;
    private User sender;
    private TextChannel textChannel;
    private Message message;
    private String[] args;

    private boolean cancelled;

    public UserCommandEvent(MercureCommand command, User sender, TextChannel textChannel, Message message, String[] args) {
        this.command = command;
        this.sender = sender;
        this.textChannel = textChannel;
        this.message = message;
        this.args = args;
        cancelled = false;
    }

    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }


    public MercureCommand getCommand() {
        return this.command;
    }

    public User getSender() {
        return this.sender;
    }

    public TextChannel getTextChannel() {
        return this.textChannel;
    }

    public Message getMessage() {
        return this.message;
    }

    public String[] getArgs() {
        return this.args;
    }

    public boolean getCancelled() {
        return this.cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void executeCommand() {
        if(!cancelled){
            Mercure.INSTANCE.commandManager.getExecutors().get(command).onUserCommand(command, sender, textChannel, message, args);
        }else{
            MercureLogger.log(LogType.DEBUG, "La commande a été annulée par son event");
        }
    }

}
