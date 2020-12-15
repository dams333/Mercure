package ch.dams333.mercure.core.commands.baseCommands;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;

/**
 * Executing class of reload command
 * @author Dams333
 * @version 1.0.0
 */
public class ReloadCommand implements CommandExecutor {
   /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;

    /**
     * Class' constructor
     * @param main Mercure instance
     * @since 1.0.0
     */
    public ReloadCommand(Mercure main) {
        this.main = main;
    }

    
    /**
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args) {
        return false;
    }

    
    /**
     * Reload of the Mercure's plugins
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onConsoleCommand(MercureCommand command, String[] args) {
        MercureLogger.log(MercureLogger.LogType.INFO, "Rechargement des plugins en cours, ne pas éteindre l'application");
        Date startDate = new Date();
        main.commandManager.reloadCommands();
        main.pluginManager.reloadPlugins();
        long millis = new Date().getTime() - startDate.getTime();
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Rechargement des plugins réussi en " + millis + "ms");
        return false;
    }
}
