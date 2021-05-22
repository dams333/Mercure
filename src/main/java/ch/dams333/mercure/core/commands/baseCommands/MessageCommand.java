package ch.dams333.mercure.core.commands.baseCommands;

import java.awt.Color;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.Bot;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Executing class of message command
 * 
 * @author Dams333
 * @version 1.1.0
 */
public class MessageCommand implements CommandExecutor {

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
    public MessageCommand(Mercure main) {
        this.main = main;
    }

    /**
     * Parse command and execute
     * 
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, Message message, String[] args) {
        if (args.length >= 3) {
            String botName = args[0];
            StringBuilder messageB = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                messageB.append(args[i]).append(" ");
            }
            if (main.botsManager.isBotByName(botName)) {
                try {
                    Bot bot = main.botsManager.getBot(botName);
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(Color.GRAY);
                    embedBuilder.setDescription(messageB.toString().substring(0, messageB.length() - 1));
                    bot.sendEmbed(embedBuilder, textChannel);;
                } catch (NoBotException e) {
                    MercureLogger.log("Ce bot n'a pas pu être trouvé", e);
                }
            }
        }
        return false;
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
