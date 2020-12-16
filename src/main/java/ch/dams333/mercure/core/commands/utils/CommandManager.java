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

/**
 * Mercure's command's manager
 * @author Dams333
 * @version 1.0.1
 */
public class CommandManager {
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;
    /**
     * Commands' tag
     * @since 1.0.0
     */
    private String tag = "!";
    /**
     * String printed when a user doesn't have the permission
     * @since 1.0.0
     */
    private String noPermission = "Vous n'avez pas la permission d'éxecuter cette commande !";
    /**
     * List of the registerd commands
     * @since 1.0.0
     */
    private List<MercureCommand> commands;
    /**
     * List of the CommandExecutors linked to MercureCommand
     * @see CommandExecutor
     * @since 1.0.0
     */
    private Map<MercureCommand, CommandExecutor> executors;

    /**
     * Class' constructor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public CommandManager(Mercure mercure) {
        this.main = mercure;
        commands = new ArrayList<>();
        executors = new HashMap<>();
    }

    /**
     * Get the tag used to detect commands
     * @return Tag
     * @since 1.0.0
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get all util informations for commands' execution
     * @since 1.0.0
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
        registerCommand("message", new MessageCommand(main));
    }

    /**
     * Register a CommandExecutor for a command
     * @param name Command's name
     * @param executor CommandExecutor
     * @since 1.0.0
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
     * Is a command with a specific name
     * @param name Command's name
     * @return Boolean
     * @since 1.0.0
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
     * Get a command
     * @param name Command's name
     * @return MercureCommand
     * @since 1.0.0
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
     * Save the command's informations
     * @param mercureCommand Command
     * @since 1.0.0
     */
    public void registerCommandInformations(MercureCommand mercureCommand) {
        commands.add(mercureCommand);
    }

    /**
     * Console performed a command
     * @param commandeSTR Console's input
     * @since 1.0.0
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
     * Can the console execute this command
     * @param name Command's name
     * @return Boolean
     * @since 1.0.0
     */
    private boolean canConsolePerformCommand(String name) {
        MercureCommand command = getCommand(name);
        if(command.getPermissions().contains("all") || command.getPermissions().contains("console")){
            return true;
        }
        return false;
    }

    /**
     * User performed a command
     * @param event By a bot detected event
     * @since 1.0.0
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
     * Can a user perform this command (include role's permissions)
     * @param name Command's name
     * @param user User performes the command
     * @param textChannel Channel where the command was performed
     * @return Boolean
     * @since 1.0.0
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

    /**
     * Get all commands' informations
     * @return
     * @since 1.0.0
     */
    public List<MercureCommand> getCommands() {
        return this.commands;
    }

    /**
     * Reload all commands' informations
     * @since 1.0.0
     */
    public void reloadCommands() {
        this.commands = new ArrayList<>();
        main.mercureCommandFileParser.parse();
        registerBaseCommands();
    }
}
