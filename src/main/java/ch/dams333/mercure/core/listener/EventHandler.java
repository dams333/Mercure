package ch.dams333.mercure.core.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used by the methods in Listeners
 * @see Listener
 * @author Dams333
 * @version 1.0.0
 */
@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
}
