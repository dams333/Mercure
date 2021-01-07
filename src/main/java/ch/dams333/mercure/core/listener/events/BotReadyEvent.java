package ch.dams333.mercure.core.listener.events;

import ch.dams333.mercure.core.bots.Bot;

public class BotReadyEvent extends MercureEvent{
    private Bot bot;

    public Bot getBot() {
        return this.bot;
    }

    public BotReadyEvent(Bot bot) {
        this.bot = bot;
    }

}
