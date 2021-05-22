package ch.dams333.mercure.core.bots.voiceBot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;

/**
 * Util class for LavaPlayer
 * @author NeutronStar
 * @version 1.0.0
 */
public class MusicPlayer {

    private final AudioPlayer audioPlayer;
    private final AudioListener listener;
    private final Guild guild;

    public MusicPlayer(AudioPlayer audioPlayer, Guild guild){
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        listener = new AudioListener(this);
        audioPlayer.addListener(listener);
    }

    
    /** 
     * @return AudioPlayer
     */
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    
    /** 
     * @return Guild
     */
    public Guild getGuild() {
        return guild;
    }

    
    /** 
     * @return AudioListener
     */
    public AudioListener getListener() {
        return listener;
    }

    
    /** 
     * @return AudioHandler
     */
    public AudioHandler getAudioHandler(){
        return new AudioHandler(audioPlayer);
    }

    
    /** 
     * @param track
     */
    public synchronized void playTrack(AudioTrack track){
        listener.queue(track);
    }

    public synchronized void skipTrack(){
        listener.nextTrack();
    }
}