package ch.dams333.mercure.core.bots.voiceBot.music;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.dams333.mercure.utils.exceptions.VoiceException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;


public class MusicManager {

    private AudioPlayerManager manager;
    private Map<String, MusicPlayer> players;

    public MusicManager(){
        MercureLogger.log(MercureLogger.LogType.DEBUG, "CREATING");
        manager = new DefaultAudioPlayerManager();
        players = new HashMap<>();
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
        MercureLogger.log(MercureLogger.LogType.DEBUG, "YOLO");
    }

    public synchronized MusicPlayer getPlayer(Guild guild){
        if(!players.containsKey(guild.getId())) players.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
        return players.get(guild.getId());
    }

    public void loadTrack(final TextChannel channel, final String source){

        MusicPlayer player = getPlayer(channel.getGuild());
        channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());

        manager.loadItemOrdered(player, source, new AudioLoadResultHandler(){

            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for(int i = 0; i < playlist.getTracks().size() && i < 5; i++){
                    AudioTrack track = playlist.getTracks().get(i);
                    player.playTrack(track);
                }
            }

            @Override
            public void noMatches() {
                try {
                    throw new VoiceException("Impossible de trouver cette piste sonore");
                } catch (VoiceException e) {
                    MercureLogger.log("Impossible de lancer cette piste", e);
                }
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                throw exception;
            }
        });

    }

    public void stopTracks(Guild guild) {
        getPlayer(guild).getAudioPlayer().stopTrack();
    }
}