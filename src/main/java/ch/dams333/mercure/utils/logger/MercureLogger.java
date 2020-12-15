package ch.dams333.mercure.utils.logger;

import ch.dams333.mercure.Mercure;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MercureLogger {

    /**
     * Méthode pour logger un message en console
     *
     * @param logType : Type de log
     * @param message : Message à logger
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
     * Méthode pour logger une erreur en console
     *
     * @param message : Message à logger
     * @param e : Erreur à logger (uniquement si le paramètre de démarrage est "debug")
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
     * Types de log
     */
    public enum LogType{
        INFO("INFO", ConsoleColors.CYAN),
        SUB_INFO("", ConsoleColors.RESET),
        DEBUG("DEBUG", ConsoleColors.PURPLE),
        SUCESS("SUCESS", ConsoleColors.GREEN),
        ERROR("ERROR", ConsoleColors.RED);

        private String name;
        private String color;

        LogType(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }
    }
}
