package ch.dams333.mercure.utils.yaml;

import java.util.LinkedHashMap;

/**
 * Section of a YAMLFile
 * @author Dams333
 * @version 1.0.0
 */
public class ConfigurationSection extends YAMLConfiguration {

    /**
     * Content of the section
     * @since 1.0.0
     */
    private LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    /**
     * If parent is a configuration file
     * @see YAMLConfiguration
     * @since 1.0.0
     */
    private YAMLConfiguration parent;
    /**
     * If parent is a configuration section
     * @since 1.0.0
     */
    private ConfigurationSection parentSection;
    /**
     * Section's name
     * @since 1.0.0
     */
    private String name;

    /**
     * Class' constructor if the parent is a YAMLConfiguration
     * @param map Section's config
     * @param parent YAMLConfiguration that is the parent
     * @param name Section's name
     * @since 1.0.0
     */
    public ConfigurationSection(LinkedHashMap<String, Object> map, YAMLConfiguration parent, String name) {
        super(map);
        this.parent = parent;
        this.name = name;
        this.parentSection = null;
    }
    /**
     * Class' constructor if the parent is a ConfigurationSection
     * @param map Section's config
     * @param parent ConfigurationSection that is the parent
     * @param name Section's name
     * @since 1.0.0
     */
    public ConfigurationSection(LinkedHashMap<String, Object> map, ConfigurationSection parentSection, String name) {
        super(map);
        this.parentSection = parentSection;
        this.name = name;
        this.parent = null;
    }

    /**
     * Modifiy a value
     * @param key Key to define
     * @param object Value to define
     * @see YAMLConfiguration
     * @since 1.0.0
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
     * Called by a child to be updated
     * @param name Name of the child's section
     * @param map Value of the child's section
     * @see YAMLConfiguration
     * @since 1.0.0
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
     * Create a sub section
     * @param name Name of the sub section
     * @return ConfigurationSection
     * @see YAMLConfiguration
     * @since 1.0.0
     */
    @Override
    public ConfigurationSection createConfigurationSection(String name){
        map.put(name, "{}");
        return new ConfigurationSection(map, this, name);
    }
}
