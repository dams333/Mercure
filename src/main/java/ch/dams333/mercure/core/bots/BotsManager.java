package ch.dams333.mercure.core.bots;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.security.auth.login.LoginException;
import java.util.*;

public class BotsManager {
    Mercure main;
    private Map<String, String> botsTokens;
    private Map<String, Boolean> isConnected;
    private Map<String, JDA> jdas;
    public BotsManager(Mercure mercure) {
        this.main = mercure;
        botsTokens = new HashMap<>();
        isConnected = new HashMap<>();
        jdas = new HashMap<>();
        voiceConnected = new HashMap<>();
    }

    public boolean isBotByName(String name) {
        return botsTokens.keySet().contains(name);
    }

    /**
     * Méthode pour enregistrer un bot sur Mercure
     *
     * @param name : Nom à donner au bot
     * @param token : Token du bot
     */
    public void registerBot(String name, String token) {
        botsTokens.put(name, token);
        isConnected.put(name, false);
        this.serializeBots();
    }

    public Map<String, String> getBotsTokens(){
        return botsTokens;
    }

    /**
     * Méthode d'enregistrement des bots dans le fichier bots.yml
     */
    private void serializeBots() {

        YAMLConfiguration yamlConfiguration = new YAMLConfiguration(new LinkedHashMap<String, Object>());

        MercureLogger.log(MercureLogger.LogType.DEBUG, "Serialization de " + botsTokens.size() + " bots");

        for(String name : this.botsTokens.keySet()){
            yamlConfiguration.set(name, this.botsTokens.get(name));
        }

        MercureLogger.log(MercureLogger.LogType.DEBUG, "Serialization des bots terminée");

        yamlConfiguration.save("bots.yml");

    }

    /**
     * Méthode de chargement des bots depuis le fichier bots.yml
     */
    public void deserializeBots() {
        YAMLConfiguration yamlConfiguration = YAMLConfiguration.load("bots.yml");
        MercureLogger.log(MercureLogger.LogType.DEBUG, "Déserialization de " + yamlConfiguration.getKeys(false).size() + " bots...");
        for(String name : yamlConfiguration.getKeys(false)){
            botsTokens.put(name, yamlConfiguration.getString(name));
            isConnected.put(name, false);
        }
        MercureLogger.log(MercureLogger.LogType.INFO, botsTokens.size() + " bots chargés par Mercure");
    }

    /**
     * Méthode pour savoir si un bot est démarré et connecté à Discord
     *
     * @param name : Nom du bot
     * @return boolean
     */
    public boolean isConnected(String name) {
        return isConnected.get(name);
    }

    /**
     * Méthode pour démarrer un bot et le connecter à Discord
     *
     * @param name : Nom du bot
     */
    @SuppressWarnings("deprecation")
    public void connect(String name) {
        Date starting = new Date();
        MercureLogger.log(MercureLogger.LogType.INFO, "Démarrage du bot " + name + " ...");
        String token = botsTokens.get(name);
        try {
            JDA jda = JDABuilder.createDefault(token).build();
            isConnected.put(name, true);
            long millis = new Date().getTime() - starting.getTime();
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Le bot " + name + " a démarré en " + millis + "ms");
            if(jdas.size() <= 0){
                main.listenerManager.setBotListerner(name, jda);
            }
            jdas.put(name, jda);
        } catch (LoginException e){
            MercureLogger.log("Impossible de connecter le bot", e);
        }
    }

    /**
     * Méthode pour déconnecter tous les bots de Discord
     */
    public void disconnectAllBots() {
        for(String name : jdas.keySet()){
            disconnect(name, false);
        }
    }

    /**
     * Méthode pour déconnecter un bot de Discord
     *
     * @param name : Nom du bot
     * @param replacing : S'il est le listerner, le bot doit-il être remplacé
     */
    public void disconnect(String name, boolean replacing) {
        MercureLogger.log(MercureLogger.LogType.DEBUG, "Déconnexion du bot " + name + " ...");
        JDA jda = this.jdas.get(name);

        boolean needToBeReplace = false;

        if(main.listenerManager.isListener(name)){
            needToBeReplace = true;
        }

        jda.shutdown();
        if(replacing) {
            this.jdas.remove(name);
            this.isConnected.put(name, false);
        }
        MercureLogger.log(MercureLogger.LogType.INFO, "Bot " + name + " déconnecté");

        if(needToBeReplace && replacing){
            if(this.jdas.size() > 0){
                main.listenerManager.setBotListerner(name, (JDA) jdas.values().toArray()[0]);
            }else{
                MercureLogger.log(MercureLogger.LogType.INFO, "Tous les bots étant déconnectés il n'est désormais plus possible de savoir ce qu'il se passe sur Discord");
            }
        }

    }

    /**
     * Méthode pour supprimer un bot de Mercure
     *
     * @param name : Nom du bot
     */
    public void removeBot(String name) {
        this.botsTokens.remove(name);
        this.isConnected.remove(name);
        this.jdas.remove(name);
        serializeBots();
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Bot " + name + " supprimé ");
    }

    /**
     * Méthode pour mettre à jour le message de status du bot
     *
     * @param name : Nom du bot
     * @param status : Status du bot
     */
    public void updateStatus(String name, String status) {
        JDA jda = jdas.get(name);
        jda.getPresence().setActivity(Activity.playing(status));
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Status du bot mis à jour");
    }

    /**
     * Méthode pour changer la présence du bot
     *
     * @param name : Nom du bot
     * @param presence : online/dnd/idle/offline
     */
    public void changePresence(String name, String presence) {
        JDA jda = jdas.get(name);
        if(presence.equalsIgnoreCase("online")){
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence modifiée");
            return;
        }
        if(presence.equalsIgnoreCase("dnd")){
            jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence modifiée");
            return;
        }
        if(presence.equalsIgnoreCase("idle")){
            jda.getPresence().setStatus(OnlineStatus.IDLE);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence modifiée");
            return;
        }
        if(presence.equalsIgnoreCase("offline")){
            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence modifiée");
            return;
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "Présence invalide: online | dnd | idle |offline>");
    }

    /**
     * Méthode pour récupérer un bot aléatoire géré par Mercure
     *
     * @return JDA
     * @throws NoBotException
     */
    public JDA getRandomBot() throws NoBotException {
        if(this.jdas.size() > 0){
            int random = random(0, this.jdas.size() - 1);
            return (JDA) jdas.values().toArray()[random];
        }else{
            throw new NoBotException("Il n'y a pas de bot connecté sur cette instance de Mercure, il est donc impossible d'en prendre un de manière aléatoire");
        }
    }

    /**
     * Méthode pour récupérer un bot géré par Mercure
     *
     * @param name : Nom du bot
     * @return JDA
     * @throws NoBotException
     */
    public JDA getBot(String name) throws NoBotException{
        if(this.jdas.keySet().contains(name)){
            return this.jdas.get(name);
        }else{
            throw new NoBotException("Impossible de récupérer un bot portant ce nom");
        }
    }

    private Map<String, VoiceChannel> voiceConnected;

    /**
     * Méthode pour connecter un bot en vocal
     *
     * @param name : Nom du bot
     * @param voiceChannel : Channel dans lequel le bot doit se connecter
     * @throws NoBotException
     */
    public void connectToVocal(String name, VoiceChannel voiceChannel) throws NoBotException {
        if(isConnected(name)){
            JDA jda = getBot(name);

            VoiceChannel channel = jda.getVoiceChannelById(voiceChannel.getId());
            AudioManager audioManager = channel.getGuild().getAudioManager();
            audioManager.openAudioConnection(channel);

            this.voiceConnected.put(name, voiceChannel);

        }else{
            throw new NoBotException("Ce bot n'est pas connecté");
        }
    }

    /**
     * Méthode pour déconnecter un bot des salons vocaux
     *
     * @param name : Nom du bot
     * @param textChannel : Channel duquel vient l'ordre (nécessaire pour récupérer le bot)
     * @throws NoBotException
     */
    public void disconnectFromVocal(String name, TextChannel textChannel) throws NoBotException {
        if(isConnected(name)) {
            JDA jda = getBot(name);
            main.voiceManager.stopMusic(name, textChannel);
            VoiceChannel channel = jda.getVoiceChannelById(voiceConnected.get(name).getId());
            AudioManager audioManager = channel.getGuild().getAudioManager();
            audioManager.closeAudioConnection();
        }else{
            throw new NoBotException("Ce bot n'est pas connecté");
        }
    }

    /**
     * Méthode pour savoir si un bot est connecté en vocal
     *
     * @param name : Nom du bot
     * @return
     */
    public boolean isVoiceConnected(String name){
        return voiceConnected.containsKey(name);
    }

    /**
     * Méthode pour obtenir un nombre aléatoire
     *
     * @param min : Nombre minimum (inclus)
     * @param max : Nombre maximum (inclus)
     * @return number
     */
    private int random(int min, int max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
