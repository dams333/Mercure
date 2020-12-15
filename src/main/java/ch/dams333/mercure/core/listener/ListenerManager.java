package ch.dams333.mercure.core.listener;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.JDA;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager{
    Mercure main;
    public ListenerManager(Mercure mercure) {
        this.main = mercure;
        listeners = new ArrayList<>();
        listenerName = "";
    }

    private List<Listener> listeners;

    public List<Listener> getListeners() {
        return listeners;
    }

    private String listenerName;

    /**
     * Méthode d'enregistrement des listeners
     *
     * @param listener : Class à enregistrer
     */
    public void registerEvents(Listener listener){
        listeners.add(listener);
    }

    /**
     * Méthode pour définir le bot qui écoutera Discord (listener)
     *
     * @param name : Nom du bot
     * @param jda : JDA du bot
     */
    public void setBotListerner(String name, JDA jda) {
        listenerName = name;
        jda.addEventListener(new BotListener(main));
        MercureLogger.log(MercureLogger.LogType.INFO, "Le bot " + name + " est maitenant le listener");
    }

    /**
     * Méthode pour savoir si un bot est le listenet
     *
     * @param name : Nom du bot
     * @return boolean
     */
    public boolean isListener(String name) {
        return name.equalsIgnoreCase(listenerName);
    }
}
