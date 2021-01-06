package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.Bot;
import ch.dams333.mercure.core.listener.BotListener;
import ch.dams333.mercure.core.listener.MercureEvent;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.exceptions.VoiceException;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Basic plugins' interactions' manager Class. Used to control the accesses of plugins
 * @see BotsManager
 * @author Dams333
 * @version 1.1.0
 */
public class BotInteractionManager {
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
    public BotInteractionManager(Mercure main) {
        this.main = main;
    }

    /**
     * Get a specific bot
     * @param name Bot's name
     * @return Bot's JDA
     * @throws NoBotException There is no bot with this name
     * @since 1.0.0
     */
    public Bot getBot(String name) throws NoBotException {
        return main.botsManager.getBot(name);
    }

    /**
     * MGet a random bot
     * @return Bot's JDA
     * @throws NoBotException There is no connected bot
     * @since 1.0.0
     */
    public Bot getRandomBot(Boolean needToBeConnected) throws NoBotException {
        return main.botsManager.getRandomBot(needToBeConnected);
    }

    /**
     * Connect a bot to vocal
     * @param name Bot's name
     * @param channel Channel to connect
     * @throws NoBotException Impossible to get the bot
     * @since 1.0.0
     */
    public void connectToVocal(String name, VoiceChannel channel) throws NoBotException {
        main.botsManager.connectToVocal(name, channel);
    }

    /**
     * Add an AudioTrack to a bot
     * @see VoiceManager
     * @param name Bot's name
     * @param track Track to add
     * @param textChannel Channel of the guild (needed to get the bot)
     * @throws NoBotException Impossible to get the bot
     * @since 1.0.0
     */
    public void addTrack(String name, String track, TextChannel textChannel) throws NoBotException {
        main.voiceManager.addTrack(name, track, textChannel);
    }

    /**
     * Skip current track of a bot
     * @see VoiceManager
     * @param name Bot's name
     * @param channel Channel of the guild (needed to get the bot)
     * @throws NoBotException Impossible to get the bot
     * @since 1.0.0
     */
    public void skipTrack(String name, TextChannel channel) throws NoBotException, VoiceException {
        main.voiceManager.skipTrack(name, channel);
    }

    /**
     * Clear audio list of a bot
     * @see VoiceManager
     * @param name Bot's name
     * @param channel Channel of the guild (needed to get the bot)
     * @throws NoBotException Impossible to get the bot
     * @since 1.0.0
     */
    public void clearTeacks(String name, TextChannel channel) throws NoBotException, VoiceException {
        main.voiceManager.clearTracks(name, channel);
    }

    /**
     * Disconnect a bot from VoiceChannel
     * @see VoiceManager
     * @param name Bot's name
     * @param channel Channel of the guild (needed to get the bot)
     * @throws NoBotException Impossible to get the bot
     * @since 1.0.0
     */
    public void disconnectFromVocal(String name, TextChannel channel) throws NoBotException {
        main.botsManager.disconnectFromVocal(name, channel);
    }

    /**
     * Make a youtube search
     * @see VoiceManager
     * @param keeWords Kee words
     * @return Youtube URL
     * @throws IOException Impossible to execute the request
     * @throws ParseException Impossible to read the request
     * @since 1.0.0
     */
    public String getYoutubeVideo(String keeWords) throws IOException, ParseException {
        return main.voiceManager.youtubeSearch(keeWords);
    }

    /**
     * Call a custom event in listeners
     * @see BotListener
     * @param event Event to call
     * @since 1.0.1
     */
    public void callEvent(MercureEvent event){
        BotListener.performCustomEvent(event);
    }
}
