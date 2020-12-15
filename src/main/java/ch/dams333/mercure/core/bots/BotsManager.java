package ch.dams333.mercure.core.bots;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.security.auth.login.LoginException;
import java.util.*;

/**
 * All basing bots' comportment
 * @author Dams333
 * @version 1.0.0
 */
public class BotsManager {
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;
    /**
     * Map of the bots' tokens by there name
     * @since 1.0.0
     */
    private Map<String, String> botsTokens;
    /**
     * List, for all bots' names, that is started and connected to Discord
     * @since 1.0.0
     */
    private Map<String, Boolean> isConnected;
    /**
     * Map of the bots' tokens (only if they are Discord connected)
     * @since 1.0.0
     */
    private Map<String, JDA> jdas;
    /**
     * Map of the bot's VoiceChannel where they are connected (only if they are connected)
     * @since 1.0.0
     */
    private Map<String, VoiceChannel> voiceConnected;

    /**
     * Class' constructor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public BotsManager(Mercure mercure) {
        this.main = mercure;
        botsTokens = new HashMap<>();
        isConnected = new HashMap<>();
        jdas = new HashMap<>();
        voiceConnected = new HashMap<>();
    }

    /**
     * Is a bot for this name in this Mercure instance
     * @param name Name of the bot
     * @return Boolean
     * @since 1.0.0
     */
    public boolean isBotByName(String name) {
        return botsTokens.keySet().contains(name);
    }

    /**
     * Save a bot in Mercure
     * @param name Name needs to be gived to the bot
     * @param token Bot's token
     * @since 1.0.0
     */
    public void registerBot(String name, String token) {
        botsTokens.put(name, token);
        isConnected.put(name, false);
        this.serializeBots();
    }

    /**
     * Get all bots tokens by name
     * @return Map with bot's name in key and token in value
     * @since 1.0.0
     */
    public Map<String, String> getBotsTokens(){
        return botsTokens;
    }

    /**
     * Save all bots in bots.yml file
     * @since 1.0.0
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
     * Load all bots from bots.yml file
     * @since 1.0.0
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
     * Is this bot started and connected to Discord
     * @param name Bot's name
     * @return Boolean
     * @since 1.0.0
     */
    public boolean isConnected(String name) {
        return isConnected.get(name);
    }

    /**
     * Start a bot and connecte him to Discord
     * @param name Bot's name
     * @since 1.0.0
     */
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
     * Disconnect all bots from Discord
     * @since 1.0.0
     */
    public void disconnectAllBots() {
        for(String name : jdas.keySet()){
            disconnect(name, false);
        }
    }

    /**
     * Disconnect a bot from Discord
     * @param name Bot's name
     * @param replacing If he's the listener then he will be replaced
     * @see ListenerManager
     * @since 1.0.0
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
     * Delete a bot from Mercure
     * @param name Bot's name
     * @since 1.0.0
     */
    public void removeBot(String name) {
        this.botsTokens.remove(name);
        this.isConnected.remove(name);
        this.jdas.remove(name);
        serializeBots();
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Bot " + name + " supprimé ");
    }

    /**
     * Change bot's status' message
     * @param name Bot's name
     * @param status Status
     * @since 1.0.0
     */
    public void updateStatus(String name, String status) {
        JDA jda = jdas.get(name);
        jda.getPresence().setActivity(Activity.playing(status));
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Status du bot mis à jour");
    }

    /**
     * Change bot's presence
     * @param name Bot's name
     * @param presence : online/dnd/idle/offline
     * @since 1.0.0
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
     * Get a random bot managed by Mercure
     * @return Bot's JDA
     * @throws NoBotException There is no conneted bots to Discord
     * @since 1.0.0
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
     * Get a Mercure's managed bot
     * @param name Bot's name
     * @return Bot's JDA
     * @throws NoBotException There is no bot with this name
     * @since 1.0.0
     */
    public JDA getBot(String name) throws NoBotException{
        if(this.jdas.keySet().contains(name)){
            return this.jdas.get(name);
        }else{
            throw new NoBotException("Impossible de récupérer un bot portant ce nom");
        }
    }

    /**
     * Connect a bot to a VoiceChannel
     * @param name Bot's name
     * @param voiceChannel Channel to connect
     * @throws NoBotException This bot is not connected to Discord
     * @since 1.0.0
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
     * Disconnect a bot from his VoiceChannel
     * @param name Bot's name
     * @param textChannel A channel of the guild (needed to get the bot)
     * @throws NoBotException This bot is not connected to Discord
     * @since 1.0.0
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
     * Is a bot in a VoiceChannel
     * @param name Bot's name
     * @return Boolean
     * @since 1.0.0
     */
    public boolean isVoiceConnected(String name){
        return voiceConnected.containsKey(name);
    }

    /**
     * Util method to get a random number
     * @param min Maximum (include)
     * @param max :Minimum (include)
     * @return Integer in range
     * @since 1.0.0
     */
    private int random(int min, int max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
