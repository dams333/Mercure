package ch.dams333.mercure.core.listener;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.logger.MercureLogger;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base listener of JDA's events
 * @author Dams333
 * @version 1.0.1
 */
public class BotListener implements net.dv8tion.jda.api.hooks.EventListener{
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;

    /**
     * Self instance
     * @since 1.0.0
     */
    public static BotListener INSTANCE;

    /**
     * Class' constructor
     * @param main Mercure instance
     * @since 1.0.0
     */
    public BotListener(Mercure main) {
        this.main = main;
        INSTANCE = this;
    }

    /**
     * An event is activated
     * Util for command detecting and Listeners
     * @see Listener
     * @param event Detected event
     * @since 1.0.0
     */
    @Override
    public void onEvent(@Nonnull GenericEvent event) {

        if(event instanceof MessageReceivedEvent) {
            if (((MessageReceivedEvent) event).getAuthor().isBot()) {
                return;
            }
            if(((MessageReceivedEvent) event).getMessage().getContentDisplay().startsWith(main.commandManager.getTag())){
                main.commandManager.userCommand((MessageReceivedEvent) event);
                return;
            }
        }

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
