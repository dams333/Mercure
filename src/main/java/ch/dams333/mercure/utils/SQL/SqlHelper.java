package ch.dams333.mercure.utils.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ch.dams333.mercure.utils.logger.MercureLogger;

/**
 * A util class to make SQL requests
 * @author Dams333
 * @version 1.0.0
 */
public class SqlHelper {
    private Connection connection;

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
     * Database's name 
     * @since 1.0.0
     */
    private String database;

    /**
     * Class's contructor
     * @param host Databse's host
     * @param port Databse's port
     * @param user Databse's user
     * @param password Databse's password
     * @param database Databse's database
     * @since 1.0.0
     */
    public SqlHelper(String host, String port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.initConnection();
    }

    /**
     * Init the connection to the database
     * @since 1.0.0
     */
    public void initConnection(){
       
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            MercureLogger.log("Erreur lors de la connexion SQL", e);
        }
    }

    /**
     * Prepare a statement for this connection
     * @param sql Statement
     * @param values Values of the statement
     * @return PreparedStatement
     * @throws SQLException Error when the preparing of the statement
     * @since 1.0.0
     */
    public PreparedStatement prepareStatement(String sql, Object... values) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
        return preparedStatement;
    }

    /**
     * Close the connection
     * @since 1.0.0
     */
    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            MercureLogger.log("Erreur lors de la fermeture de la connexion SQL", e);
        }
    }
}
