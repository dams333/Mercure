package ch.dams333.mercure.core.plugins.pluginInteractions;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.exceptions.VoiceException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class BotInteractionManager {
    Mercure main;

    public BotInteractionManager(Mercure main) {
        this.main = main;
    }

    /**
     * Méthode d'interaction de plugin pour récupérer un bot spécifique
     *
     * @param name : Nom du bot
     * @return JDA
     * @throws NoBotException
     */
    public JDA getBot(String name) throws NoBotException {
        return main.botsManager.getBot(name);
    }

    /**
     * Méthode d'interaction de plugin pour récupérer un bot aléatoire
     *
     * @return JDA
     * @throws NoBotException
     */
    public JDA getRandomBot() throws NoBotException {
        return main.botsManager.getRandomBot();
    }

    /**
     * Méthode d'interaction de plugin pour connecter un bot en vocal
     *
     * @param name : Nom du bot
     * @param channel : Channel dans lequel connecter le bot
     * @throws NoBotException
     */
    public void connectToVocal(String name, VoiceChannel channel) throws NoBotException {
        main.botsManager.connectToVocal(name, channel);
    }

    /**
     * Méthode d'interaction de plugin pour ajouter une piste audio à un bot
     *
     * @param name : Nom du bot
     * @param track : Piste à ajouter
     * @param textChannel : Channel d'où vient l'ordre (nécessaire pour récupérer le bot
     * @throws NoBotException
     */
    public void addTrack(String name, String track, TextChannel textChannel) throws NoBotException {
        main.voiceManager.addTrack(name, track, textChannel);
    }

    /**
     * Méthode d'interaction de plugin pour sauter la piste audio acutelle d'un bot
     *
     * @param name : Nom du bot
     * @param channel : Channel d'où vient l'ordre (nécessaire pour récupérer le bot
     * @throws NoBotException
     */
    public void skipTrack(String name, TextChannel channel) throws NoBotException, VoiceException {
        main.voiceManager.skipTrack(name, channel);
    }

    /**
     * Méthode d'interaction de plugin pour vider la liste des pistes audio d'un bot
     *
     * @param name : Nom du bot
     * @param channel : Channel d'où vient l'ordre (nécessaire pour récupérer le bot
     * @throws NoBotException
     */
    public void clearTeacks(String name, TextChannel channel) throws NoBotException, VoiceException {
        main.voiceManager.clearTracks(name, channel);
    }

    /**
     * Méthode d'interaction de plugin pour déconnecter un bot du vocal
     *
     * @param name : Nom du bot
     * @param channel : Channel d'où vient l'ordre (nécessaire pour récupérer le bot
     * @throws NoBotException
     */
    public void disconnectFromVocal(String name, TextChannel channel) throws NoBotException {
        main.botsManager.disconnectFromVocal(name, channel);
    }

    /**
     * Méthode d'interaction de plugin pour faire une recherche youtube
     *
     * @param keeWords : recherche
     * @return Youtube URL
     * @throws IOException
     * @throws ParseException
     */
    public String getYoutubeVideo(String keeWords) throws IOException, ParseException {
        return main.voiceManager.youtubeSearch(keeWords);
    }
}
