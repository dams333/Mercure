package ch.dams333.mercure.core.commands.utils;

import java.util.List;

/**
 * List of a command's informations
 * @author Dams333
 * @version 1.0.0
 */
public class MercureCommand {

    /**
     * Command's name
     * @since 1.0.0
     */
    private String name;
     /**
     * Command's aliases
     * @since 1.0.0
     */
    private List<String> aliases;
     /**
     * Command's description
     * @since 1.0.0
     */
    private String description;
     /**
     * Command's permissions
     * @since 1.0.0
     */
    private List<String> permissions;
     /**
     * Command's format
     * @since 1.0.0
     */
    private List<String> format;

    /**
     * Class' constructor
     * @param name Command's name
     * @param aliases Command's aliases
     * @param description Command's description
     * @param permissions Command's permissions
     * @param format Command's formats
     * @since 1.0.0
     */
    public MercureCommand(String name, List<String> aliases, String description, List<String> permissions, List<String> format) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.permissions = permissions;
        this.format = format;
    }

    /**
     * Get command's name
     * @return String
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Get command's aliases
     * @return List of string
     * @since 1.0.0
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Get command's description
     * @return String
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get command's permissions
     * @return List of string
     * @since 1.0.0
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * Get command's formats
     * @return List of strings
     * @since 1.0.0
     */
    public List<String> getFormat() {
        return format;
    }
}
