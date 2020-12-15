package ch.dams333.mercure.core.commands.utils;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.baseCommands.*;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    Mercure main;

    private String tag = "!";

    private String noPermission = "Vous n'avez pas la permission d'éxecuter cette commande !";

    private List<MercureCommand> commands;
    private Map<MercureCommand, CommandExecutor> executors;

    public CommandManager(Mercure mercure) {
        this.main = mercure;
        commands = new ArrayList<>();
        executors = new HashMap<>();
    }

    public String getTag() {
        return tag;
    }

    /**
     * Méthode de récupération des infos en config
     * Méthode d'enregistrement des commandes de base de Mercure
     */
    public void registerBaseCommands() {

        YAMLConfiguration config = main.getConfig();
        if(config.getKeys(false).contains("noPermissionString")){
            noPermission = config.getString("noPermissionString");
        }
        if(config.getKeys(false).contains("commandTag")){
            tag = config.getString("commandTag");
        }

        registerCommand("stop", new StopCommand(main));
        registerCommand("bot", new BotCommand(main));
        registerCommand("mercure", new MercureHelpCommand(main));
        registerCommand("help", new HelpCommand(main));
        registerCommand("reload", new ReloadCommand(main));
    }

    /**
     * Méthode d'enregistrement d'un exécuteur de commande
     *
     * @param name : Nom de la commande
     * @param executor : Exécuteur de la commande
     */
    public void registerCommand(String name, CommandExecutor executor) {
        if(isCommand(name)){
            executors.put(getCommand(name), executor);
            MercureLogger.log(MercureLogger.LogType.INFO, "La commande " + name + " a été enregistrée avec succès par le système");
            return;
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "La commande " + name + " n'existe pas. Elle n'a donc pas pu être enregistrée pas le système");
    }

    /**
     * Méthode pour savoir si une commande avec ce nom existe (alias inclus)
     * @param name : Nom de la commande
     * @return boolean
     */
    public boolean isCommand(String name) {
        for(MercureCommand command : this.commands){
            if(command.getName().equalsIgnoreCase(name) || command.getAliases().contains(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode pour récupérer une commande
     * @param name : Nom de la commande
     * @return MercureCommand
     */
    public MercureCommand getCommand(String name) {
        for(MercureCommand command : this.commands){
            if(command.getName().equalsIgnoreCase(name) || command.getAliases().contains(name)){
                return command;
            }
        }
        return null;
    }

    /**
     * Méthode d'enregistrement des informations d'une commande
     * @param mercureCommand : Commande
     */
    public void registerCommandInformations(MercureCommand mercureCommand) {
        commands.add(mercureCommand);
    }

    /**
     * Méthode lorsque la console exécute une commande
     *
     * @param commandeSTR : Input en console
     */
    public void performConsoleCommand(String commandeSTR) {
        String[] argsWithName = commandeSTR.split(" ");
        if(isCommand(argsWithName[0])){
            if(canConsolePerformCommand(argsWithName[0])){
                MercureCommand command = getCommand(argsWithName[0]);
                String[] args = new String[argsWithName.length - 1];

                for(int i = 1; i < argsWithName.length; i++){
                    args[i - 1] = argsWithName[i];
                }

                executors.get(command).onConsoleCommand(command, args);

            }
            return;
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "La commande " + argsWithName[0] + " n'existe pas");
    }

    /**
     * Méthode pour savoir si la console peut exécuter cette commande
     *
     * @param name : Nom de la commande
     * @return boolean
     */
    private boolean canConsolePerformCommand(String name) {
        MercureCommand command = getCommand(name);
        if(command.getPermissions().contains("all") || command.getPermissions().contains("console")){
            return true;
        }
        return false;
    }

    /**
     * Méthode executée lorsqu'un utilisateur exécute la commande sur Discord
     *
     * @param event : Event détecté par un bot
     */
    public void userCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay().replaceFirst(tag, "");
        MercureLogger.log(MercureLogger.LogType.INFO, event.getAuthor().getName() + " a utilisé la commande: " + message);
        String[] argsWithName = message.split(" ");
        if(isCommand(argsWithName[0])){
            event.getMessage().delete().queue();
            if(canUserPerformCommand(argsWithName[0], event.getAuthor(), event.getTextChannel())){
                MercureCommand command = getCommand(argsWithName[0]);
                String[] args = new String[argsWithName.length - 1];
                for(int i = 1; i < argsWithName.length; i++){
                    args[i - 1] = argsWithName[i];
                }
                executors.get(command).onUserCommand(command, event.getAuthor(), event.getTextChannel(), args);
            }
            return;
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "La commande " + argsWithName[0] + " n'existe pas");
    }

    /**
     * Méthode pour savoir si un utilisateur peut exécuter la commande
     *
     * @param name : Nom de la commande
     * @param user : Utilisateur qui a exécuté la commande
     * @param textChannel : Channel dans lequel la command a été utilisé
     * @return boolean
     */
    private boolean canUserPerformCommand(String name, User user, TextChannel textChannel) {
        MercureCommand command = getCommand(name);
        if(command.getPermissions().contains("all") || command.getPermissions().contains("user")){
            for(String perm : command.getPermissions()){
                if(perm.startsWith("role:")){
                    String roleID = perm.split(":")[1];
                    boolean hasRole = false;
                    for (Role role : user.getJDA().getRoles()) {
                        if(role.getId().equalsIgnoreCase(roleID)){
                            hasRole = true;
                        }
                    }
                    if(!hasRole){
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setDescription(noPermission);
                        textChannel.sendMessage(embedBuilder.build()).queue();
                        MercureLogger.log(MercureLogger.LogType.ERROR, user.getName() + " n'a pas la permission d'utiliser cette commande");
                        return false;
                    }
                }
            }
            return true;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(noPermission);
        textChannel.sendMessage(embedBuilder.build()).queue();
        MercureLogger.log(MercureLogger.LogType.ERROR, user.getName() + " n'a pas la permission d'utiliser cette commande");
        return false;
    }

    public List<MercureCommand> getCommands() {
        return this.commands;
    }

    /**
     * Méthode pour recharger tles commandes
     */
    public void reloadCommands() {
        this.commands = new ArrayList<>();
        main.mercureCommandFileParser.parse();
        registerBaseCommands();
    }
}
