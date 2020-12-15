package ch.dams333.mercure.core.listener;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BotListener implements net.dv8tion.jda.api.hooks.EventListener{
    Mercure main;
    public BotListener(Mercure main) {
        this.main = main;
    }

    /**
     * Méthode exécutée lorsque un événement est détecté par le lustener
     *
     * @param event : Evénement détecté
     */
    @Override
    public void onEvent(@Nonnull GenericEvent event) {

        /*
          Test si l'événement détecté est un envoi de commande
         */
        if(event instanceof MessageReceivedEvent) {
            if (((MessageReceivedEvent) event).getAuthor().isBot()) {
                return;
            }
            if(((MessageReceivedEvent) event).getMessage().getContentDisplay().startsWith(main.commandManager.getTag())){
                main.commandManager.userCommand((MessageReceivedEvent) event);
            }
        }

        /*
          Exécutions des listeners
         */
        for(Listener listener : main.listenerManager.getListeners()){
            for(Method method : listener.getClass().getDeclaredMethods()){
                for(Annotation annotation : method.getDeclaredAnnotations()){
                    if(annotation instanceof EventHandler){
                        Class<?> parameter = method.getParameterTypes()[0];
                        if(parameter.isInstance(event)){
                            try {
                                method.invoke(listener, event);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                MercureLogger.log("Impossible d'accéder à la méthode demandée", e);
                            }
                        }
                    }
                }
            }
        }

    }

}
