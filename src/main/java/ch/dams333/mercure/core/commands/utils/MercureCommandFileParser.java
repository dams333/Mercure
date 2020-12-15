package ch.dams333.mercure.core.commands.utils;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoInformationException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.ConfigurationSection;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;

import java.util.List;

/**
 * Read YAML's commands' informations
 * @author Dams333
 * @version 1.0.0
 */
public class MercureCommandFileParser {
    /**
     * Mercure instance
     * @since 1.0.0
     */
    Mercure main;

    /**
     * Class' constructor
     * @param mercure Mercure instance
     * @since 1.0.0
     */
    public MercureCommandFileParser(Mercure mercure) {
        this.main = mercure;
    }

    /**
     * Get commands' informations in base config file (ch/dams333/mercure/core/commands/utils/MercureCommands.yml)
     * @since 1.0.0
     */
    public void parse() {
        YAMLConfiguration yamlConfiguration = new YAMLConfiguration(ClassLoader.getSystemResourceAsStream("ch/dams333/mercure/core/commands/utils/MercureCommands.yml"));

        for(String name : yamlConfiguration.getKeys(false)){
            MercureLogger.log(MercureLogger.LogType.DEBUG, "Chargement des informations de la commande " + name + " ...");
            ConfigurationSection section = yamlConfiguration.getConfigurationSection(name);
            String description = section.getString("description");
            List<String> permissions = section.getStringList("permissions");

            YAMLConfiguration config = main.getConfig();
            if(config.getKeys(false).contains(name + "Permission")){
                permissions.add(config.getString(name + "Permission"));
            }

            List<String> format = section.getStringList("format");
            List<String> aliases = section.getStringList("aliases");
            MercureLogger.log(MercureLogger.LogType.DEBUG, "Informations de la commande " + name + " chargée avec succès");
            main.commandManager.registerCommandInformations(new MercureCommand(name, aliases, description, permissions, format));
        }

    }

    /**
     * Get commands' informations from a ConfigurationSection
     * @param globalSection Sections where are the commands' informations
     * @throws NoInformationException An infromation miss
     * @since 1.0.0
     */
    public void parse(ConfigurationSection globalSection) throws NoInformationException {

        for(String name : globalSection.getKeys(false)){
            MercureLogger.log(MercureLogger.LogType.DEBUG, "Chargement des informations de la commande " + name + " ...");
            ConfigurationSection section = globalSection.getConfigurationSection(name);
            if(!section.getKeys(false).contains("description")) throw new NoInformationException("La commande n'a pas de description");
            if(!section.getKeys(false).contains("permissions")) throw new NoInformationException("La commande n'a pas de permission");
            if(!section.getKeys(false).contains("format")) throw new NoInformationException("La commande n'a pas de format");
            if(!section.getKeys(false).contains("aliases")) throw new NoInformationException("La commande n'a pas d'alias");
            String description = section.getString("description");
            List<String> permissions = section.getStringList("permissions");
            List<String> format = section.getStringList("format");
            List<String> aliases = section.getStringList("aliases");
            MercureLogger.log(MercureLogger.LogType.DEBUG, "Informations de la commande " + name + " chargée avec succès");
            main.commandManager.registerCommandInformations(new MercureCommand(name, aliases, description, permissions, format));
        }
    }
}
