package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.listener.Listener;

/**
 * Plugins' registers' manager Class. Used to control the accesses of plugins
 * @see BotsManager
 * @author Dams333
 * @version 1.0.0
 */
public class PluginRegister {
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;

    /**
     * Class' contructor
     * @param main Mercure class
     * @since 1.0.0
     */
    public PluginRegister(Mercure main) {
        this.main = main;
    }

    /**
     * Register a Listener
     * @see ListenerManager
     * @param listener Class to register
     * @since 1.0.0
     */
    public void registerEvents(Listener listener){
        main.listenerManager.registerEvents(listener);
    }

    /**
     * Register a command
     * @see CommandManager
     * @param name Command's name
     * @param executor Command's executor
     * @since 1.0.0
     */
    public void registerCommand(String name, CommandExecutor executor){
        main.commandManager.registerCommand(name, executor);
    }
}
