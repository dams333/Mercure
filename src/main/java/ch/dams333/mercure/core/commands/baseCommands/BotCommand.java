package ch.dams333.mercure.core.commands.baseCommands;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.logger.ConsoleColors;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;

public class BotCommand implements CommandExecutor {
    Mercure main;
    public BotCommand(Mercure main) {
        this.main = main;
    }

    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args) {
        return executeCommand(args);
    }

    @Override
    public boolean onConsoleCommand(MercureCommand command, String[] args) {
        return executeCommand(args);
    }

    /**
     * Méthode de la commande bot
     *
     * @param args : Arguments de la commande
     * @return boolean
     */
    @SuppressWarnings("deprecation")
    private boolean executeCommand(String[] args){
        if(args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if(args.length == 3){
                    String name = args[1];
                    String token = args[2];
                    if(!main.botsManager.isBotByName(name)){
                        main.botsManager.registerBot(name, token);
                        MercureLogger.log(MercureLogger.LogType.SUCESS, "Le bot a été enregistré dans Mercure");
                        return true;
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Il y a déjà un bot avec ce nom");
                    return true;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: !bot create <name> <token>");
                return true;
            }
            if(args[0].equalsIgnoreCase("start")){
                if(args.length == 2){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        if(!main.botsManager.isConnected(name)){
                            main.botsManager.connect(name);
                            return true;
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
            if(args[0].equalsIgnoreCase("stop")){
                if(args.length == 2){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        if(main.botsManager.isConnected(name)){
                            main.botsManager.disconnect(name, true);
                            return true;
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
                        if(!main.botsManager.isConnected(name)){
                            main.botsManager.removeBot(name);
                            return true;
                        }
                        MercureLogger.log(MercureLogger.LogType.ERROR, "Il faut d'abord déconnecter ce bot (!bot stop)");
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
                for(String name : main.botsManager.getBotsTokens().keySet()){
                    if(main.botsManager.isConnected(name)){
                        MercureLogger.log(MercureLogger.LogType.SUB_INFO, "- " + name + " : " + ConsoleColors.GREEN_BOLD + "CONNECTÉ" + ConsoleColors.RESET);
                    }else{
                        MercureLogger.log(MercureLogger.LogType.SUB_INFO, "- " + name + " : " + ConsoleColors.RED + "DÉCONNECTÉ" + ConsoleColors.RESET);
                    }
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("status")){
                if(args.length >= 3){
                    String name = args[1];
                    if(main.botsManager.isBotByName(name)){
                        if(main.botsManager.isConnected(name)){
                            StringBuilder sb = new StringBuilder();
                            for(int i = 2; i < args.length; i++){
                                sb.append(args[i]).append(" ");
                            }
                            main.botsManager.updateStatus(name, sb.toString());
                            return true;
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
                        if(main.botsManager.isConnected(name)){
                            main.botsManager.changePresence(name, args[2]);
                            return true;
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
        MercureLogger.log(MercureLogger.LogType.ERROR, "Format de commande invalide: bot <show/create/delete/start/stop/presence/status>");
        return false;
    }
}
