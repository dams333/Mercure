package ch.dams333.mercure.core.commands.baseCommands;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class StopCommand implements CommandExecutor {
    Mercure main;
    public StopCommand(Mercure main) {
        this.main = main;
    }

    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args) {
        return false;
    }

    @Override
    public boolean onConsoleCommand(MercureCommand command, String[] args) {
        main.stop();
        return false;
    }
}
