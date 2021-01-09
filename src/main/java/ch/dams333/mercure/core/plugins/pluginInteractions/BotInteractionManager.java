package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.Bot;
import ch.dams333.mercure.core.listener.BotListener;
import ch.dams333.mercure.core.listener.events.MercureEvent;
import ch.dams333.mercure.utils.exceptions.NoBotException;

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
     * Get a random bot
     * @return Bot's JDA
     * @throws NoBotException There is no connected bot
     * @since 1.0.0
     */
    public Bot getRandomBot(Boolean needToBeConnected) throws NoBotException {
        return main.botsManager.getRandomBot(needToBeConnected);
    }

    public Bot getRandomBotReadyToConnectToVocal() throws NoBotException {
        return main.botsManager.getBotReadyToConnectToVocal();
    }

    /**
     * Call a custom event in listeners
     * @see BotListener
     * @param event Event to call
     * @since 1.0.1
     */
    public void callEvent(MercureEvent event){
        main.listenerManager.performCustomEvent(event);
    }
}
