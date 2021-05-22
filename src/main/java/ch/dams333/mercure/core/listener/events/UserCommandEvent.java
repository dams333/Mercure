package ch.dams333.mercure.core.listener.events;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Event called when a user perform a command
 * @author Dams333
 * @version 1.0.0
 */
public class UserCommandEvent extends MercureEvent{
    
    /**
     * Command performed
     * @since 1.0.0
     */
    private MercureCommand command;

    /**
     * The user send the command
     * @since 1.0.0
     */
    private User sender;

    /**
     * The channel where the command was send
     * @since 1.0.0
     */
    private TextChannel textChannel;

    /**
     * The message of the command
     * @since 1.0.0
     */
    private Message message;

    /**
     * The arguments of the command
     * @since 1.0.0
     */
    private String[] args;

    /**
     * Is tihs event cancelled
     * @since 1.0.0
     */
    private boolean cancelled;

    /**
     * Basic constructor of the event
     * @param command Command performed
     * @param sender Sender of the command
     * @param textChannel Channel where the command was send
     * @param message Command's message
     * @param args Arguments of the command
     * @since 1.0.0
     */
    public UserCommandEvent(MercureCommand command, User sender, TextChannel textChannel, Message message, String[] args) {
        this.command = command;
        this.sender = sender;
        this.textChannel = textChannel;
        this.message = message;
        this.args = args;
        cancelled = false;
    }

    /**
     * Set thi bot concelled
     * @param cancelled boolean
     * @since 1.0.0
     */
    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }

    /**
     * Get the performed command
     * @return MecureCommand
     * @since 1.0.0
     */
    public MercureCommand getCommand() {
        return this.command;
    }

    /**
     * Get the Command's sender
     * @return User
     * @since 1.0.0
     */
    public User getSender() {
        return this.sender;
    }

    /**
     * Get the channel where the command was performed
     * @return TextChannel
     * @since 1.0.0
     */
    public TextChannel getTextChannel() {
        return this.textChannel;
    }

    /**
     * Get the command's message
     * @return Message
     * @since 1.0.0
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * Get the command's arguments
     * @return Table of string
     * @since 1.0.0
     */
    public String[] getArgs() {
        return this.args;
    }

    /**
     * Get the performed command
     * @return MecureCommand
     * @since 1.0.0
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Execute the command of the event
     * @since 1.0.0
     */
    public void executeCommand() {
        if(!cancelled){
            Mercure.INSTANCE.commandManager.getExecutors().get(command).onUserCommand(command, sender, textChannel, message, args);
        }else{
            MercureLogger.log(LogType.DEBUG, "La commande a été annulée par son event");
        }
    }

}
