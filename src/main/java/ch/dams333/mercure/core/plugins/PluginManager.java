package ch.dams333.mercure.core.plugins;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoClassException;
import ch.dams333.mercure.utils.exceptions.NoInformationException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Plugin's manager
 * @author Dams333
 * @version 1.1.0
 */
public class PluginManager {
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;
    /**
     * List of all plugins
     * @since 1.0.0
     */
    private List<MercurePluginInformation> pluginInformations;

    /**
     * Class' constructor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public PluginManager(Mercure mercure) {
        this.main = mercure;
        pluginInformations = new ArrayList<>();
    }
    
    /**
     * Get all plugins
     * @return List of MercurePluginInformation
     * @since 1.0.0
     */
    public List<MercurePluginInformation> getPluginInformations() {
        return pluginInformations;
    }

    /**
     * Load plugins
     * @throws Exception Missing information for plugin's load
     * @since 1.0.0
     */
    public void loadPlugins() throws Exception {
        List<File> tmpFile = new ArrayList<>();

        if(!new File("plugins/").exists()){
            new File("plugins/").mkdir();
        }

        replaceFiles();

        for (File pluginFile : Objects.requireNonNull(new File("plugins/").listFiles())) {
            if (!pluginFile.isDirectory()) {
                tmpFile.add(pluginFile);
            }
        }
        int i = 0;
        File[] pluginFiles = new File[tmpFile.size()];
        for (File pluginFile : tmpFile) {
            pluginFiles[i] = (pluginFile);
            i++;
        }


        for (int index = 0; index < pluginFiles.length; index++) {

            if (!pluginFiles[index].exists()) {
                break;
            }

            MercureLogger.log(MercureLogger.LogType.DEBUG, "Chargement du plugin " + pluginFiles[index].toString().replaceFirst("plugins", "").replaceFirst(".jar", "").substring(1) + " ...");

            URL url = pluginFiles[index].toURI().toURL();
            URLClassLoader loader = new URLClassLoader(new URL[]{url});
            JarFile jar = new JarFile(pluginFiles[index].getAbsolutePath());
            Enumeration<?> jarContent = jar.entries();

            String name = null;
            String author = null;
            String version = null;
            String mainClassPath = null;
            Class<?> pluginClass = null;
            String description = null;

            while (jarContent.hasMoreElements()) {
                String element = jarContent.nextElement().toString();
                if (element.endsWith(".yml")) {
                    //List all YML files
                    if (element.endsWith("plugin.yml")) {
                        InputStream is = loader.getResourceAsStream(element);
                        YAMLConfiguration yamlConfiguration = new YAMLConfiguration(is);
                        if(yamlConfiguration.getKeys(true).contains("name")){
                            if(yamlConfiguration.getKeys(true).contains("author")){
                                if(yamlConfiguration.getKeys(true).contains("version")){
                                    if(yamlConfiguration.getKeys(true).contains("description")){
                                        if(yamlConfiguration.getKeys(true).contains("main")){
                                            name = yamlConfiguration.getString("name");
                                            author = yamlConfiguration.getString("author");
                                            version = yamlConfiguration.getString("version");
                                            description = yamlConfiguration.getString("description");
                                            mainClassPath = yamlConfiguration.getString("main");

                                            if(yamlConfiguration.getKeys(true).contains("commands")){
                                                main.mercureCommandFileParser.parse(yamlConfiguration.getConfigurationSection("commands"));
                                            }
                                            break;
                                        }
                                        throw new NoInformationException("Impossible de charger la clé 'main'");
                                    }
                                    throw new NoInformationException("Impossible de charger la clé 'description'");
                                }
                                throw new NoInformationException("Impossible de charger la clé 'version'");
                            }
                            throw new NoInformationException("Impossible de charger la clé 'author'");
                        }
                        throw new NoInformationException("Impossible de charger la clé 'name'");
                    }
                }
            }

            jarContent = jar.entries();
            while (jarContent.hasMoreElements()) {
                String element = jarContent.nextElement().toString();
                if(element.endsWith(".class")) {
                    //List all classes

                    element = element.substring(0,element.length()-6);
                    element = element.replaceAll("/",".");

                    if(element.equals(mainClassPath)){
                        Class<?> tmpClass = Class.forName(element ,true,loader);
                        pluginClass = tmpClass;
                    }
                }
            }

            if(pluginClass == null){
                jar.close();
                throw new NoClassException("Impossible de trouver la class principale du plugin");
            }

            MercurePlugin mercurePlugin = (MercurePlugin) pluginClass.getDeclaredConstructor().newInstance();
            mercurePlugin.setName(name);
            mercurePlugin.onLoad();

            MercureLogger.log(MercureLogger.LogType.INFO, "Le plugin " + name + " a été chargé");

            pluginInformations.add(new MercurePluginInformation(name, description, author, version, mainClassPath, mercurePlugin, loader, jar));
        }
    }

    /**
     * Start plugins
     * @since 1.0.0
     */
    public void enablePlugins() {
        for(MercurePluginInformation info : pluginInformations){
            info.getPluginClass().onEnable();
        }
    }

    /**
     * Clean stop of plugins
     * @since 1.0.0
     */
    public void disablePlugins() {
        for(MercurePluginInformation info : pluginInformations){
            MercureLogger.log(MercureLogger.LogType.DEBUG, "Déchargement du plugin " + info.getName());
            info.getPluginClass().onDisable();
            try {
                info.getUrlClassLoader().close();
                info.getJar().close();
            } catch (IOException e) {
                MercureLogger.log("Impossible d'arrêter le plugin" + info.getName(), e);
            }
            MercureLogger.log(MercureLogger.LogType.INFO, "Le plugin " + info.getName() + " a été déchargé avec succès");
        }
    }

    /**
     * Update plugins
     * @since 1.0.0
     */
    public void reloadPlugins(){
        main.listenerManager.reloadListeners();
        for(MercurePluginInformation pluginInformation : pluginInformations){
            MercureLogger.log(MercureLogger.LogType.DEBUG, "Déchargement du plugin " + pluginInformation.getName() + " ...");
            try {
                pluginInformation.getPluginClass().onDisable();
                pluginInformation.getUrlClassLoader().close();
                pluginInformation.getJar().close();
            } catch (IOException e) {
                MercureLogger.log("Impossible d'arrêter le plugin" + pluginInformation.getName(), e);
            }
            MercureLogger.log(MercureLogger.LogType.INFO, "Le plugin " + pluginInformation.getName() + " a été déchargé avec succès");
        }
        this.pluginInformations = new ArrayList<>();

        try {
            this.loadPlugins();
            this.enablePlugins();
        } catch (Exception e) {
            MercureLogger.log("Impossible de recharger les plugins", e);
        }
    }

    /**
     * Update plugins' files
     * @since 1.0.0
     */
    private void replaceFiles() {

        if(!new File("plugins/update/").exists()){
            new File("plugins/update/").mkdir();
        }
        for(File fileUpdate : Objects.requireNonNull(new File("plugins/update/").listFiles())){
            for(File fileCurrent : Objects.requireNonNull(new File("plugins/").listFiles())){
                if(fileCurrent.getName().equals(fileUpdate.getName())){
                    MercureLogger.log(MercureLogger.LogType.DEBUG, "Mise à jour du plugin " + fileUpdate.getName().replaceFirst(".jar", "") + " ...");
                    try {
                        Files.move(FileSystems.getDefault().getPath("plugins"+ File.separator + "update" + File.separator + fileUpdate.getName()), FileSystems.getDefault().getPath("plugins" + File.separator + fileCurrent.getName()), REPLACE_EXISTING);
                        MercureLogger.log(MercureLogger.LogType.INFO, "Le plugin " + fileUpdate.getName().replaceFirst(".jar", "") + " a bien été mis à jour");
                    } catch (IOException e) {
                        MercureLogger.log("Impossible de déplacer le fichier de plugin", e);
                    }
                }
            }
        }

    }
}
