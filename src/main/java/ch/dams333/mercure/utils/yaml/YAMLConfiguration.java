package ch.dams333.mercure.utils.yaml;

import ch.dams333.mercure.utils.logger.MercureLogger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class YAMLConfiguration {

    private LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    public YAMLConfiguration(LinkedHashMap<String, Object> map) {
        this.map = map;
    }

    public YAMLConfiguration(InputStream in){
        Yaml yaml = new Yaml();
        map = yaml.load(in);
    }

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
     * Méthode pour cahrger une configuration depuis un fichier
     *
     * @param path : Chemin d'accès du fichier
     * @return YAMLConfiguration
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
     * Méthode pour récupérer toutes les clés de la section
     *
     * @param inLowerCase : Les clés dovient-elles êtres mises en minuscule
     * @return List des clés
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
     * Méthode pour récupérer une section de la configuration
     *
     * @param key : Nom de la section
     * @return Section
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
     * Méthode pour récupérer une valeur
     *
     * @param key : Clé à chercher
     * @return Valeur
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
     * Méthode pour récupérer une liste de valeurs
     *
     * @param key : Clé à chercher
     * @return Liste de valeurs
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
     * Méthode pour récupérer une liste de String
     *
     * @param key : Clé à chercher
     * @return Liste de String
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
     * Méthode pour récupérer un String
     *
     * @param key : Clé à chercher
     * @return String
     */
    public String getString(String key){
        Object object = get(key);
        if(object instanceof String){
            return String.valueOf(object);
        }
        return null;
    }

    /**
     * Méthode pour récupérer un Integer
     *
     * @param key : Clé à chercher
     * @return Integer
     */
    public int getInt(String key){
        Object object = get(key);
        if(object instanceof Integer){
            return (int) object;
        }
        return Integer.parseInt(null);
    }

    /**
     * Méthode pour définir une valeur
     *
     * @param key : Clé à définir
     * @param object : Valeur à défnir
     */
    public void set(String key, Object object){
        map.put(key, object);
    }

    /**
     * Méthode pour créer une section
     *
     * @param name : Nom de la section
     * @return ConfigurationSection
     */
    public ConfigurationSection createConfigurationSection(String name){
        map.put(name, "{}");
        return new ConfigurationSection(map, this, name);
    }

    /**
     * Méthode pour savuegarder la configuration dans un fichier
     *
     * @param path
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
     * Méthode appelée depuis un enfant pour être mis à jour
     *
     * @param name : Nom de la section
     * @param map : Contenu de la section
     */
    protected void update(String name, LinkedHashMap<String,Object> map) {
        this.map.put(name, map);
    }
}
