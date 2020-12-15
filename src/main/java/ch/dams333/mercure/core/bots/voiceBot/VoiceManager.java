package ch.dams333.mercure.core.bots.voiceBot;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.voiceBot.music.MusicManager;
import ch.dams333.mercure.core.bots.voiceBot.music.MusicPlayer;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.exceptions.VoiceException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Util class for the gestion of the bots' vocal's interactions
 * @author Dams333
 * @version 1.0.0
 */
public class VoiceManager {

    /**
     * Main class instance
     * @since 1.0.0
     */
    private Mercure main;
    /**
     * Saving MusicManager for bot map
     * @since 1.0.0
     */
    private Map<String, MusicManager> managers;

    /**
     * Class' constructor
     * @param main Mercure instance
     * @since 1.0.0
     */
    public VoiceManager(Mercure main) {
        this.main = main;
        managers = new HashMap<>();
    }

    /**
     * Get the MusicBot assoicated to a bot
     * @param name Bot's name
     * @return Associated MusicManager (If it does not exist, it will be created)
     * @since 1.0.0
     */
    private MusicManager getManager(String name){
        if(managers.containsKey(name)){
            return managers.get(name);
        }else{
            MusicManager musicManager = new MusicManager();
            MercureLogger.log(MercureLogger.LogType.DEBUG, "created");
            managers.put(name, musicManager);
            return getManager(name);
        }
    }

    /**
     * Get the first resul of a youtub search
     * @param search KeyWords of the search
     * @return Youtube URL
     * @throws IOException Impossible to execute the API request
     * @throws ParseException Impossible to read the result of the request
     * @since 1.0.0
     */
    public String youtubeSearch(String search) throws IOException, ParseException {
        String keyword = search.replace(" ", "+");

        YAMLConfiguration secret = new YAMLConfiguration(ClassLoader.getSystemResourceAsStream("ch/dams333/mercure/secret.yml"));

        String link = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=rating&q=" + keyword + "&key=" + secret.getString("googleAPI");

        try {

            URL url = new URL(link);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                scanner.close();

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                JSONArray itemsArray = (JSONArray) data_obj.get("items");
                JSONObject itemsObj = (JSONObject) itemsArray.get(0);

                JSONObject idObj = (JSONObject) itemsObj.get("id");

                return  "https://www.youtube.com/watch?v=" + idObj.get("videoId");

            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Add an AudioTrack to the playing list of a bot
     * @param name Bot's name
     * @param track AudioTrack to add
     * @param textChannel A channel of the guild (needed to get the bot)
     * @throws NoBotException The bot is not reachable
     * @since 1.0.0
     */
    public void addTrack(String name, String track, TextChannel textChannel) throws NoBotException {
        if(main.botsManager.isBotByName(name)){
            if(main.botsManager.isConnected(name)){
                if(main.botsManager.isVoiceConnected(name)){

                    JDA jda = main.botsManager.getBot(name);
                    TextChannel channel = jda.getTextChannelById(textChannel.getId());

                    getManager(name).loadTrack(channel, track.replaceAll(" ", ""));
                    return;
                }
                throw new NoBotException("Le bot " + name + " n'est pas connecté en vocal");
            }
            throw new NoBotException("Le bot " + name + " n'est pas connecté");
        }
        throw new NoBotException("Le bot " + name + " n'existe pas");
    }

    /**
     * Méthode pour sauter la piste audio actuelle
     *
     * @param name : Nom du bot
     * @param channel A channel of the guild (needed to get the bot)
     * @throws NoBotExceptionThe The bot is not reachable
     * @throws VoiceException There is nos track to skip
     * @since 1.0.0
     */
    @SuppressWarnings("deprecation")
    public void skipTrack(String name, TextChannel channel) throws NoBotException, VoiceException {
        if(main.botsManager.isBotByName(name)){
            if(main.botsManager.isConnected(name)){
                if(main.botsManager.isVoiceConnected(name)){

                    JDA jda = main.botsManager.getBot(name);
                    Guild guild = jda.getTextChannelById(channel.getId()).getGuild();

                    if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
                        throw new VoiceException("Il n'y a pas de piste en cours");
                    }

                    getManager(name).getPlayer(guild).skipTrack();
                    return;

                }
                throw new NoBotException("Le bot " + name + " n'est pas connecté en vocal");
            }
            throw new NoBotException("Le bot " + name + " n'est pas connecté");
        }
        throw new NoBotException("Le bot " + name + " n'existe pas");
    }

    /**
     * Vider la liste d'attente du bot
     *
     * @param name : Nom du bot
     * @param channel A channel of the guild (needed to get the bot)
     * @throws NoBotExceptionThe The bot is not reachable
     * @throws VoiceException There is no track in waiting list
     * @since 1.0.0
     */
    public void clearTracks(String name, TextChannel channel) throws NoBotException, VoiceException {
        if(main.botsManager.isBotByName(name)){
            if(main.botsManager.isConnected(name)){
                if(main.botsManager.isVoiceConnected(name)){

                    JDA jda = main.botsManager.getBot(name);
                    TextChannel textChannel = jda.getTextChannelById(channel.getId());
                    MusicPlayer player = getManager(name).getPlayer(textChannel.getGuild());

                    if(player.getListener().getTracks().isEmpty()){
                        throw new VoiceException("Il n'y a pas de piste en attente");
                    }

                    player.getListener().getTracks().clear();
                    return;

                }
                throw new NoBotException("Le bot " + name + " n'est pas connecté en vocal");
            }
            throw new NoBotException("Le bot " + name + " n'est pas connecté");
        }
        throw new NoBotException("Le bot " + name + " n'existe pas");
    }

    /**
     * Méthode pour stopper la musique d'un bot
     *
     * @param name : Nom du bot
     * @param channel A channel of the guild (needed to get the bot)
     * @throws NoBotException The bot is not reachable
     * @since 1.0.0
     */
    public void stopMusic(String name, TextChannel channel) throws NoBotException {
        if(main.botsManager.isBotByName(name)){
            if(main.botsManager.isConnected(name)){
                if(main.botsManager.isVoiceConnected(name)){

                    JDA jda = main.botsManager.getBot(name);

                    getManager(name).stopTracks(jda.getTextChannelById(channel.getId()).getGuild());
                    this.managers.remove(name);
                    return;

                }
                throw new NoBotException("Le bot " + name + " n'est pas connecté en vocal");
            }
            throw new NoBotException("Le bot " + name + " n'est pas connecté");
        }
        throw new NoBotException("Le bot " + name + " n'existe pas");
    }

}
