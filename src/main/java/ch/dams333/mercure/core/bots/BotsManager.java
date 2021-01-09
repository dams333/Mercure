package ch.dams333.mercure.core.bots;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import java.util.*;

/**
 * All basing bots' comportment
 * @author Dams333
 * @version 2.0.0
 */
public class BotsManager{
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;

    private List<Bot> bots;

    /**
     * Class' constructor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public BotsManager(Mercure mercure) {
        this.main = mercure;
        bots = new ArrayList<>();
    }

    public List<Bot> getBots() {
        return this.bots;
    }

    /**
     * Is a bot for this name in this Mercure instance
     * @param name Name of the bot
     * @return Boolean
     * @since 1.0.0
     */
    public boolean isBotByName(String name) {
        for(Bot bot : this.bots){
            if(bot.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Save a bot in Mercure
     * @param name Name needs to be gived to the bot
     * @param token Bot's token
     * @since 1.0.0
     */
    public void registerBot(String name, String token) {
        Bot bot = new Bot(name, token);
        this.bots.add(bot);
        serializeBots();
    }

    /**
     * Save all bots in bots.yml file
     * @since 1.0.0
     */
    private void serializeBots() {

        YAMLConfiguration yamlConfiguration = new YAMLConfiguration(new LinkedHashMap<String, Object>());

        MercureLogger.log(MercureLogger.LogType.DEBUG, "Serialization de " + bots.size() + " bots");

        for(Bot bot : this.bots){
            bot.serialize(yamlConfiguration);
        }

        yamlConfiguration.save("bots.yml");

        MercureLogger.log(MercureLogger.LogType.INFO, "Serialization des bots terminée");
    }

    /**
     * Load all bots from bots.yml file
     * @since 1.0.0
     */
    public void deserializeBots() {
        YAMLConfiguration yamlConfiguration = YAMLConfiguration.load("bots.yml");
        MercureLogger.log(MercureLogger.LogType.DEBUG, "Déserialization de " + yamlConfiguration.getKeys(false).size() + " bots...");
        for(String name : yamlConfiguration.getKeys(false)){
            Bot bot = new Bot(name, yamlConfiguration.getString(name));
            this.bots.add(bot);
        }
        MercureLogger.log(MercureLogger.LogType.INFO, bots.size() + " bots chargés par Mercure");
    }

    /**
     * Start a bot and connecte him to Discord
     * @param name Bot's name
     * @throws NoBotException
     * @since 2.0.0
     */
    public void connect(String name) throws NoBotException {
        Bot bot = getBot(name);
        bot.connectToDiscord();
    }

    /**
     * Disconnect all bots from Discord
     * @since 2.0.0
     */
    public void disconnectAllBots() {
        for(Bot bot : this.bots){
            bot.disconnectFromDiscord();
        }
    }

    /**
     * Delete a bot from Mercure
     * 
     * @param name Bot's name
     * @throws NoBotException
     * @since 1.0.0
     */
    public void removeBot(String name) throws NoBotException {
        Bot bot = getBot(name);
        this.bots.remove(bot);
        if(bot.isConnectedToDiscord()){
            bot.disconnectFromDiscord();
        }
    }


    /**
     * Get a random bot managed by Mercure
     * @return Bot's JDA
     * @throws NoBotException There is no conneted bots to Discord
     * @since 1.0.0
     */
    public Bot getRandomBot(Boolean needToBeConnectedToDiscord) throws NoBotException {
        if(bots.size() <= 0){
            throw new NoBotException("Impossible de trouver un bot car Mercure n'en gère aucun");
        }
        int index = random(0, bots.size() - 1);
        int firstTested = index;
        Bot bot = bots.get(index);
        if(needToBeConnectedToDiscord){
            while(!bot.isConnectedToDiscord()){
                index++;
                if(index >= bots.size()) index = 0;
                if(index == firstTested){
                    throw new NoBotException("Impossible de trouver un bot connecté à Discord");
                }
                bot = bots.get(index);
            }
        }
        return bot;
    }

    /**
     * Get a Mercure's managed bot
     * @param name Bot's name
     * @return Bot's object
     * @throws NoBotException There is no bot with this name
     * @since 1.0.0
     */
    public Bot getBot(String name) throws NoBotException{
        for(Bot bot : this.bots){
            if(bot.getName().equalsIgnoreCase(name)){
                return bot;
            }
        }
        throw new NoBotException("Il n'y a pas de bot géré par Mercure portant ce nom");
    }

    public Bot getBotReadyToConnectToVocal() throws NoBotException {
        List<Bot> readyBots = new ArrayList<>();
        for(Bot bot : this.bots){
            if(bot.isConnectedToDiscord()){
                if(!bot.isVocalConnected()){
                    readyBots.add(bot);
                }
            }
        }
        if(readyBots.size() <= 0){
            throw new NoBotException("Il n'y a aucun bot disponible pour se connecter en vocal");
        }
        return readyBots.get(random(0, readyBots.size() - 1));
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
