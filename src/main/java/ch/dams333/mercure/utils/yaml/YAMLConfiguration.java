package ch.dams333.mercure.utils.yaml;

import ch.dams333.mercure.utils.logger.MercureLogger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;


/**
 * YAML file facilities
 * @author Dams333
 * @version 1.0.0
 */
public class YAMLConfiguration {

    /**
     * Content of the file
     * @since 1.0.0
     */
    private LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    /**
     * Class' constructor by Map
     * @param map Content of the file
     * @since 1.0.0
     */
    public YAMLConfiguration(LinkedHashMap<String, Object> map) {
        this.map = map;
    }

    /**
     * Class' constructor by InputStream
     * @param in InputStream to load
     * @since 1.0.0
     */
    public YAMLConfiguration(InputStream in){
        Yaml yaml = new Yaml();
        map = yaml.load(in);
    }

     /**
     * Class' constructor by file path
     * @param path Path where is the YAML file
     * @since 1.0.0
     */
    public YAMLConfiguration(String path){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            MercureLogger.log("Impossible de trouver le fichier spécifié", e);
        }
        Yaml yaml = new Yaml();
        map = yaml.load(inputStream);
        if(map == null){
            map = new LinkedHashMap<String, Object>();
        }
    }

    /**
     * Load a config file by path (if it does not exist, it will be created)
     * @param path Access path to the file
     * @return YAMLConfiguration
     * @since 1.0.0
     */
    public static YAMLConfiguration load(String path){
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
                FileWriter myWriter = new FileWriter(path);
                myWriter.write("{}\n");
                myWriter.close();
            } catch (IOException e) {
                MercureLogger.log("Impossible de créer le fichier", e);
            }
        }
        YAMLConfiguration yamlConfiguration = new YAMLConfiguration(path);
        return yamlConfiguration;
    }

    /**
     * Get all the keys of the current section
     * @param inLowerCase Keys printed in lower cases
     * @return List of String
     * @since 1.0.0
     */
    public List<String> getKeys(boolean inLowerCase){
        List<String> keys = new ArrayList<>();
        if(inLowerCase) {
            for(String key : map.keySet()){
                keys.add(key.toLowerCase());
            }
        }else{
            keys.addAll(map.keySet());
        }
        return keys;
    }

    /**
     * Get a configuration sections
     * @param key Section's name
     * @return ConfigurationSection
     * @since 1.0.0
     */
    public ConfigurationSection getConfigurationSection(String key) {
        for(String keyInFile : getKeys(false)){
            if(keyInFile.equals(key)){
                if(map.get(keyInFile) instanceof LinkedHashMap){
                    ConfigurationSection section = new ConfigurationSection((LinkedHashMap<String, Object>) map.get(keyInFile), this, key);
                    return section;
                }else{
                    MercureLogger.log(MercureLogger.LogType.ERROR, "La clé ne charge pas de section dans ce fichier YAML");
                    return null;
                }
            }
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "Cette clé n'existe pas dans ce fichier YAML");
        return null;
    }

    /**
     * Get a value
     * @param key Key to search
     * @return Value
     * @since 1.0.0
     */
    public Object get(String key){
        for(String keyInFile : getKeys(false)) {
            if (keyInFile.equals(key)) {
                return map.get(keyInFile);
            }
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "Cette clé n'existe pas dans ce fichier YAML");
        return null;
    }

    /**
     * Get a list of values
     * @param key Key to find
     * @return List of values
     * @since 1.0.0
     */
    public List<Object> getList(String key){
        for(String keyInFile : getKeys(false)) {
            if (keyInFile.equals(key)) {
                if(map.get(keyInFile) instanceof ArrayList){
                    return (List<Object>) map.get(keyInFile);
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Cette clé ne fait pas référence à une liste");
                return null;
            }
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "Cette clé n'existe pas dans ce fichier YAML");
        return null;
    }

    /**
     * Get a list of Strings
     * @param key Key to find
     * @return List of Strings
     * @since 1.0.0
     */
    public List<String> getStringList(String key){
        for(String keyInFile : getKeys(false)) {
            if (keyInFile.equals(key)) {
                if(map.get(keyInFile) instanceof ArrayList){
                    if(((ArrayList) map.get(keyInFile)).size() > 0) {
                        if (((ArrayList) map.get(keyInFile)).get(0) instanceof String) {
                            return (List<String>) map.get(keyInFile);
                        }
                    }else{
                        return new ArrayList<>();
                    }
                    MercureLogger.log(MercureLogger.LogType.ERROR, "Cette liste n'est pas remplie de string");
                    return null;
                }
                MercureLogger.log(MercureLogger.LogType.ERROR, "Cette clé ne fait pas référence à une liste");
                return null;
            }
        }
        MercureLogger.log(MercureLogger.LogType.ERROR, "Cette clé n'existe pas dans ce fichier YAML");
        return null;
    }

    /**
     * Get a String
     * @param key Key to find
     * @return String
     * @since 1.0.0
     */
    public String getString(String key){
        Object object = get(key);
        if(object instanceof String){
            return String.valueOf(object);
        }
        return null;
    }

    /**
     * Get an Integer
     * @param key Key to find
     * @return Integer
     * @since 1.0.0
     */
    public int getInt(String key){
        Object object = get(key);
        if(object instanceof Integer){
            return (int) object;
        }
        return Integer.parseInt(null);
    }

    /**
     * Set a value
     * @param key Key to define
     * @param object Value to define
     * @since 1.0.0
     */
    public void set(String key, Object object){
        map.put(key, object);
    }

    /**
     * Create a section in the current section
     * @param name Name of the section
     * @return ConfigurationSection
     * @since 1.0.0
     */
    public ConfigurationSection createConfigurationSection(String name){
        map.put(name, "{}");
        return new ConfigurationSection(map, this, name);
    }

    /**
     * Save this config in a file
     * @param path Path to the file
     * @since 1.0.0
     */
    public void save(String path){

        Yaml yaml = new Yaml();
        try {
            FileWriter writer = null;
            writer = new FileWriter(path);
            PrintWriter pw = new PrintWriter(writer);
            pw.write("");
            pw.flush();
            pw.close();
        } catch (IOException e) {
            MercureLogger.log("Impossible de remettre le fichier à 0", e);
        }

        try {
            FileWriter writer = null;
            writer = new FileWriter(path);
            yaml.dump(map, writer);
            writer.close();
        } catch (IOException e) {
            MercureLogger.log("Impossible d'écrire dans le fichier", e);
        }
    }

     /**
     * Called by a child to be updated
     * @param name Name of the child's section
     * @param map Value of the child's section
     * @since 1.0.0
     */
    protected void update(String name, LinkedHashMap<String,Object> map) {
        this.map.put(name, map);
    }
}
