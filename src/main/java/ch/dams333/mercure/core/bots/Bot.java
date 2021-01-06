package ch.dams333.mercure.core.bots;

import java.util.Date;

import javax.security.auth.login.LoginException;

import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Class of bot's object
 * @author Dams333
 * @version 1.0.0
 */
public class Bot {

    private String name;
    private String token;
    private JDA jda;

    public String getName() {
        return this.name;
    }

    public Bot(String name, String token) {
        this.name = name;
        this.token = token;
        this.jda = null;
    }

    public void connectToDiscord() {
        MercureLogger.log(LogType.INFO, "Démarrage du bot " + name);
        Date date = new Date();
        try {
            jda = JDABuilder.createDefault(token).build();
            long millis = new Date().getTime() - date.getTime();
            MercureLogger.log(LogType.SUCESS, "Le bot " + name + " a démarré avec succès en " + millis + "ms");
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
        MercureLogger.log(LogType.INFO, "Déconnexion du bot " + name + "...");
        jda.shutdown();
        jda = null;
        MercureLogger.log(LogType.SUCESS, "Le bot " + name + " a été déconnecté");
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

}
