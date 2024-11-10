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
    public int insertJugador(JugadorDTO jugador) {
        int codigo = 0;
        String queryInsert = properties.getProperty("insert_usuario");
        String queryBuscar = properties.getProperty("buscar_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico
            statementBuscar.setString(1, jugador.getCorreoElectronico());
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
                return -2; // Código para indicar que el usuario ya está registrado
            }

            // Si el usuario no existe, procedemos a la inserción
            try (PreparedStatement statementInsert = connection.prepareStatement(queryInsert)) {
                statementInsert.setString(1, jugador.getNombre());
                statementInsert.setString(2, jugador.getApellidos());
                statementInsert.setDate(3, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
                statementInsert.setDate(4, new java.sql.Date(jugador.getFechaInscripcion().getTime()));
                statementInsert.setString(5, jugador.getCorreoElectronico());

                int rowsInserted = statementInsert.executeUpdate();
                codigo = rowsInserted > 0 ? 1 : 0; // Retorna 1 si se insertó correctamente, 0 en caso contrario
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario en la base de datos: " + e.getMessage());
            return -1; // Código para indicar error general de base de datos
        } finally {
            db.closeConnection();
        }

        return codigo;
    }


    /**
     * Lista los usuarios de la base de datos.
     *
     *
     * @return true si la operación es exitosa, false de lo contrario.
     */
    public int listarUsuarios() {
        String query = properties.getProperty("listar_usuarios");
        int codigo = 0; // Código 0 indica éxito, -1 indica error

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("---- Lista de Usuarios ----");

            // Verificar si el ResultSet tiene registros
            boolean hasUsers = false; // Variable para verificar si hay usuarios

            while (resultSet.next()) {
                hasUsers = true; // Hay al menos un usuario en el ResultSet

                // Extraer los valores de cada columna según la estructura de UsuarioDTO
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                Date fechaNacimiento = resultSet.getDate("fechaNacimiento");
                Date fechaInscripcion = resultSet.getDate("fechaInscripcion");
                String correoElectronico = resultSet.getString("correoElectronico");

                // Imprimir los datos del usuario en la consola
                System.out.printf("Nombre: %s %s\n", nombre, apellidos);
                System.out.printf("Fecha de Nacimiento: %s\n", fechaNacimiento);
                System.out.printf("Fecha de Inscripción: %s\n", fechaInscripcion);
                System.out.printf("Correo Electrónico: %s\n", correoElectronico);
                System.out.println("-------------------------");
            }

            // Si no se encontraron usuarios
            if (!hasUsers) {
            	codigo = 2;
            }

        } catch (SQLException e) {
            System.err.println("Error listando usuarios: " + e.getMessage());
            codigo = -1; // Error al listar usuarios
        } finally {
            db.closeConnection();
        }

        return codigo; // Retorna 0 si fue exitoso, -1 en caso de error
    }

        
        
    
    
    
} 
    
    
    
    
    
    
  
				/* ps.setString(1,asistente.getNombreApellidos());
				ps.setDate(2, new java.sql.Date(asistente.getFechaNacimiento().getTime()));
				ps.setBoolean(3,asistente.isEspecial());
				if(ps.executeUpdate() == 1) {
					respuesta = 1;
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}

		c.closeConnection();
		return respuesta;
	}
    
	public int insertarUsuario(JugadorDTO newJugador) {
        int codigo = 0;

        // Comprobar si el usuario ya existe mediante el correo electrónico
        JugadorDTO jugadorExistente = buscarUsuarioPorCorreo(newJugador.getCorreoElectronico());
        if (jugadorExistente != null) {
            codigo = -2;
            return codigo; // El usuario ya está registrado
        }

        try {
            // Añadir el nuevo jugador a la lista en memoria
            usuarios.add(newJugador);

            // Guardar el nuevo jugador en el archivo users.txt
            String rutaArchivo = "src/es/uco/pw/files/users.txt"; // Ruta al archivo

            // Abrimos el archivo en modo append para añadir al final
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true));

            // Escribimos la información del usuario en una nueva línea
            writer.write(newJugador.getNombreCompleto() + ";" + 
                         new SimpleDateFormat("dd/MM/yyyy").format(newJugador.getFechaNacimiento()) + ";" +
                         newJugador.getCorreoElectronico());
            writer.newLine(); // Añadir salto de línea
            writer.close(); // Cerrar el archivo

            codigo = 1; // Usuario añadido correctamente
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
            codigo = -1; // Error al añadir el usuario
        }

        return codigo;
    }
	
    
    */