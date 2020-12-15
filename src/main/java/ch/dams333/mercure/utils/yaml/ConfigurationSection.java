package ch.dams333.mercure.utils.yaml;

import java.util.LinkedHashMap;

public class ConfigurationSection extends YAMLConfiguration {

    private LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    private YAMLConfiguration parent;
    private ConfigurationSection parentSection;
    private String name;

    public ConfigurationSection(LinkedHashMap<String, Object> map, YAMLConfiguration parent, String name) {
        super(map);
        this.parent = parent;
        this.name = name;
        this.parentSection = null;
    }

    public ConfigurationSection(LinkedHashMap<String, Object> map, ConfigurationSection parentSection, String name) {
        super(map);
        this.parentSection = parentSection;
        this.name = name;
        this.parent = null;
    }

    /**
     * Méthode pour définir une valeur
     *
     * @param key : Clé à définir
     * @param object : Valeur à défnir
     */
    @Override
    public void set(String key, Object object){
        map.put(key, object);
        if(parentSection != null){
            parentSection.update(name, this.map);
        }else {
            parent.update(name, this.map);
        }
    }

    /**
     * Méthode appelée depuis un enfant pour être mis à jour
     *
     * @param name : Nom de la section
     * @param map : Contenu de la section
     */
    @Override
    public void update(String name, LinkedHashMap<String,Object> map) {
        this.map.put(name, map);
        if(parentSection != null){
            parentSection.update(this.name, this.map);
        }else {
            parent.update(this.name, this.map);
        }
    }

    /**
     * Méthode pour créer une sous section
     *
     * @param name : Nom de la section
     * @return ConfigurationSection
     */
    @Override
    public ConfigurationSection createConfigurationSection(String name){
        map.put(name, "{}");
        return new ConfigurationSection(map, this, name);
    }
}
