package ch.dams333.mercure.core.bots;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.bots.voiceBot.music.MusicManager;
import ch.dams333.mercure.core.bots.voiceBot.music.MusicPlayer;
import ch.dams333.mercure.core.listener.BotListener;
import ch.dams333.mercure.core.listener.events.BotDisconnectEvent;
import ch.dams333.mercure.core.listener.events.BotReadyEvent;
import ch.dams333.mercure.utils.exceptions.VoiceException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Class of bot's object
 * @author Dams333
 * @version 1.0.0
 */
public class Bot {

    private String name;
    private String token;
    private JDA jda;
    private VoiceChannel voiceChannel;
    private MusicManager musicManager;

    public VoiceChannel getVoiceChannel() {
        return this.voiceChannel;
    }

    public String getName() {
        return this.name;
    }


    public JDA getJda() {
        return this.jda;
    }


    public Bot(String name, String token) {
        this.name = name;
        this.token = token;
        this.jda = null;
        this.voiceChannel = null;
        this.musicManager = null;
    }

    public void connectToDiscord() {
        MercureLogger.log(LogType.INFO, "Démarrage du bot " + name);
        Date date = new Date();
        try {
            jda = JDABuilder.createDefault(token).build();
            long millis = new Date().getTime() - date.getTime();
            MercureLogger.log(LogType.SUCESS, "Le bot " + name + " a démarré avec succès en " + millis + "ms");
            Mercure.INSTANCE.listenerManager.performCustomEvent(new BotReadyEvent(this));
        } catch (LoginException e) {
            MercureLogger.log("Erreur lors du démarrage du bot " + name, e);
        }
    }

    public boolean isConnectedToDiscord(){
        if(jda == null){
            return false;
        }
        return true;
    }

    public void disconnectFromDiscord(){
        if(this.jda != null){
            MercureLogger.log(LogType.INFO, "Déconnexion du bot " + name + "...");
            jda.shutdown();
            jda = null;
            MercureLogger.log(LogType.SUCESS, "Le bot " + name + " a été déconnecté");
            Mercure.INSTANCE.listenerManager.performCustomEvent(new BotDisconnectEvent(this));
        }
    }

    public void sendMessage(String message, TextChannel channel){
        jda.getTextChannelById(channel.getId()).sendMessage(message).queue();
    }

    public void changeStatus(String status) {
        jda.getPresence().setActivity(Activity.playing(status));
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Status du bot " + name + " mis à jour");
    }

    public void changePresence(String presence) {
        if(presence.equalsIgnoreCase("online")){
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence du bot " + name + " modifiée");
            return;
        }
        if(presence.equalsIgnoreCase("dnd")){
            jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence du bot " + name + " modifiée");
            return;
        }
        if(presence.equalsIgnoreCase("idle")){
            jda.getPresence().setStatus(OnlineStatus.IDLE);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence du bot " + name + " modifiée");
            return;
        }
        if(presence.equalsIgnoreCase("offline")){
            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            MercureLogger.log(MercureLogger.LogType.SUCESS, "Presence du bot " + name + " modifiée");
            return;
        }
        MercureLogger.log(MercureLogger.LogType.WARN, "Présence invalide: online | dnd | idle |offline>");
    }

    public void sendEmbed(String message, TextChannel channel){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        jda.getTextChannelById(channel.getId()).sendMessage(embedBuilder.build()).queue();
    }

    public void sendEmbed(EmbedBuilder embedBuilder, TextChannel channel){
        jda.getTextChannelById(channel.getId()).sendMessage(embedBuilder.build()).queue();
    }

	public void serialize(YAMLConfiguration yamlConfiguration) {
        yamlConfiguration.set(name, token);
	}

	public void setTriggerer() {
        this.jda.addEventListener(new BotListener(Mercure.INSTANCE));
	}

    public boolean isVocalConnected(){
        if(voiceChannel == null){
            return false;
        }else{
            return true;
        }
    }

    public void connectToVocal(VoiceChannel voiceChannel){
        VoiceChannel channel = jda.getVoiceChannelById(voiceChannel.getId());
        AudioManager audioManager = channel.getGuild().getAudioManager();
        audioManager.openAudioConnection(channel);
        this.voiceChannel = voiceChannel;
    }

    public void disconnectFromVocal() throws VoiceException {
        stopSound();
        clearTracksList();
        AudioManager audioManager = voiceChannel.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
    }

    private MusicManager getMusicManager(){
        if(musicManager != null){
            return musicManager;
        }else{
            MusicManager musicManager = new MusicManager();
            this.musicManager = musicManager;
            return musicManager;
        }
    }

    public void addTrack(String track) throws VoiceException {
        if(this.voiceChannel != null){
            getMusicManager().loadTrack(voiceChannel, track);
        }else{
            throw new VoiceException("Le bot " + name + " n'est pas connecté en vocal");
        }
    }

    @SuppressWarnings("deprecation")
    public void skipTrack() throws VoiceException {
        if(this.voiceChannel != null){
            Guild guild = voiceChannel.getGuild();

            if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
                throw new VoiceException("Il n'y a pas de piste en cours");
            }

            getMusicManager().getPlayer(guild).skipTrack();
        }else{
            throw new VoiceException("Le bot " + name + " n'est pas connecté en vocal");
        }
    }

    public void clearTracksList() throws VoiceException {
        if(this.voiceChannel != null){
            MusicPlayer player = getMusicManager().getPlayer(voiceChannel.getGuild());

            if(player.getListener().getTracks().isEmpty()){
                throw new VoiceException("Il n'y a pas de piste en attente");
            }

            player.getListener().getTracks().clear();
        }else{
            throw new VoiceException("Le bot " + name + " n'est pas connecté en vocal");
        }
    }

    public void stopSound() throws VoiceException {
        if(this.voiceChannel != null){
            getMusicManager().stopTracks(voiceChannel.getGuild());
            musicManager = null;
        }else{
            throw new VoiceException("Le bot " + name + " n'est pas connecté en vocal");
        }
    }

    public List<AudioTrack> getTracksList() throws VoiceException {
        if(this.voiceChannel != null){
            MusicPlayer player = getMusicManager().getPlayer(voiceChannel.getGuild());

            List<AudioTrack> tracks = new ArrayList<>();
            for(AudioTrack track : player.getListener().getTracks()){
                tracks.add(track);
            }
            return tracks;
        }else{
            throw new VoiceException("Le bot " + name + " n'est pas connecté en vocal");
        }
    }
}
