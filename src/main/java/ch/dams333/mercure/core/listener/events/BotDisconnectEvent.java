package ch.dams333.mercure.core.listener.events;

import ch.dams333.mercure.core.bots.Bot;

public class BotDisconnectEvent extends MercureEvent{
    private Bot bot;

    public Bot getBot() {
        return this.bot;
    }

    public BotDisconnectEvent(Bot bot) {
        this.bot = bot;
    }

}
