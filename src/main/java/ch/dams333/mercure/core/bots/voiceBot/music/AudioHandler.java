package ch.dams333.mercure.core.bots.voiceBot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

/**
 * Util class for LavaPlayer
 * @author NeutronStar
 * @version 1.0.0
 */
public class AudioHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }


    
    /** 
     * @return boolean
     */
    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    
    /** 
     * @return ByteBuffer
     */
    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    
    /** 
     * @return boolean
     */
    @Override
    public boolean isOpus() {
        return true;
    }

}
