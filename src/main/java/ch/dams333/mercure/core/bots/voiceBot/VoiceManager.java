package ch.dams333.mercure.core.bots.voiceBot;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.voiceBot.music.MusicManager;
import ch.dams333.mercure.core.bots.voiceBot.music.MusicPlayer;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.exceptions.VoiceException;
import ch.dams333.mercure.utils.logger.MercureLogger;
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

public class VoiceManager {

    private Mercure main;

    public VoiceManager(Mercure main) {
        this.main = main;
        managers = new HashMap<>();
    }

    private Map<String, MusicManager> managers;

    /**
     * Métode pour récupérer le MusicManager d'un bot précis
     * @param name : Nom du bot
     * @return MusicManager associé (s'il n'existe pas, il sera créé)
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
     * Méthode pour récupérer le premier résultat de recherche sur Youtube
     *
     * @param search : Mots-clé de la recherche
     * @return Youtube URL
     * @throws IOException
     * @throws ParseException
     */
    public String youtubeSearch(String search) throws IOException, ParseException {
        String keyword = search.replace(" ", "+");

        String link = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=rating&q=" + keyword + "&key=AIzaSyDCIs9TJ8GrGmzTpRllP8_6ai5tATnJPuQ";

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
     * Méthode pour ajouter une piste audio à la file d'attent d'un bot
     *
     * @param name : Nom du bot
     * @param track : AudioTrack à ajouter
     * @param textChannel : Channel d'où provient l'action (nécessaire pour récupérer le bot sur le serveur)
     * @throws NoBotException
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
     * @param channel : Channel d'où provient l'action (nécessaire pour récupérer le bot sur le serveur)
     * @throws NoBotException
     * @throws VoiceException
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
     * @param channel : Channel d'où provient l'action (nécessaire pour récupérer le bot sur le serveur)
     * @throws NoBotException
     * @throws VoiceException
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
     * @param channel : Channel d'où provient l'action (nécessaire pour récupérer le bot sur le serveur)
     * @throws NoBotException
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
