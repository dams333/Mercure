package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.listener.Listener;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.exceptions.VoiceException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.ArrayList;

public class PluginInteractionsManager {

    Mercure main;

    BotInteractionManager botInteractionManager;
    PluginRegister pluginRegister;

    public PluginInteractionsManager(Mercure mercure) {
        this.main = mercure;
        botInteractionManager = new BotInteractionManager(main);
        pluginRegister = new PluginRegister(main);
    }

    /**
     * Méthode d'intéraction de plugin pour récupérer la classe permettant d'enregistrer des listener et des commandes
     *
     * @return PluginRegister
     */
    public PluginRegister getRegisterManager(){
        return pluginRegister;
    }

    /**
     * Méthode d'intéraction de plugin pour récupérer la classe permettant de gérer les interactions de bots
     *
     * @return BotInteractionManager
     */
    public BotInteractionManager getBotInteractionsManager(){
        return botInteractionManager;
    }

}
