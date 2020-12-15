package ch.dams333.mercure.core.plugins;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.core.plugins.pluginInteractions.PluginInteractionsManager;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;

import java.io.File;

public abstract class MercurePlugin {

    public void onLoad(){}
    public void onEnable(){}
    public void onDisable(){}

    private Mercure main = null;
    private String name = "";

    public MercurePlugin() {
        main = Mercure.selfInstance;
    }

    public void setName(String name){
        this.name = name;
    }

    public PluginInteractionsManager getPluginManager(){
        return main.registerManager;
    }

    /**
     * Méthode pour récupérer le fichier de configuration de ce plugin
     *
     * @return YAMLConfiguration
     */
    public YAMLConfiguration getConfig(){
        return YAMLConfiguration.load(getPluginDirectory() + File.separator + "config.yml");
    }

    /**
     * Méthode pour récupérer le répértoire de fichiers de ce plugin
     *
     * @return Directory
     */
    public File getPluginDirectory(){
        File file = new File("plugins" + File.separator + name + File.separator);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    /**
     * Sauvegarde la config de ce plugin
     *
     * @param config : Config à sauvegarder
     */
    public void saveConfig(YAMLConfiguration config){
        config.save(getPluginDirectory() + File.separator + "config.yml");
    }

    /**
     * Méthode permettant de savoir si ce plugin à un fichier de configuration
     *
     * @return boolean
     */
    private boolean isConfig() {
        return new File(getPluginDirectory(), "config.yml").exists();
    }

    /**
     * Méthode pour créer le fichier de configuration de ce plugin
     */
    private void createConfig(){
        YAMLConfiguration.load(getPluginDirectory() + File.separator + "config.yml");
    }
}
