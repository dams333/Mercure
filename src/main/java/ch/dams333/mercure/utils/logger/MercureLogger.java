package ch.dams333.mercure.utils.logger;

import ch.dams333.mercure.Mercure;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger system for Mercure's console
 * @author Dams333
 * @version 1.1.0
 */
public class MercureLogger {

    /**
     * Log a message in console
     * @param logType Type of log
     * @param message Message to log
     * @since 1.0.0
     */
    public static void log(LogType logType, String message){

        if(logType == LogType.SUB_INFO){
            System.out.println("       " + message);
        }else {
            Date date = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

            if(logType == LogType.DEBUG){
                if(!Mercure.consoleType.equalsIgnoreCase("debug")){
                    return;
                }
            }

            System.out.println("[" + formater.format(date) + "] " + logType.getColor() + logType.getName() + ConsoleColors.RESET + " - " + ConsoleColors.RESET + message);
        }
    }

    /**
     * Log an error in console
     * @param message Message to log
     * @param e Error to log (only if console type is on 'debug')
     * @since 1.0.0
     */
    public static void log(String message, Exception e){
        Date date = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

        System.out.println("[" + formater.format(date) + "] " + LogType.ERROR.getColor() + LogType.ERROR.getName() + " - " + ConsoleColors.RESET + message);

        if(Mercure.consoleType.equalsIgnoreCase("debug")){
            e.printStackTrace();
        }
    }

    /**
     * Log a message in console from plugin
     * @param pluginName Name of the plugin that log this message
     * @param logType Type of log
     * @param message Message to log
     * @since 1.1.0
     */
    public static void log(String pluginName, LogType logType, String message){

        if(logType == LogType.SUB_INFO){
            System.out.println("       " + message);
        }else {
            Date date = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

            if(logType == LogType.DEBUG){
                if(!Mercure.consoleType.equalsIgnoreCase("debug")){
                    return;
                }
            }

            System.out.println("[" + formater.format(date) + "] " + logType.getColor() + logType.getName() + " - " + ConsoleColors.BLUE + pluginName + ConsoleColors.RESET + " - " + message);
        }
    }

    /**
     * Log an error in console from plugin
     * @param pluginName Name of the plugin that log this message
     * @param message Message to log
     * @param e Error to log (only if console type is on 'debug')
     * @since 1.1.0
     */
    public static void log(String pluginName, String message, Exception e){
        Date date = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

        System.out.println("[" + formater.format(date) + "] " + LogType.ERROR.getColor() + LogType.ERROR.getName() + " - " + ConsoleColors.BLUE + pluginName + ConsoleColors.RESET + " - " + message);

        if(Mercure.consoleType.equalsIgnoreCase("debug")){
            e.printStackTrace();
        }
    }


    /**
     * Enumeration of all types of log
     * @since 1.0.0
     */
    public enum LogType{
        /**
         * Log of a classic information
         * @since 1.0.0
         */
        INFO("INFO", ConsoleColors.CYAN),
         /**
         * Log of a sub information
         * @since 1.0.0
         */
        SUB_INFO("", ConsoleColors.RESET),
        /**
         * Log of a debug message (only if console type is on 'debug')
         * @since 1.0.0
         */
        DEBUG("DEBUG", ConsoleColors.PURPLE),
        /**
         * Log of success message
         * @since 1.0.0
         */
        SUCESS("SUCESS", ConsoleColors.GREEN),
        /**
         * Log of an error message
         * @since 1.0.0
         */
        ERROR("ERROR", ConsoleColors.RED);

        /**
         * Name of log's type
         * @since 1.0.0
         */
        private String name;
        /**
         * Color of log
         * @since 1.0.0
         */
        private String color;

        /**
         * Class' constructor
         * @param name Name of log's type
         * @param color Color of log
         * @since 1.0.0
         */
        LogType(String name, String color) {
            this.name = name;
            this.color = color;
        }

        /**
         * Get the name of the log's type
         * @return String
         * @since 1.0.0
         */
        public String getName() {
            return name;
        }

        /**
         * Get the color of the log
         * @return String
         * @since 1.0.0
         */
        public String getColor() {
            return color;
        }
    }
}
