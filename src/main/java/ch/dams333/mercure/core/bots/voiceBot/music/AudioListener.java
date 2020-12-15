package ch.dams333.mercure.core.bots.voiceBot.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * Util class for LavaPlayer
 * @author NeutronStar
 * @version 1.0.0
 */
public class AudioListener extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
    private final MusicPlayer player;

    public AudioListener(MusicPlayer player){
        this.player = player;
    }

    
    /** 
     * @return BlockingQueue<AudioTrack>
     */
    public BlockingQueue<AudioTrack> getTracks() {
        return tracks;
    }

    
    /** 
     * @return int
     */
    public int getTrackSize(){
        return tracks.size();
    }

    public void nextTrack(){
        if(tracks.isEmpty()){
            if(player.getGuild().getAudioManager().getConnectedChannel() != null)
                player.getGuild().getAudioManager().closeAudioConnection();
            return;
        }
        player.getAudioPlayer().startTrack(tracks.poll(), false);
    }

    
    /** 
     * @param player
     * @param track
     * @param endReason
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) nextTrack();
    }

    
    /** 
     * @param track
     */
    public void queue(AudioTrack track) {
        if (!player.getAudioPlayer().startTrack(track, true)) tracks.offer(track);
    }

}
