package ch.dams333.mercure.core.commands.baseCommands;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.commands.utils.CommandExecutor;
import ch.dams333.mercure.core.commands.utils.MercureCommand;
import ch.dams333.mercure.core.plugins.MercurePluginInformation;
import ch.dams333.mercure.utils.exceptions.NoBotException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Date;

/**
 * Executing class of mercure command
 * @author Dams333
 * @version 1.0.0
 */
public class MercureHelpCommand implements CommandExecutor {
   /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;

    /**
     * Class' constructor
     * @param main Mercure instance
     * @since 1.0.0
     */
    public MercureHelpCommand(Mercure main) {
        this.main = main;
    }

    
    /**
     * Print the Mercure's help
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onUserCommand(MercureCommand command, User user, TextChannel textChannel, String[] args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Mercure app");
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setDescription("Mercure est un gestionnaire de bots Discord en Java qui implémente un système de plugins personnalisables grâce à une API qui s'inspire de celle de Spigot");
        embedBuilder.addField("", "Liste des plugins lancés avec Mercure :", false);
        for(MercurePluginInformation pluginInformation : main.pluginManager.getPluginInformations()){
            StringBuilder sb = new StringBuilder();
            sb.append("┣ Auteur: " + pluginInformation.getAuthor());
            sb.append("\n");
            sb.append("┣ Version: " + pluginInformation.getVersion());
            sb.append("\n");
            embedBuilder.addField(pluginInformation.getName() + " : " + pluginInformation.getDescription(), sb.toString(), false);
        }
        Date current = new Date();
        long inteval = current.getTime() - Mercure.startingDate.getTime();
        int diffDays = (int) (inteval / (24 * 60 * 60 * 1000));
        String day = "";
        if(diffDays < 10){
            day = "0" + diffDays + "j";
        }else{
            day = diffDays + "j";
        }
        int diffhours = (int) (inteval / (60 * 60 * 1000)) % 24;
        String hour = "";
        if(diffhours < 10){
            hour = "0" + diffhours + "h";
        }else{
            hour = diffhours + "h";
        }
        int diffmin = (int) (inteval / (60 * 1000)) % 60;
        String min = "";
        if(diffmin < 10){
            min = "0" + diffmin + " min";
        }else{
            min = diffmin + " min";
        }
        int diffsec = ((int) (inteval / (1000))) % 60;
        String sec = "";
        if(diffsec < 10){
            sec = "0" + diffsec + " sec";
        }else{
            sec = diffsec + " sec";
        }
        embedBuilder.addField("En cours d'éxecution depuis :", day + " " + hour + " " + min + " " + sec, false);
        embedBuilder.addField("", "Mercure a été créé par Dams333", false);
        try {
            main.botsManager.getRandomBot().getTextChannelById(textChannel.getId()).sendMessage(embedBuilder.build()).queue();
        } catch (NoBotException e) {
            MercureLogger.log("Impossible d'envoyer le message", e);
        }
        return false;
    }

    
    /**
     * @see CommandExecutor
     * @since 1.0.0
     */
    @Override
    public boolean onConsoleCommand(MercureCommand command, String[] args) {
        return false;
    }
}
