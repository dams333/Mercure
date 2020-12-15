package ch.dams333.mercure.core.commands.baseCommands;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

/**
 * Executing class of help command
 * @author Dams333
 * @version 1.0.0
 */
public class HelpCommand implements CommandExecutor {
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
    public HelpCommand(Mercure main) {
        this.main = main;
    }

    
    /**
     * Detect type of help
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args) {
        if(args.length != 1){
            printGlobalHelp(textChannel);
        }else{
            if(main.commandManager.isCommand(args[0])){
                printSpecialHelp(textChannel, main.commandManager.getCommand(args[0]));
            }else{
                printGlobalHelp(textChannel);
            }
        }
        return false;
    }

    /**
     * Print specific command's help
     * @param textChannel Channel where the message needs to be sended
     * @param command MercureCommand's informations whose needs to be sended
     * @since 1.0.0
     */
    private void printSpecialHelp(TextChannel textChannel, MercureCommand command) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Aide de la commande " + command.getName());
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setDescription("Liste des arguments possible pour la commande :");
        StringBuilder formatBuilder = new StringBuilder();
        for(String format : command.getFormat()){
            formatBuilder.append("┣ ").append(format).append("\n");
        }
        embedBuilder.addField(main.commandManager.getTag() + command.getName() + " : " + command.getDescription(), formatBuilder.toString(), false);
        if(command.getAliases() != null && command.getAliases().size() > 0) {
            StringBuilder aliasesBuilder = new StringBuilder();
            for (String alias : command.getAliases()) {
                aliasesBuilder.append(alias + " | ");
            }
            String aliases = aliasesBuilder.deleteCharAt(aliasesBuilder.length() - 2).toString();
            embedBuilder.addField("Aliases:", aliases, false);
        }
        try {
            main.botsManager.getRandomBot().getTextChannelById(textChannel.getId()).sendMessage(embedBuilder.build()).queue();
        } catch (NoBotException e) {
            MercureLogger.log("Impossible d'envoyer le message", e);
        }
    }

    /**
     * Print command's list
     * @param textChannel Channel where the message needs to be sended
     * @since 1.0.0
     */
    private void printGlobalHelp(TextChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Mercure app");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setDescription("Liste des commandes disponibles avec Mercure :");
        StringBuilder sb = new StringBuilder();
        for(MercureCommand command : main.commandManager.getCommands()){
            sb.append(":white_small_square: ");
            sb.append(main.commandManager.getTag());
            sb.append(command.getName());
            sb.append(" : ");
            sb.append(command.getDescription());
            sb.append("\n");
        }
        embedBuilder.addField("", sb.toString(), false);
        try {
            main.botsManager.getRandomBot().getTextChannelById(textChannel.getId()).sendMessage(embedBuilder.build()).queue();
        } catch (NoBotException e) {
            MercureLogger.log("Impossible d'envoyer le message", e);
        }
    }

    
    /**
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onConsoleCommand(MercureCommand command, String[] args) {
        return false;
    }
}
