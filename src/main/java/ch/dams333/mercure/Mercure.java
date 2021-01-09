package ch.dams333.mercure;

import ch.dams333.mercure.core.bots.BotsManager;
import ch.dams333.mercure.core.commands.utils.CommandManager;
import ch.dams333.mercure.core.commands.utils.MercureCommandFileParser;
import ch.dams333.mercure.core.listener.ListenerManager;
import ch.dams333.mercure.core.plugins.PluginManager;
import ch.dams333.mercure.core.plugins.pluginInteractions.PluginInteractionsManager;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Dams333
 * @version 1.0.0
 * @since 1.0.0
 */
public class Mercure implements Runnable {

    /**
     * Type of logger in console
     * @since 1.0.0
     */
    public static String consoleType = "";
    /**
     * Date at Mercure started
     * @since 1.0.0
     */
    public static Date startingDate;
    /**
     * Instance of Mercure class
     * @since 1.0.0
     */
    public static Mercure INSTANCE;
    /**
     * Is Mercure rnning
     * @since 1.0.0
     */
    private boolean running;
    /**
     * Console scanner
     * @since 1.0.0
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Mercure's commands' manager
     * @since 1.0.0
     */
    public CommandManager commandManager;
    /**
     * Bots' manager for connexions, serialzing and vocal
     * @since 1.0.0
     */
    public BotsManager botsManager;
    /**
     * Listener's classes' manager for registering and selecting bot
     * @since 1.0.0
     */
    public ListenerManager listenerManager;
    /**
     * Plugins' manager for loading and unloading
     * @since 1.0.0
     */
    public PluginManager pluginManager;
    /**
     * Plugins' manager for interactions between plugins' files and Mercure instance
     * @since 1.0.0
     */
    public PluginInteractionsManager registerManager;
    /**
     * Class for deserialization of YAML files contains commands' informations
     * @since 1.0.0
     */
    public MercureCommandFileParser mercureCommandFileParser;

    /**
     * Starting method of Mercure
     * @param args Java starting command's parameters
     * @since 1.0.0
     */
    public static void main(String[] args) {
        try {
            Mercure mercure = new Mercure();
            if(args.length > 0){
                Mercure.consoleType = args[0];
            }

            Mercure.startingDate = new Date();
            MercureLogger.log(MercureLogger.LogType.INFO, "Démarrage de Mercure...");

            new Thread(mercure, "Mercure").start();
        } catch (IllegalArgumentException e) {
            MercureLogger.log(MercureLogger.LogType.ERROR, "Erreur lors du démarrage de Mercure");
            e.printStackTrace();
        }

    }

    /**
     * Mercure running metod
     * @since 1.0.0
     */
    public void run() {

        INSTANCE = this;

        registerManager = new PluginInteractionsManager(this);
        commandManager = new CommandManager(this);
        botsManager = new BotsManager(this);
        listenerManager = new ListenerManager(this);
        mercureCommandFileParser = new MercureCommandFileParser(this);
        mercureCommandFileParser.parse();
        pluginManager = new PluginManager(this);


        try {
            pluginManager.loadPlugins();
        } catch (Exception e) {
            MercureLogger.log("Impossible de charger les plugins", e);
        }


        pluginManager.enablePlugins();
        commandManager.registerBaseCommands();
        botsManager.deserializeBots();


        running = true;
        long millis = new Date().getTime() - startingDate.getTime();
        MercureLogger.log(MercureLogger.LogType.SUCESS, "Mercure a démarré, en " + millis + "ms");


        while (running){
            if(scanner.hasNextLine()) {
                commandManager.performConsoleCommand(scanner.nextLine());
            }
        }


        MercureLogger.log(MercureLogger.LogType.INFO, "Mercure s'éteint...");

        pluginManager.disablePlugins();
        botsManager.disconnectAllBots();

        MercureLogger.log(MercureLogger.LogType.SUCESS, "Mercure éteint avec succès");
        System.exit(0);
    }

    /**
     * Clean stop of Mercure
     * @since 1.0.0
     */
    public void stop() {
        running = false;
    }

    /**
     * Getting the pirncipal Mercure's configuration file
     * @return YAML file
     * @since 1.0.0
     */
    public YAMLConfiguration getConfig(){
        File file = new File("config.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                MercureLogger.log("Impossible de créer le fichier de config", e);
            }
        }
        return YAMLConfiguration.load("config.yml");
    }

}
