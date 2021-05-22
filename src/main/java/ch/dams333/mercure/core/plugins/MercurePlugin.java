package ch.dams333.mercure.core.plugins;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.plugins.pluginInteractions.PluginInteractionsManager;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;

import java.io.File;
import java.sql.PreparedStatement;

/**
 * Abstract class of all Mercure's plugins
 * @see MercurePlugin
 * @author Dams333
 * @version 1.0.2
 */
public abstract class MercurePlugin {

    /**
     * Mercure instance
     * @since 1.0.0
     */
    private Mercure main;
    /**
     * Plugin's name
     * @since 1.0.0
     */
    private String name = "";

    /**
     * Class' constructor
     * @since 1.0.0
     */
    public MercurePlugin() {
        main = Mercure.INSTANCE;
    }

    /**
     * Plugin is loaded by Mercure
     * @since 1.0.0
     */
    public void onLoad(){}
    /**
     * Plugin is enabled by Mercure
     * @since 1.0.0
     */
    public void onEnable(){}
    /**
     * Plugin is disabled by Mercure
     * @since 1.0.0
     */
    public void onDisable(){}

    /**
     * Define plugin's name
     * @since 1.0.0
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get plugins' interactions' manager
     * @return PluginInteractionsManager
     * @since 1.0.0
     */
    public PluginInteractionsManager getPluginManager(){
        return main.registerManager;
    }

    /**
     * Get the config file of this plugin
     * @return YAMLConfiguration
     * @since 1.0.0
     */
    public YAMLConfiguration getConfig(){
        return YAMLConfiguration.load(getPluginDirectory() + File.separator + "config.yml");
    }

    /**
     * Get the plugin's files' directory ogf this plugin
     * @return Directory
     * @since 1.0.0
     */
    public File getPluginDirectory(){
        File file = new File("plugins" + File.separator + name + File.separator);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    /**
     * Save the base config of this plugin
     * @param config : Config à sauvegarder
     * @since 1.0.0
     */
    public void saveConfig(YAMLConfiguration config){
        config.save(getPluginDirectory() + File.separator + "config.yml");
    }

    /**
     * Log a message in console
     * @param logType Type of log
     * @param message Message to log
     * @since 1.0.1
     */
    public void log(LogType logType, String message){
        MercureLogger.log(this.name, logType, message);
    }

    /**
     * Log an error in console
     * @param message Message to log
     * @param e Error to log (only inf console type is 'debug')
     * @since 1.0.1
     */
    public void log(String message, Exception e){
        MercureLogger.log(this.name, message, e);
    }

    /**
     * Prepare a statement for this plugin
     * @param sql Statement
     * @param values Values of the statement
     * @return Prepared Statement for this plugin
     * @since 1.0.2
     */
    public PreparedStatement prepareStatement(String sql, Object... values){
        return main.sqlManager.prepareStatement(this.name + "_plugin", sql, values);
    }
}
