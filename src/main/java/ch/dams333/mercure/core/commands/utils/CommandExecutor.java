package ch.dams333.mercure.core.commands.utils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Interface of the classes that execute commands
 * @author Dams333
 * @version 1.0.0
 */
public interface CommandExecutor {

    /**
     * A user perform the command
     * @param command MercureCommand performed
     * @param user User performed the command
     * @param textChannel Channel where the command was performed
     * @param args Command's argument
     * @return Command worked ?
     * @since 1.0.0
     */
    boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args);

    /**
     * Console perform command
     * @param command MercureCommand performed
     * @param args Command's argument
     * @return Command worked ?
     * @since 1.0.0
     */
    boolean onConsoleCommand(MercureCommand command, String[] args);

}
