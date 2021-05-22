package ch.dams333.mercure.core.commands.baseCommands;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.Bot;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.ConsoleColors;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Executing class of bot command
 * 
 * @author Dams333
 * @version 1.1.0
 */
public class BotCommand implements CommandExecutor {
    /**
     * Mercure instance
     * 
     * @since 1.0.0
     */
    Mercure main;

    /**
     * Class' constructor
     * 
     * @param main Mercure instance
     * @since 1.0.0
     */
    public BotCommand(Mercure main) {
        this.main = main;
    }

    /**
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, Message message, String[] args) {
        return executeCommand(args);
    }

    /**
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onConsoleCommand(MercureCommand command, String[] args) {
        return executeCommand(args);
    }

    /**
     * Execute command
     * 
     * @param args Command's arguments
     * @return not used
     * @since 1.0.0
     */
    private boolean executeCommand(String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 3) {
                    String name = args[1];
                    String token = args[2];
                    if (!main.botsManager.isBotByName(name)) {
                        main.botsManager.registerBot(name, token);
                        MercureLogger.log(MercureLogger.LogType.SUCESS, "Le bot a été enregistré dans Mercure");
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il y a déjà un bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR,
                        "Format de commande invalide: !bot create <name> <token>");
                return true;
            }
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length == 2) {
                    String name = args[1];
                    if (main.botsManager.isBotByName(name)) {
                        try {
                            if (!main.botsManager.getBot(name).isConnectedToDiscord()) {
                                main.botsManager.connect(name);
                                return true;
                            }
                        } catch (NoBotException e) {
                            MercureLogger.log("Impossible de récupérer le bot " + name, e);
                        }
                        MercureLogger.log(MercureLogger.LogType.ERROR, "Ce bot est déjà connecté");
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il n'y a pas de bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: !bot start <name>");
                return true;
            }
            if(args[0].equalsIgnoreCase("startall")){
                for(Bot bot : main.botsManager.getBots()){
                    if(!bot.isConnectedToDiscord()){
                        bot.connectToDiscord();
                    }
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("stop")){
                if(args.length == 2){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        try {
                            if (main.botsManager.getBot(name).isConnectedToDiscord()) {
                                main.botsManager.getBot(name).disconnectFromDiscord();
                                return true;
                            }
                        } catch (NoBotException e) {
                            MercureLogger.log("Impossible de récupérer le bot " + name, e);
                        }
                        MercureLogger.log(MercureLogger.LogType.ERROR, "Ce bot n'est pas connecté");
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il n'y a pas de bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: !bot stop <name>");
                return true;
            }
            if(args[0].equalsIgnoreCase("delete")){
                if(args.length == 2){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        try {
                            main.botsManager.removeBot(name);
                        } catch (NoBotException e) {
                            MercureLogger.log("Impossible de déconnecter le bot " + name, e);
                        }
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il n'y a pas de bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: !bot delete <name>");
                return true;
            }
            if(args[0].equalsIgnoreCase("show")){
                MercureLogger.log(MercureLogger.LogType.INFO, "Liste des bots:");
                for(Bot bot : main.botsManager.getBots()){
                    if(bot.isConnectedToDiscord()){
                        MercureLogger.log(MercureLogger.LogType.SUB_INFO, "- " + bot.getName() + " : " + ConsoleColors.GREEN_BOLD + "CONNECTÉ" + ConsoleColors.RESET);
                    }else{
                        MercureLogger.log(MercureLogger.LogType.SUB_INFO, "- " + bot.getName() + " : " + ConsoleColors.RED + "DÉCONNECTÉ" + ConsoleColors.RESET);
                    }
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("status")){
                if(args.length >= 3){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        try {
                            if (main.botsManager.getBot(name).isConnectedToDiscord()) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                main.botsManager.getBot(name).changeStatus(sb.toString());
                                return true;
                            }
                        } catch (NoBotException e) {
                            MercureLogger.log("Impossible de récupérer le bot " + name, e);
                        }
                        MercureLogger.log(MercureLogger.LogType.ERROR, "Il faut d'abord connecter ce bot (!bot start)");
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il n'y a pas de bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: !bot status <name> <status>");
                return true;
            }
            if(args[0].equalsIgnoreCase("presence")){
                if(args.length == 3){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        try {
                            if (main.botsManager.getBot(name).isConnectedToDiscord()) {
                                main.botsManager.getBot(name).changePresence(args[2]);
                                return true;
                            }
                        } catch (NoBotException e) {
                            MercureLogger.log("Impossible de récupérer le bot " + name, e);
                        }
                        MercureLogger.log(MercureLogger.LogType.ERROR, "Il faut d'abord connecter ce bot (!bot start)");
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il n'y a pas de bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: !bot presence <name> <online|dnd|idle|offline>");
                return true;
            }
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: bot <show/create/delete/startall/start/stop/presence/status>");
        return false;
    }
}
