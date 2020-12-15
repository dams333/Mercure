package ch.dams333.mercure.core.plugins;

import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * List of all plugin's informations
 * @author Dams333
 * @version 1.0.0
 */
public class MercurePluginInformation {

    /**
     * Plugin's name
     * @since 1.0.0
     */
    private String name;
     /**
     * Plugin's author
     * @since 1.0.0
     */
    private String author;
     /**
     * Plugin's version
     * @since 1.0.0
     */
    private String version;
     /**
     * Plugin's path to main calss
     * @since 1.0.0
     */
    private String mainClassPath;
     /**
     * Plugin's main class
     * @since 1.0.0
     */
    private MercurePlugin pluginClass;
     /**
     * Plugin's description
     * @since 1.0.0
     */
    private String description;
     /**
     * Plugin's URLClassLoader to main class
     * @since 1.0.0
     */
    private URLClassLoader urlClassLoader;
     /**
     * Plugin's jar file
     * @since 1.0.0
     */
    private JarFile jar;

    /**
     * Class' constructor
     * @param name Plugin's name
     * @param description Plugin's description
     * @param author Plugin's author
     * @param version Plugin's version
     * @param mainClassPath Plugin's path to main calss
     * @param pluginClass Plugin's main class
     * @param urlClassLoader Plugin's URLClassLoader to main class
     * @param jar Plugin's jar file
     * @since 1.0.0
     */
    public MercurePluginInformation(String name, String description, String author, String version, String mainClassPath, MercurePlugin pluginClass, URLClassLoader urlClassLoader, JarFile jar) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.mainClassPath = mainClassPath;
        this.pluginClass = pluginClass;
        this.description = description;
        this.urlClassLoader = urlClassLoader;
        this.jar = jar;

    }

    /**
     * Get plugin's name
     * @return String
     * @since 1.0.0
     */
    public String getName() {

        return name;
    }

    /**
     * Get plugin's author
     * @return String
     * @since 1.0.0
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get plugin's version
     * @return String
     * @since 1.0.0
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get plugin's path to main calss
     * @return String
     * @since 1.0.0
     */
    public String getMainClassPath() {
        return mainClassPath;
    }

    /**
     * Get plugin's main class
     * @return MercurePlugin
     * @see MercurePlugin
     * @since 1.0.0
     */
    public MercurePlugin getPluginClass() {
        return pluginClass;
    }

    /**
     * Get plugin's description
     * @return String
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get plugin's URLClassLoader to main class
     * @return URLClassLoader
     * @since 1.0.0
     */
    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    /**
     * Get plugin's JAR
     * @return JarFile
     * @since 1.0.0
     */
    public JarFile getJar() {
        return jar;
    }
}
