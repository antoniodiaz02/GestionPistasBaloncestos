package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.common.DBConnection;

import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Clase que gestiona a los usuarios/jugadores en la base de datos.
 */
public class JugadorDAO {

	/**
     * Objeto connection para conectarnos a la base de datos.
     */
	private Connection connection;
	
	/**
     * Objeto properties para inicializar las sentencias SQL.
     */
    private Properties properties;
	
	/**
     * Constructor que inicializa la conexión con la base de datos.
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
     * @return codigo Código de error, 0 exitoso.
     */
    public int insertJugador(JugadorDTO jugador) {
        int codigo = 0;
        String queryInsert = properties.getProperty("insert_usuario");
        String queryBuscar = properties.getProperty("buscar_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, jugador.getCorreoElectronico());
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	codigo = -2;
                return codigo; // Código para indicar que el usuario ya está registrado.
            }

            // Si el usuario no existe, procedemos a la inserción.
            try (PreparedStatement statementInsert = connection.prepareStatement(queryInsert)) {
                statementInsert.setString(1, jugador.getNombre());
                statementInsert.setString(2, jugador.getApellidos());
                statementInsert.setDate(3, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
                statementInsert.setDate(4, new java.sql.Date(jugador.getFechaInscripcion().getTime()));
                statementInsert.setString(5, jugador.getCorreoElectronico());

                int rowsInserted = statementInsert.executeUpdate();
                codigo = rowsInserted > 0 ? 1 : 0; // Retorna 1 si se insertó correctamente, 0 en caso contrario.
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario en la base de datos: " + e.getMessage());
            return -1; // Código para indicar error general de base de datos.
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
    
    /**
     * Busca si existe un usuario asociado a un correo.
     *
     * @param correo Correo del usuario a buscar.
     * @return codigo Código de error, 1 si existe el usuario.
     */
    public int buscarUsuarioPorCorreo(String correo) {
    	
    	int codigo = 0;
        String queryBuscar = properties.getProperty("buscar_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, correo);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	codigo = 1;
                return codigo;
            
            }
	    } catch (SQLException e) {
	        System.err.println("Error al buscar el usuario en la base de datos: " + e.getMessage());
	        return -1; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
	    return codigo;
    }
    
    /**
     * Lista los usuarios registrados en la base de datos.
     * @return codigo Código del error, 0 exitoso.
     */
    public int listarUsuarios() {
        String query = properties.getProperty("listar_usuarios");
        int codigo = 0;

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

        	System.out.println("\n───────────────────────────────────────");
            System.out.println("---------- Lista de Usuarios ----------");
            System.out.println("───────────────────────────────────────");

            // Verificar si el ResultSet tiene registros.
            boolean hasUsers = false; // Variable para verificar si hay usuarios.

            while (resultSet.next()) {
                hasUsers = true; // Hay al menos un usuario en el ResultSet.

                // Extraer los valores de cada columna según la estructura de UsuarioDTO.
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                Date fechaNacimiento = resultSet.getDate("fechaNacimiento");
                Date fechaInscripcion = resultSet.getDate("fechaInscripcion");
                String correoElectronico = resultSet.getString("correoElectronico");

                // Imprimir los datos del usuario en la consola.
                System.out.printf("Nombre: %s %s\n", nombre, apellidos);
                System.out.printf("Fecha de Nacimiento: %s\n", fechaNacimiento);
                System.out.printf("Fecha de Inscripción: %s\n", fechaInscripcion);
                System.out.printf("Correo Electrónico: %s\n", correoElectronico);
                System.out.println("───────────────────────────────────────");
            }

            // Si no se encontraron usuarios.
            if (!hasUsers) {
            	codigo = 2;
            }

        } catch (SQLException e) {
            System.err.println("Error listando usuarios: " + e.getMessage());
            codigo = -1; // Error al listar usuarios.
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
        
    /**
     * Modifica un jugador existente en la base de datos.
     * @param jugador Objeto JugadorDTO con la información a cambiar.
     * @param correoElectronico El correo del jugador a modificar.
     * @return codigo Código de error, 0 exitoso.
     */
    public int modificarUsuario(JugadorDTO jugador, String correo) {
        int codigo = 0;

        // Iniciar la consulta SQL para actualizar solo los campos no nulos.
        StringBuilder queryActualizar = new StringBuilder("UPDATE Usuarios SET ");
        
        boolean firstField = true;  // Para agregar coma solo si es necesario.

        // Verificar cada campo y agregarlo a la consulta si no es nulo.
        if (jugador.getNombre() != null) {
            queryActualizar.append("nombre = ?");
            firstField = false;
        }
        
        if (jugador.getApellidos() != null) {
            if (!firstField) queryActualizar.append(", ");
            queryActualizar.append("apellidos = ?");
            firstField = false;
        }
        
        if (jugador.getFechaNacimiento() != null) {
            if (!firstField) queryActualizar.append(", ");
            queryActualizar.append("fechaNacimiento = ?");
            firstField = false;
        }
        
        // Verificar si el correo fue modificado.
        if (jugador.getCorreoElectronico() != null) {
            if (!firstField) queryActualizar.append(", ");
            queryActualizar.append("correoElectronico = ?");
        }
        
        // Completar la consulta con la condición WHERE para el correo.
        queryActualizar.append(" WHERE correoElectronico = ?");

        // Crear la conexión y preparar la declaración.
        DBConnection db = new DBConnection();
        Connection connection = db.getConnection();

        try (PreparedStatement statementActualizar = connection.prepareStatement(queryActualizar.toString())) {
            int index = 1;

            // Establecer los parámetros solo si no son nulos.
            if (jugador.getNombre() != null) {
                statementActualizar.setString(index++, jugador.getNombre());
            }
            if (jugador.getApellidos() != null) {
                statementActualizar.setString(index++, jugador.getApellidos());
            }
            if (jugador.getFechaNacimiento() != null) {
                statementActualizar.setDate(index++, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
            }
            if (jugador.getCorreoElectronico() != null) {
                statementActualizar.setString(index++, jugador.getCorreoElectronico());
            }

            // Establecer el correo electrónico original del jugador (para la cláusula WHERE).
            statementActualizar.setString(index, correo);

            // Ejecutar la actualización.
            int filasActualizadas = statementActualizar.executeUpdate();
            codigo = filasActualizadas > 0 ? 1 : 0; // 1 si se actualizó correctamente, 0 si no.
        } catch (SQLException e) {
            System.err.println("Error al modificar el usuario en la base de datos: " + e.getMessage());
            codigo = -1;  // Indicar error en la base de datos.
        } finally {
            db.closeConnection();
        }
        return codigo;
    }

} 