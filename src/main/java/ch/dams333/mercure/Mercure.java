package ch.dams333.mercure;

import ch.dams333.mercure.core.bots.BotsManager;
import ch.dams333.mercure.core.bots.voiceBot.VoiceManager;
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

public class Mercure implements Runnable {

    public static String consoleType = "";
    public static Date startingDate;
    public static Mercure selfInstance;
    private boolean running;
    private final Scanner scanner = new Scanner(System.in);

    public CommandManager commandManager;
    public BotsManager botsManager;
    public ListenerManager listenerManager;
    public PluginManager pluginManager;
    public PluginInteractionsManager registerManager;
    public MercureCommandFileParser mercureCommandFileParser;
    public VoiceManager voiceManager;

    /**
     * Méthode de démarrage de Mercure
     *
     * @param args : Argument de la commande java de démarrage (""/"debug")
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
     * Thread de Mercure
     */
    public void run() {

        selfInstance = this;

        registerManager = new PluginInteractionsManager(this);
        commandManager = new CommandManager(this);
        botsManager = new BotsManager(this);
        listenerManager = new ListenerManager(this);
        mercureCommandFileParser = new MercureCommandFileParser(this);
        mercureCommandFileParser.parse();
        pluginManager = new PluginManager(this);
        voiceManager = new VoiceManager(this);


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
     * Méthode d'arrêt de mercure
     */
    public void stop() {
        running = false;
    }

    /**
     * Méthode pour récupérer le fichier de config de Mercure
     *
     * @return Fichier YAML
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
