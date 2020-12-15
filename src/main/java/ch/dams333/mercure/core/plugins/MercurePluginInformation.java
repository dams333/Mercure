package ch.dams333.mercure.core.plugins;

import java.net.URLClassLoader;
import java.util.List;
import java.util.jar.JarFile;

public class MercurePluginInformation {

    private String name;
    private String author;
    private String version;
    private String mainClassPath;
    private MercurePlugin pluginClass;
    private String description;
    private URLClassLoader urlClassLoader;
    private JarFile jar;

    public String getDescription() {
        return description;
    }

    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }


    public JarFile getJar() {
        return jar;
    }

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

    public String getName() {

        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getMainClassPath() {
        return mainClassPath;
    }

    public MercurePlugin getPluginClass() {
        return pluginClass;
    }
}
