package ch.dams333.mercure.core.listener;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.Bot;
import ch.dams333.mercure.core.listener.events.BotDisconnectEvent;
import ch.dams333.mercure.core.listener.events.BotReadyEvent;
import ch.dams333.mercure.core.listener.events.MercureEvent;
import ch.dams333.mercure.core.listener.events.NoMoreDiscordConnectedBot;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Listener's manager
 * @author Dams333
 * @version 1.2.0
 */
public class ListenerManager implements Listener {


    


    /**
     * Mercure instance
     * 
     * @since 1.0.0
     */
    Mercure main;
    /**
     * List of all registered listeners
     * 
     * @since 1.0.0
     */
    private List<Listener> listeners;
    /**
     * Name of the bot who is the listener (the one who listen to Discord)
     * 
     * @since 1.0.0
     */
    private Bot trigerer;

    /**
     * Class' constructor
     * 
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public ListenerManager(Mercure mercure) {
        this.main = mercure;
        listeners = new ArrayList<>();
        this.registerEvents(this);
        trigerer = null;
    }

    /**
     * Get all listener's classes
     * 
     * @return List
     * @since 1.0.0
     */
    public List<Listener> getListeners() {
        return listeners;
    }


    public Bot getTrigerer() {
        return this.trigerer;
    }


    /**
     * Register a listener
     * 
     * @param listener Class to register
     * @since 1.0.0
     */
    public void registerEvents(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Choose the listener's bot (the one who listen to Discord)
     * 
     * @param name Bot's name
     * @param jda  Bot's JDA
     * @since 1.0.0
     */
    public void setTriggererBot(Bot bot) {
        trigerer = bot;
        bot.setTriggerer();
        MercureLogger.log(MercureLogger.LogType.INFO, "Le bot " + bot.getName() + " est maitenant le triggerer");
    }

    @EventHandler
    public void onBotReady(BotReadyEvent e) {
        if (this.trigerer == null) {
            this.trigerer = e.getBot();
            e.getBot().getJda().addEventListener(new BotListener(main));
            MercureLogger.log(LogType.INFO, "Le bot " + e.getBot().getName() + " est maitenant le Trigerer");
        }
    }

    @EventHandler
    public void onBotDisconnec(BotDisconnectEvent e) {
        if (this.isListener(e.getBot().getName())) {
            this.trigerer = null;
            try {
                trigerer = main.botsManager.getRandomBot(true);
                trigerer.getJda().addEventListener(new BotListener(main));
                MercureLogger.log(LogType.INFO, "Le bot " + trigerer.getName() + " est maitenant le Trigerer");
            } catch (NoBotException e1) {
                performCustomEvent(new NoMoreDiscordConnectedBot());
            }
        }
    }

    @EventHandler
    public void onNoMoreBot(NoMoreDiscordConnectedBot e){
        MercureLogger.log(LogType.WARN, "Il n'y a plus de bot connecté à Discord pour être le Trigerer");
    }

    /**
     * Is a bot the listener (the one who listen to Discord)
     * @param name Bot's name
     * @return Boolean
     * @since 1.0.0
     */
    public boolean isListener(String name) {
        if(trigerer != null){
            return name.equalsIgnoreCase(trigerer.getName());
        }else{
            return false;
        }
    }

    /**
     * Disable all loaded Listeners for plugin reloadinf
     * @since 1.0.1
     */
	public void reloadListeners() {
        this.listeners = new ArrayList<>();
    }
    
    public void performCustomEvent(MercureEvent event){
        for(Listener listener : getListeners()){
            for(Method method : listener.getClass().getDeclaredMethods()){
                for(Annotation annotation : method.getDeclaredAnnotations()){
                    if(annotation instanceof EventHandler){
                        Class<?> parameter = method.getParameterTypes()[0];
                        if(parameter.isInstance(event)){
                            try {
                                method.invoke(listener, event);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                MercureLogger.log("Impossible d'accéder à la méthode demandée", e);
                            }
                        }
                    }
                }
            }
        }

    }
}
