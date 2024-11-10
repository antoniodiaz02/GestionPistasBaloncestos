package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.MaterialDTO;
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
public class PistaDAO {

    private Connection connection;
    private Properties properties;

    /**
     * Constructor que inicializa la conexión con base de datos.
     */
    public PistaDAO() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/sql.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading SQL properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserta una nueva pista en la base de datos.
     *
     * @param pista El objeto PistaDTO que se desea insertar.
     * @return true si la operación es exitosa, false de lo contrario.
     */
    public boolean insertPista(PistaDTO pista) {
        boolean respuesta = false;
        String query = properties.getProperty("insert_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pista.getNombre());
            statement.setBoolean(2, pista.isDisponible());
            statement.setBoolean(3, pista.isInterior());
            statement.setString(4, pista.getTamanoPista().name());
            statement.setInt(5, pista.getMaxJugadores());

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

    public PistaDTO findPistaByNombre(String nombre) {
        PistaDTO pista = null;
        String query = properties.getProperty("find_pista_by_nombre");  // Asegúrate de que esta propiedad esté definida en el archivo de propiedades.

        // Obtener la conexión utilizando DBConnection
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Establecer el parámetro para la consulta
            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PistaDTO.TamanoPista tamanoPista = PistaDTO.TamanoPista.valueOf(resultSet.getString("tamano").toUpperCase());
                    pista = new PistaDTO(
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("tipo").equalsIgnoreCase("INTERIOR"),
                        tamanoPista,
                        resultSet.getInt("numMaxJugadores")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding pista by nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar la conexión después de usarla
            db.closeConnection();
        }

        return pista;
    }


    /**
     * Actualiza la información de una pista en la base de datos.
     *
     * @param pista El objeto PistaDTO con los nuevos datos.
     * @return true si la operación es exitosa, false de lo contrario.
     */
    public boolean updatePista(PistaDTO pista) {
        boolean respuesta = false;
        String query = properties.getProperty("update_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, pista.isDisponible());
            statement.setBoolean(2, pista.isInterior());
            statement.setString(3, pista.getTamanoPista().name());
            statement.setInt(4, pista.getMaxJugadores());
            statement.setString(5, pista.getNombre());

            int rowsUpdated = statement.executeUpdate();
            respuesta = rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }

    /**
     * Elimina una pista de la base de datos.
     *
     * @param nombre El nombre de la pista que se desea eliminar.
     * @return true si la operación es exitosa, false de lo contrario.
     */
    public boolean deletePista(String nombre) {
        boolean respuesta = false;
        String query = properties.getProperty("delete_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);

            int rowsDeleted = statement.executeUpdate();
            respuesta = rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }


   
    public List<PistaDTO> listarPistas() {
        List<PistaDTO> todasLasPistas = new ArrayList<>();
        String query = properties.getProperty("listar_todas_las_pistas");

        // Obtener la conexión utilizando DBConnection
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                PistaDTO.TamanoPista tamanoPista = PistaDTO.TamanoPista.valueOf(resultSet.getString("tamano").toUpperCase());
                PistaDTO pista = new PistaDTO(
                    resultSet.getString("nombre"),
                    resultSet.getBoolean("estado"),
                    resultSet.getString("tipo").equalsIgnoreCase("INTERIOR"),
                    tamanoPista,
                    resultSet.getInt("numMaxJugadores")
                );
                todasLasPistas.add(pista);
            }
        } catch (SQLException e) {
            System.err.println("Error listing pistas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar la conexión después de usarla
            db.closeConnection();
        }

        return todasLasPistas;
    }


}