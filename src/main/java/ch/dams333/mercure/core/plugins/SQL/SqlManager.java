package ch.dams333.mercure.core.plugins.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import ch.dams333.mercure.Mercure;
import ch.dams333.mercure.utils.SQL.SqlHelper;
import ch.dams333.mercure.utils.logger.MercureLogger;
import ch.dams333.mercure.utils.logger.MercureLogger.LogType;
import ch.dams333.mercure.utils.yaml.YAMLConfiguration;

/**
 * Plugin's SQL managing class
 * @author Dams333
 * @version 1.0.0
 */
public class SqlManager {

    /**
     * Mercure's Instance
     * @since 1.0.0
     */
    private Mercure main;

    /**
     * Database's host 
     * @since 1.0.0
     */
    private String host; 

    /**
     * Database's port 
     * @since 1.0.0
     */
    private String port; 

    /**
     * Database's user 
     * @since 1.0.0
     */
    private String user; 

    /**
     * Database's password 
     * @since 1.0.0
     */
    private String password; 

    /**
     * Connection to the database
     * @since 1.0.0
     */
    private Connection connection;

    /**
     * Map of SqlHelpers' by name of plugin
     * @since 1.0.0
     */
    private Map<String, SqlHelper> sqlHelpers;

    /**
     * Class' constructor
     * @param main Mercure's Instance
     * @since 1.0.0
     */
    public SqlManager(Mercure main) {
        this.main = main;
        this.sqlHelpers = new HashMap<>();
        this.initSQLInfos();
    }

    /**
     * Connect to database
     * @since 1.0.0
     */
    private void initSQLInfos(){
        YAMLConfiguration config = main.getConfig();
        if(config.getKeys(false).contains("sql")){
            host = config.getConfigurationSection("sql").getString("host");
            port = config.getConfigurationSection("sql").getString("port");
            user = config.getConfigurationSection("sql").getString("user");
            password = config.getConfigurationSection("sql").getString("password");

            String url = "jdbc:mysql://" + host + ":" + port + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
            try {
                connection = DriverManager.getConnection(url, user, password);
                MercureLogger.log(LogType.SUCESS, "Connexion SQL établie avec succès");
            } catch (SQLException e) {
                MercureLogger.log("Erreur lors de la connexion SQL", e);
            }
        }else{
            MercureLogger.log(LogType.WARN, "Il n'y a pas d'information de connexion SQL");
        }
    }

    /**
     * Create SqlHelper for plugin (and create database if needed)
     * @param pluginName Name of the plugin need a database
     * @since 1.0.0
     */
    private void createDatabaseForPlugin(String pluginName){
        if(this.connection != null){

            boolean needToInit = true;
            try {
                PreparedStatement statement = connection.prepareStatement("SHOW DATABASES");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if(rs.getString(1).equalsIgnoreCase(pluginName)){
                        needToInit = false;
                    }
                }
                rs.close();
                statement.close();
            } catch (SQLException e1) {
                MercureLogger.log("Erreur lors de l'éxecution de la requête SQL", e1);
            }

            if(needToInit){
                String sql = "CREATE DATABASE " + pluginName;
                try {
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(sql);
                    stmt.close();

                    MercureLogger.log(LogType.SUCESS, "Base de données pour le plugin " + pluginName + " créée avec succès");
                } catch (SQLException e) {
                    MercureLogger.log("Erreur lors de la création de la base de donnée", e);
                }
            }
            this.sqlHelpers.put(pluginName, new SqlHelper(host, port, user, password, pluginName));

        }else{
            MercureLogger.log(LogType.ERROR, "Le plugin " + pluginName + " a besoin d'une base de donnée mais impossible de s'y connecter");
        }
    }

    /**
     * Has the plugin an linked SqlHelper
     * @param pluginName Name of the plugin
     * @return Boolean
     * @since 1.0.0
     */
    private boolean hasSQLConnection(String pluginName){
        return this.sqlHelpers.keySet().contains(pluginName);
    }

    /**
     * Prepare a statement for a plugin
     * @param pluginName Name of the plugin
     * @param sql The statement
     * @param values The values of the statement
     * @return The statement prepared for the good connection
     * @since 1.0.0
     */
    public PreparedStatement prepareStatement(String pluginName, String sql, Object... values){
        if(!hasSQLConnection(pluginName)){
            createDatabaseForPlugin(pluginName);
        }

        PreparedStatement statement = null;
        try {
            statement = this.sqlHelpers.get(pluginName).prepareStatement(sql, values);
        } catch (SQLException e) {
            MercureLogger.log("Erreur lors de l'éxecution de la requête SQL", e);
        }
        return statement;
    }

    /**
     * Close connection to database and connections for all plugins
     * @since 1.0.0
     */
    public void closeConnections() {
        for(SqlHelper helper : this.sqlHelpers.values()){
            helper.closeConnection();
        }
        this.sqlHelpers = new HashMap<>();
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                MercureLogger.log("Erreur lors de la fermeture de la connexion SQL", e);
            }
        }
    }
    
}
