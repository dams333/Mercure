package ch.dams333.mercure.core.commands.utils;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.exceptions.NoInformationException;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.yaml.ConfigurationSection;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;

import java.io.InputStream;
import java.util.List;

public class MercureCommandFileParser {
    Mercure main;
    public MercureCommandFileParser(Mercure mercure) {
        this.main = mercure;
    }

    /**
     * Méthode pour récupérer les informations de commandes depuis le fichier des commandes de base
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
     * Méthode pour récupérer des informations de commandes spéciifiques
     *
     * @param globalSection : Section de la configuration où se trouvent les informations
     * @throws NoInformationException
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
