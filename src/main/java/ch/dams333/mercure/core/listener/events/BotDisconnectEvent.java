package ch.dams333.mercure.core.listener.events;

import ch.dams333.mercure.core.bots.Bot;

/**
 * Event called when a bot is disconnected
 * @author Dams333
 * @version 1.0.0
 */
public class BotDisconnectEvent extends MercureEvent{
    /**
     * Event's bot
     * @since 1.0.0
     */
    private Bot bot;

    /**
     * Get the disconnected bot
     * @return Bot's object
     * @since 1.0.0
     */
    public Bot getBot() {
        return this.bot;
    }

    /**
     * Event's constructor
     * @param bot Disconnected bot
     * @since 1.0.0
     */
    public BotDisconnectEvent(Bot bot) {
        this.bot = bot;
    }

}
