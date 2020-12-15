package ch.dams333.mercure.core.commands.utils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public interface CommandExecutor {

    boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args);

    boolean onConsoleCommand(MercureCommand command, String[] args);

}
