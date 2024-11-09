package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Clase que gestiona las pistas en la base de datos.
 */
public class JugadorDAO {

	private Connection connection;
    private Properties properties;
	
	/**
     * Constructor que inicializa la conexión con base de datos.
     */
    public JugadorDAO() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/sql.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading SQL properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
    /**
     * Inserta un nuevo jugador en la base de datos.
     *
     * @param jugador El objeto JugadorDTO que se desea insertar.
     * @return true si la operación es exitosa, false de lo contrario.
     */
    public boolean insertJugador(JugadorDTO jugador) {
        boolean respuesta = false;
        String query = properties.getProperty("insert_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
        	
            /*statement.setString(1, jugador.getNombre());
            statement.setBoolean(2, jugador.isDisponible());
            statement.setBoolean(3, jugador.isInterior());
            statement.setString(4, jugador.getTamanoPista().name());
            statement.setInt(5, jugador.getMaxJugadores());
			*/
        	
            int rowsInserted = statement.executeUpdate();
            respuesta = rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }
    
    
    
}
