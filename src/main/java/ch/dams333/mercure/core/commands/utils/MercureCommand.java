package ch.dams333.mercure.core.commands.utils;

import java.util.List;

public class MercureCommand {

    private String name;
    private List<String> aliases;
    private String description;
    private List<String> permissions;
    private List<String> format;

    public MercureCommand(String name, List<String> aliases, String description, List<String> permissions, List<String> format) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.permissions = permissions;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getFormat() {
        return format;
    }
}
