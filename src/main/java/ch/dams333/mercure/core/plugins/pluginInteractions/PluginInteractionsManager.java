package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;

/**
 * Plugins' base access to Mercure's methodes
 * @see MercurePlugin
 * @author Dams333
 * @version 1.0.0
 */
public class PluginInteractionsManager {

    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;
    /**
     * The manager with the base interactions of plugins
     * @since 1.0.0
     */
    BotInteractionManager botInteractionManager;
    /**
     * The manager with the registering methodes
     * @since 1.0.0
     */
    PluginRegister pluginRegister;

    /**
     * Class' contrucutor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public PluginInteractionsManager(Mercure mercure) {
        this.main = mercure;
        botInteractionManager = new BotInteractionManager(main);
        pluginRegister = new PluginRegister(main);
    }

    /**
     * Get the PluginRegister's class
     * @return PluginRegister
     * @since 1.0.0
     */
    public PluginRegister getRegisterManager(){
        return pluginRegister;
    }

    /**
     * Get the BotInteractionManager's class
     * @return BotInteractionManager
     * @since 1.0.0
     */
    public BotInteractionManager getBotInteractionsManager(){
        return botInteractionManager;
    }

}
