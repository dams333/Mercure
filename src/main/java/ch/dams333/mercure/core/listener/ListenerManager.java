package ch.dams333.mercure.core.listener;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.JDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener's manager
 * @author Dams333
 * @version 1.0.0
 */
public class ListenerManager{
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;
    /** 
     * List of all registered listeners
     * @since 1.0.0
    */
    private List<Listener> listeners;
    /**
     * Name of the bot who is the listener (the one who listen to Discord)
     * @since 1.0.0
     */
    private String listenerName;

    /**
     * Class' constructor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public ListenerManager(Mercure mercure) {
        this.main = mercure;
        listeners = new ArrayList<>();
        listenerName = "";
    }

    /**
     * Get all listener's classes
     * @return List
     * @since 1.0.0
     */
    public List<Listener> getListeners() {
        return listeners;
    }

    /**
     * Register a listener
     * @param listener Class to register
     * @since 1.0.0
     */
    public void registerEvents(Listener listener){
        listeners.add(listener);
    }

    /**
     * Choose the listener's bot (the one who listen to Discord)
     * @param name Bot's name
     * @param jda Bot's JDA
     * @since 1.0.0
     */
    public void setBotListerner(String name, JDA jda) {
        listenerName = name;
        jda.addEventListener(new BotListener(main));
        MercureLogger.log(MercureLogger.LogType.INFO, "Le bot " + name + " est maitenant le listener");
    }

    /**
     * Is a bot the listener (the one who listen to Discord)
     * @param name Bot's name
     * @return Boolean
     * @since 1.0.0
     */
    public boolean isListener(String name) {
        return name.equalsIgnoreCase(listenerName);
    }
}
