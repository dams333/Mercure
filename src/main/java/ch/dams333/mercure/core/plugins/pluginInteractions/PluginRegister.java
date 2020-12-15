package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.listener.Listener;

public class PluginRegister {
    Mercure main;

    public PluginRegister(Mercure main) {
        this.main = main;
    }

    /**
     * Méthode d'interaction pour enregistrer un listener
     *
     * @param listener : Class à enregistrer
     */
    public void registerEvents(Listener listener){
        main.listenerManager.registerEvents(listener);
    }

    /**
     * Méthode d'interaction pour enregistrer un exécuteur de commande
     *
     * @param name : Nom de la commande
     * @param executor : Class à enregistrer
     */
    public void registerCommand(String name, CommandExecutor executor){
        main.commandManager.registerCommand(name, executor);
    }
}
