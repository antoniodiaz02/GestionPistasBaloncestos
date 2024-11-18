package es.uco.pw.data.DAOs;

import es.uco.pw.business.ReservaBonoFactory;
import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;


/**
 * Clase que gestiona las pistas en la base de datos.
 */
public class ReservaDAO {
	
    private final String rutaArchivoJugadores = "src/es/uco/pw/files/users.txt";
    private final String rutaArchivoPistas = "src/es/uco/pw/files/pistas.txt";
    private final String rutaArchivoReservas = "src/es/uco/pw/files/reservas.txt";
    private final String rutaArchivoBonos = "src/es/uco/pw/files/bonos.txt";


	/**
     * Objeto connection para crear la conexión con la base de datos.
     */
    private Connection connection;
    
    /**
     * Objeto properties encargado de las sentencias SQL.
     */
    private Properties properties;
    /**
     * Constructor que inicializa la conexión con base de datos.
     */
    public ReservaDAO() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/sql.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading SQL properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean hacerReservaIndividual(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends ReservaDTO> tipoReserva) {
        int jugador = buscarIdJugador(correoUsuario);
        PistaDTO pista = buscarPista(nombrePista);
        
        
        // Comprobación adicional para evitar reservas en la misma pista y hora
        if (existeReservaParaPistaYHora(nombrePista, fechaHora)) {
            System.out.println(" ERROR! Ya existe una reserva para la misma pista y horario.");
            return false;
        }
        
        // Si no existe el jugador y la pista devuelve false.
        if (jugador == -1) {
            System.out.println(" ERROR! El usuario no existe.");
            return false;
        }
        if (pista == null) {
            System.out.println(" ERROR! La pista no existe.");
            return false;
        }
        
        // Si la pista no está disponible devuelve false.
        if (!pista.isDisponible()) {
            System.out.println(" ERROR! La pista no está disponible.");
            return false;
        }
        // Si se intenta reservar la pista 24 horas antes devuelve false.
        if (plazoExcedido(fechaHora)) {
            System.out.println(" ERROR! No se puede reservar una pista antes de 24 horas.");
            return false;
        }
        
        ReservaDTO reserva = null;
        float precio = calcularPrecio(duracion);
        float descuento = jugador.calcularAntiguedad() > 2 ? precio * 0.1f : 0;

        // Crear la instancia de la reserva de acuerdo con el tipo
        if (tipoReserva == ReservaInfantilDTO.class && pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET) {
            reserva = new ReservaInfantilDTO(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroNinos);
        } else if (tipoReserva == ReservaFamiliarDTO.class && (pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET || pista.getTamanoPista() == PistaDTO.TamanoPista.TRES_VS_TRES)) {
            reserva = new ReservaFamiliarDTO(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos, numeroNinos);
        } else if (tipoReserva == ReservaAdultosDTO.class) {
            reserva = new ReservaAdultosDTO(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos);
        }

        if (reserva != null) {
            return true;
        }
        
        System.out.println(" ERROR! Tipo incorrecto de reserva.\nADULTOS->Pistas ADULTOS. FAMILIAR-> Pistas MINIBASKET y 3v3. INFANTIL-> Pistas MINIBASKET");
        return false;
    }
    
    /**
	 * Realiza una reserva con bono.
	 * @param correoUsuario Correo del usuario que realiza la reserva.
	 * @param nombrePista Nombre de la pista a reservar.
	 * @param fechaHora Día y hora de la reserva de la pista.
	 * @param duracion Tiempo de duración de la reserva (30, 60 ó 120 mins).
	 * @param numeroAdultos Número de adultos que acuden.
	 * @param numeroNinos Número de niños que acuden.
	 * @param tipoReserva Clase Reserva que tiene más cantidad de detalles de la reserva como el tipo de reserva, el tamaño de la pista...
	 * @param bonoId El identificador del bono con el que se va a realizar la reserva.
	 * @return Devuelve true si el procedimiento de reserva se ha hecho de manera correcta, y false si hay algo que se incumple.
	 */
    public boolean hacerReservaBono(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends ReservaDTO> tipoReserva, int bonoId) {
    	int jugador = buscarIdJugador(correoUsuario);
        PistaDTO pista = buscarPista(nombrePista);
        
        
        // Comprobación adicional para evitar reservas en la misma pista y hora
        if (existeReservaParaPistaYHora(nombrePista, fechaHora)) {
            System.out.println(" ERROR! Ya existe una reserva para la misma pista y horario.");
            return false;
        }

        // Si no existe el jugador y la pista devuelve false.
        if (jugador == -1) {
            System.out.println(" ERROR! El usuario no existe.");
            return false;
        }
        if (pista == null) {
            System.out.println(" ERROR! La pista no existe.");
            return false;
        }
        
        // Si la pista no está disponible devuelve false.
        if (!pista.isDisponible()) {
            System.out.println(" ERROR! La pista no está disponible.");
            return false;
        }
        // Si se intenta reservar la pista 24 horas antes devuelve false.
        if (plazoExcedido(fechaHora)) {
            System.out.println(" ERROR! No se puede reservar una pista antes de 24 horas.");
            return false;
        }
        
        // 1. Comprobacion del bono si está disponible para poder hacer reservas. 
        if(!comprobarBono(bonoId,correoUsuario,pista.getTamanoPista())) {
        	return false;
        }        

        int sesion= obtenerSesionesRestantes(bonoId);
        
        ReservaDTO reserva = null;
        ReservaBonoFactory reservaBono = new ReservaBonoFactory(bonoId, sesion);
    	
        float precio = calcularPrecio(duracion);
        float descuento = 0.05f;
        
        
        if (tipoReserva == ReservaInfantilDTO.class && pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET) {
        	reserva= reservaBono.createReservaInfantil(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroNinos);
        } else if (tipoReserva == ReservaFamiliarDTO.class && (pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET || pista.getTamanoPista() == PistaDTO.TamanoPista.TRES_VS_TRES)) {
            reserva= reservaBono.createReservaFamiliar(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos, numeroNinos);
        } else if (tipoReserva == ReservaAdultosDTO.class) {
            reserva= reservaBono.createReservaAdultos(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos);
        }
        
        
        // 3. Modificacion del número de reservas de bono y adición de la fecha si es su primera reserva.
        if (reserva != null) {
            return actualizarSesionesBono(bonoId);
        }
        
        System.out.println(" ERROR! Tipo incorrecto de reserva. El tipo del bono es distinto al de la reserva.");
        return false;
        
    }	
    
    
    /**
	 * Genera un nuevo bono de usuario.
	 * @param correoUsuario Correo del usuario que pide el bono.
	 * @param tamano Tipo de pista a la que asignar el bono.
	 * @return Devuelve true si el procedimiento de creacion del bono se ha hecho de manera correcta, y false si hay algo que falla.
	 */
    public boolean hacerNuevoBono(String correoUsuario, TamanoPista tamano){
    	    	
    	JugadorDAO jugador= new JugadorDAO();
    	int idUsuario= -1;
    	
    	String queryInsert = properties.getProperty("insert_bono");   
    	String queryJugador= properties.getProperty("buscar_por_correo");
    	
    	DBConnection dbConnection = new DBConnection();
        Connection conexion = dbConnection.getConnection();
 	
        if(jugador.buscarUsuarioPorCorreo(correoUsuario) != 1) {
        	return false;
        }
  
    	// Valor por defecto para las sesiones de un bono nuevo
    	int sesiones = 5;
    	
    	try (PreparedStatement stmtJugador = conexion.prepareStatement(queryJugador)) {
    		stmtJugador.setString(1, correoUsuario);
    		
    		ResultSet rs = stmtJugador.executeQuery();

            // Si la consulta devuelve un resultado
            if (rs.next()) {
                // Obtener el valor de id_usuario del ResultSet
                idUsuario = rs.getInt("idUsuario"); // Asegúrate de que "id_usuario" es el nombre correcto de la columna en tu base de datos
            }
            try (PreparedStatement stmtInsert = conexion.prepareStatement(queryInsert)) {
            	// Establecer los parámetros de la consulta
            	stmtInsert.setInt(1, idUsuario);                // ID del usuario
            	stmtInsert.setInt(2, sesiones);                 // Número de sesiones
            	stmtInsert.setString(3, tamano.toString());     // Tamaño de la pista (usamos toString() para convertir a texto)
            	
            	// Ejecutar la consulta
            	stmtInsert.executeUpdate();
            	System.out.println("Bono creado exitosamente");
            } catch (SQLException e) {
            	System.out.println("ERROR! No se pudo insertar el nuevo bono en la base de datos: " + e.getMessage());
            	e.printStackTrace();
            	return false;
            }
    		
    	} catch (SQLException e) {
    		System.out.println(" ERROR! No se pudo acceder a la base de datos del Jugador: " + e.getMessage());
    		e.printStackTrace();
    		return false;
    	} finally {		
    		// Cerrar la conexión a la base de datos
    		dbConnection.closeConnection();
    	}
    	// Si todo fue bien, retornamos true
    	return true;
        	
    }

    
    /**
	 * Función que calcula el precio de reserva respecto al tiempo que se quiere reservar.
	 * @param duracion Tiempo de duración de la reserva.
	 * @return Devuelve el precio total de reserva sin descuento respecto al tiempo de reserva.
	 */
    private float calcularPrecio(int duracion) {
        switch (duracion) {
            case 60:
                return 20.0f;
            case 90:
                return 30.0f;
            case 120:
                return 40.0f;
            default:
                throw new IllegalArgumentException(" ERROR! Duración no permitida. Use 60, 90 o 120 minutos.");
        }
    }
    
    
    /**
     * Busca y devuelve el identificador del jugador con el correo descrito.
     *
     * @param correoElectronico Correo electrónico del jugador a buscar.
     * @return El valor de su identificador.
     */
    public int buscarIdJugador(String correoElectronico) {
        String queryBuscar = properties.getProperty("buscar_por_correo");
        DBConnection db = new DBConnection();
        connection = db.getConnection();
        int idJugador= -1;

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, correoElectronico);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	idJugador= rs.getInt("idUsuario");
            }
	    } catch (SQLException e) {
	        System.err.println("Error al buscar el usuario en la base de datos: " + e.getMessage());
	        return -1; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
        return idJugador;
    }
    
    
    /**
     * Busca y devuelve el identificador de la pista con el nombre a buscar.
     *
     * @param nombre Nombre de la pista a buscar.
     * @return El identificador de la pista con el nombre de pista nombre.
     */
    public int buscarIdPista(String nombre) {
    	PistaDAO pista = new PistaDAO();
    	String queryBuscar = properties.getProperty("find_pista_by_nombre");
        DBConnection db = new DBConnection();
        connection = db.getConnection();
        int idPista= -1;

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, nombre);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	idPista= rs.getInt("idPista");
            }
	    } catch (SQLException e) {
	        System.err.println("Error al buscar el usuario en la base de datos: " + e.getMessage());
	        return -1; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
        return idPista;
    }
    
    
    /**
     * Busca y devuelve un objeto Pista a partir de su nombre.
     * Lee el archivo de pistas línea por línea hasta encontrar el nombre de pista solicitado.
     * Luego, convierte los datos en un objeto Pista.
     *
     * @param nombre Nombre de la pista a buscar.
     * @return Un objeto Pista si el nombre existe en el archivo, o null si no se encuentra
     *         o si ocurre algún error.
     */
    public PistaDTO buscarPista(String nombre) {
    	PistaDAO pista = new PistaDAO();
    	return pista.findPistaByNombre(nombre);
    }
    
    
    /**
	 * Guarda la reserva en el archivo reservas.txt
	 * @param reserva Clase Reserva que tiene todos los datos de la reserva.
	 * @param idReserva Identificador único de la reserva.
	 */
    private void guardarReservaEnArchivo(ReservaDTO reserva, String idReserva) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Properties propiedadesSQL = new Properties();
        
        // Cargar las propiedades SQL desde el archivo sql.properties
        try (FileInputStream fis = new FileInputStream("sql.properties")) {
            propiedadesSQL.load(fis);
        } catch (IOException e) {
            System.out.println(" ERROR! No se pudo cargar el archivo sql.properties: " + e.getMessage());
            return;
        }
        
        // Obtener la consulta INSERT INTO desde el archivo de propiedades
        String insert_reserva = propiedadesSQL.getProperty("insert_reserva");

        // Usar la clase DBConnection para obtener la conexión
        DBConnection dbConnection = new DBConnection();
        Connection conexion = dbConnection.getConnection();

        if(conexion == null) {
            System.out.println(" ERROR! No se pudo establecer la conexión con la base de datos.");
            return;
        }

        try (PreparedStatement stmt = conexion.prepareStatement(insert_reserva)) {
            String tipoReserva;
            if (reserva instanceof ReservaInfantilDTO) {
                tipoReserva = "INFANTIL";
            } else if (reserva instanceof ReservaFamiliarDTO) {
                tipoReserva = "FAMILIAR";
            } else if (reserva instanceof ReservaAdultosDTO) {
                tipoReserva = "ADULTOS";
            } else {
                throw new IllegalArgumentException(" ERROR! Tipo de reserva desconocido");
            }

            // Configurar los parámetros de la consulta SQL
            stmt.setString(1, idReserva);
            stmt.setString(2, tipoReserva);
            stmt.setString(3, reserva.getUsuarioId());
            stmt.setString(4, reserva.getPistaId());
            stmt.setString(5, sdf.format(reserva.getFechaHora()));
            stmt.setInt(6, reserva.getDuracion());
            stmt.setDouble(7, reserva.getPrecio());
            stmt.setDouble(8, reserva.getDescuento());

            // Configurar parámetros específicos según el tipo de reserva
            if (reserva instanceof ReservaInfantilDTO) {
                stmt.setInt(9, ((ReservaInfantilDTO) reserva).getNumNinos());
                stmt.setNull(10, java.sql.Types.INTEGER); // No hay adultos en la reserva infantil
            } else if (reserva instanceof ReservaFamiliarDTO) {
                stmt.setInt(9, ((ReservaFamiliarDTO) reserva).getNumNinos());
                stmt.setInt(10, ((ReservaFamiliarDTO) reserva).getNumAdultos());
            } else if (reserva instanceof ReservaAdultosDTO) {
                stmt.setNull(9, java.sql.Types.INTEGER); // No hay niños en la reserva de adultos
                stmt.setInt(10, ((ReservaAdultosDTO) reserva).getNumAdultos());
            }

            // Ejecutar la consulta SQL
            stmt.executeUpdate();
            System.out.println("Reserva guardada exitosamente en la base de datos.");
        } catch (SQLException e) {
            System.out.println(" ERROR! Error al guardar la reserva en la base de datos: " + e.getMessage());
        } finally {
            // Cerrar la conexión usando el método de DBConnection
            dbConnection.closeConnection();
        }
    }
    
    
    /**
	 * Comprueba si el bono tiene sesiones disponibles, si no está caducado, si el bono es de la persona que intenta aceder a él 
	 * y si la reserva que se quiere hacer con el bono, es a una pista del mismo tamaño que del bono.
	 * @param bonoId Identificador único del bono.
	 * @param correoUsuario Correo del usuario
	 * @param tamano Indica el tamaño de pista.
	 * @return Si se ha realizado el procedimiento correctamente devuelve true, y devuelve false si contradice una de las condiciones
	 * 		   o si ha habido un error.
	 */
    public boolean comprobarBono(String bonoId, String correoUsuario, TamanoPista tamano) {
    	String query = properties.getProperty("buscar_bono");
    	
    	DBConnection db = new DBConnection();
        Connection conexion = db.getConnection();
    	
    	boolean bonoFound = false;
    	int sesiones = 0;
    	Date fechaBono = null;
    	int idPropietario = -1;
    	
    	try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, bonoId);  // Asigna el id del bono al parámetro de la consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Se encontró el bono
                bonoFound = true;
                idPropietario = rs.getInt("usuarioId");
                sesiones = rs.getInt("sesiones");
                fechaBono = rs.getTimestamp("fechaCaducidad");  // Obtener la fecha de la primera sesión como Timestamp
                
                String tamanoString = rs.getString("tamanoPista");
                TamanoPista tamanoBono = TamanoPista.valueOf(tamanoString.toUpperCase());

                // Comprobar si el tamaño de la pista coincide con el del bono
                if (!tamanoBono.equals(tamano)) {
                    System.out.println(" ERROR! La reserva que se intenta hacer no es del mismo tipo de tamaño de pista que el del bono.");
                    return false;
                }
            } 
            
            else {
                System.out.println(" ERROR! El bono utilizado no existe.");
                return bonoFound;
            }

        } catch (SQLException e) {
            System.out.println(" ERROR! Error al acceder a la base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }

        // Comprobar que el bono tiene sesiones disponibles
        if (sesiones == 0) {
            System.out.println(" ERROR! El bono ya no tiene sesiones disponibles.");
            return false;
        }

        int idComprobar= buscarIdJugador(correoUsuario);
        // Comprobar que el bono pertenece al usuario que intenta acceder a él
        if (idComprobar!=idPropietario) {
            System.out.println(" ERROR! El bono no pertenece al usuario que intenta hacer la reserva.");
            return false;
        }

        // Verificar que la fecha del bono no exceda en un año la fecha actual
        if (plazoExcedido(fechaBono)) {
            System.out.println(" ERROR! El bono está caducado.");
            return false;
        }
    	
    	return true; // El bono es válido
    }

    
    /**
	 * Función que decrementa el numero de sesiones del bono y que añade la fecha al final si es la primera reserva del bono.
	 * @param bonoId Identificador único del bono.
	 * @return Si se ha realizado el procedimiento correctamente devuelve true, y devuelve false si ha habido algun error.
	 */
	public boolean actualizarSesionesBono(int bonoId) {
		
		String queryBuscarBono = properties.getProperty("buscar_bono");
		String queryActualizarBono= properties.getProperty("update_bono");
	    
	    DBConnection db = new DBConnection();
	    Connection conexion = db.getConnection();
	    
	    try {
	        // Paso 1: Buscar el bono por su ID
	        try (PreparedStatement stmtBuscar = conexion.prepareStatement(queryBuscarBono)) {
	            stmtBuscar.setInt(1, bonoId);
	            ResultSet rs = stmtBuscar.executeQuery();

	            if (rs.next()) {
	                int sesiones = rs.getInt("sesiones");
	                java.sql.Date fechaInicio = rs.getDate("fechaInicio");
	                java.sql.Date fechaCaducidad = rs.getDate("fechaCaducidad");

	                // Verificar si es la primera reserva (sesiones = 5)
	                
	                java.sql.Date nuevaFechaPrimeraSesion;
	                java.sql.Date nuevaFechaCaducidad;
	                if (sesiones == 5) {
	                	nuevaFechaPrimeraSesion = new java.sql.Date(new Date().getTime());
	                	
	                	Calendar calendar = Calendar.getInstance();
	                    calendar.setTime(nuevaFechaPrimeraSesion);
	                    calendar.add(Calendar.YEAR, 1);
	                    nuevaFechaCaducidad = new java.sql.Date(calendar.getTimeInMillis());
	                    
	                }
	                else {
	                	nuevaFechaPrimeraSesion= fechaInicio;
	                	nuevaFechaCaducidad= fechaCaducidad;
	                }

	                // Paso 2: Decrementar el número de sesiones
	                int nuevasSesiones = sesiones - 1;
	                if (nuevasSesiones < 0) {
	                    System.out.println(" ERROR! El bono no tiene más sesiones disponibles.");
	                    return false;
	                }

	                // Paso 3: Actualizar el bono en la base de datos
	                try (PreparedStatement stmtActualizar = conexion.prepareStatement(queryActualizarBono)) {
	                    stmtActualizar.setInt(1, nuevasSesiones);
	                    stmtActualizar.setDate(2, nuevaFechaPrimeraSesion); // Actualizar la fecha de la primera sesión si es necesario
	                    stmtActualizar.setDate(3, nuevaFechaCaducidad);
	                    stmtActualizar.setInt(4, bonoId);

	                    int rowsUpdated = stmtActualizar.executeUpdate();
	                    if (rowsUpdated <= 0) {
	                        System.out.println(" ERROR! No se pudo actualizar el bono.");
	                        return false;
	                    }
	                }
	            } else {
	                System.out.println(" ERROR! No se encontró el bono con ID: " + bonoId);
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println(" ERROR! No se pudo actualizar las sesiones del bono: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    } finally {
	        db.closeConnection();
	    }

	    return true;
	}
	
	
	/**
	 * Función que calcula si se ha excedido el plazo de 24 horas anterior a la reservas.
	 * @param fechaRecibida Fecha de la reserva que se quiere comprobar.
	 * @return Devuelve true si se excedió el plazo, y devuelve false si no se ha excedido el plazo. 
	 */
	public static boolean plazoExcedido(Date fechaRecibida) {

		Date fechaActual = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaRecibida);

        cal.add(Calendar.HOUR_OF_DAY, -24);

        return fechaActual.after(cal.getTime());
	}
	
	
	/**
	 * Función que muestra todos los detalles de las reservas futuras.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 */
	public int listarReservasFuturas() {
        int codigo = 0;
        String query = properties.getProperty("select_futuras");
        
        DBConnection db = new DBConnection();
        Connection conexion = db.getConnection();
        
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Formato de fecha requerido

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                String idReserva = rs.getString("idReserva");
                String tipoReserva= rs.getString("tipoReserva");
                int usuarioId = rs.getInt("usuarioId");
                int pistaId = rs.getInt("pistaId");
                java.util.Date fechaReserva = rs.getTimestamp("fechaHora");
                int duracion = rs.getInt("duracion");
                double precio = rs.getDouble("precio");
                double descuento = rs.getDouble("descuento");
                
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");

                // Imprimir los datos de la reserva
                System.out.println("ID Reserva: " + idReserva);
                System.out.println("Tipo de reserva" + tipoReserva);
                System.out.println("Usuario ID: " + usuarioId);
                System.out.println("Pista ID: " + pistaId);
                System.out.println("Fecha Reserva: " + sdf.format(fechaReserva));
                System.out.println("Duración: " + duracion + " horas");
                System.out.println("Precio: " + precio + " €");
                System.out.println("Descuento: " + descuento);
                
                if(tipoReserva=="ADULTOS" || tipoReserva=="FAMILIAR") {
                	System.out.println("Numero de adultos: " + numAdultos);
                }
                else if(tipoReserva=="INFANTIL" || tipoReserva=="FAMILIAR"){
                	System.out.println("Numero de niños: " + numNinos);
                	
                }
                System.out.println("───────────────────────────────────────");
            }
        } catch (SQLException e) {
            System.out.println("ERROR! No se pudo obtener las reservas futuras: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
	
	
	/**
	 * Función que modifica una reserva buscada por identificador único.
	 * @param idReserva Identificador único de la reserva a modificar.
	 * @param nuevaReserva Clase Reserva con todos los nuevos detalles modificados.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 * @throws IOException Si ocurre un error de entrada/salida al modificar el archivo de reservas.
	 */
	public int modificarReserva(String idReserva, ReservaDTO nuevaReserva) throws IOException {
		String query = properties.getProperty("modificar_reserva");
		int codigo= -1;
        
        DBConnection db = new DBConnection();
        Connection conexion = db.getConnection();
        
        // Comprobación adicional para asegurar que la nueva fecha es futura
        if (!esReservaFutura(nuevaReserva.getFechaHora())) {
            System.out.println(" ERROR! No se puede modificar la reserva a una fecha pasada.");
            return -1; // Código de error indicando que la fecha es pasada
        }
        
        if(plazoExcedido(nuevaReserva.getFechaHora())) return -1;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Determinar el tipo de reserva (INFANTIL, FAMILIAR, ADULTOS)
            String tipoReserva = (nuevaReserva instanceof ReservaInfantilDTO) ? "INFANTIL"
                    : (nuevaReserva instanceof ReservaFamiliarDTO) ? "FAMILIAR"
                    : "ADULTOS";

            // Establecer los parámetros en la consulta SQL
            statement.setInt(1, buscarIdJugador(nuevaReserva.getUsuarioId()));  						// Usuario ID
            statement.setTimestamp(2, new java.sql.Timestamp(nuevaReserva.getFechaHora().getTime()));   // Fecha y hora
            statement.setInt(3, nuevaReserva.getDuracion());  											// Duración
            statement.setInt(4, buscarIdPista(nuevaReserva.getPistaId()));  							// Pista ID
            statement.setDouble(5, calcularPrecio(nuevaReserva.getDuracion()));  						// Precio calculado
            statement.setDouble(6, nuevaReserva.getDescuento());  										// Descuento aplicado
            statement.setString(7, tipoReserva);  														// Tipo de reserva
            
            // Determinar el número de personas según el tipo de reserva
            if(tipoReserva== "INFANTIL" ) {
            	statement.setInt(8, nuevaReserva.getNumNinos()); 
            	statement.setNull(9, java.sql.Types.INTEGER);
            	
            }
            else if(tipoReserva== "FAMILIAR") {
            	statement.setInt(8, nuevaReserva.getNumNinos()); 
            	statement.setNull(9, nuevaReserva.getNumAdultos());
            }
            else {
            	statement.setInt(8, java.sql.Types.INTEGER);
            	statement.setNull(9, nuevaReserva.getNumAdultos());
            }

            // Ejecutar la actualización
            int rowsUpdated = statement.executeUpdate();
            
            // Comprobar si se actualizó alguna fila
            if (rowsUpdated > 0) {
                codigo = 1; // La reserva fue modificada correctamente
            } else {
                codigo = 0; // No se encontró la reserva
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar la reserva en la base de datos: " + e.getMessage());
            e.printStackTrace();
            codigo = -1; // Error durante la modificación
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
	
	
	/**
	 * Función que calcula si muestra todos los detalles de las reservas con una fecha y pista exacta.
	 * @param fechaBuscada Fecha de la reserva a filtrar.
	 * @param idPista Identificador de la pista a filtrar.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 */
	public int listarReservasPorFechaYPista(Date fechaBuscada, String idPista) {
		String query = properties.getProperty("modificar_reserva");
	    int codigo = 0;
	    
	    DBConnection db = new DBConnection();
        Connection conexion = db.getConnection();
        
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Formato de fecha requerido

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                String idReserva = rs.getString("idReserva");
                String tipoReserva= rs.getString("tipoReserva");
                int usuarioId = rs.getInt("usuarioId");
                int pistaId = rs.getInt("pistaId");
                java.util.Date fechaReserva = rs.getTimestamp("fechaHora");
                int duracion = rs.getInt("duracion");
                double precio = rs.getDouble("precio");
                double descuento = rs.getDouble("descuento");
                
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");

                // Imprimir los datos de la reserva
                System.out.println("ID Reserva: " + idReserva);
                System.out.println("Tipo de reserva" + tipoReserva);
                System.out.println("Usuario ID: " + usuarioId);
                System.out.println("Pista ID: " + pistaId);
                System.out.println("Fecha Reserva: " + sdf.format(fechaReserva));
                System.out.println("Duración: " + duracion + " horas");
                System.out.println("Precio: " + precio + " €");
                System.out.println("Descuento: " + descuento);
                
                if(tipoReserva=="ADULTOS" || tipoReserva=="FAMILIAR") {
                	System.out.println("Numero de adultos: " + numAdultos);
                }
                else if(tipoReserva=="INFANTIL" || tipoReserva=="FAMILIAR"){
                	System.out.println("Numero de niños: " + numNinos);
                	
                }
                System.out.println("───────────────────────────────────────");
            }
        } catch (SQLException e) {
            System.out.println("ERROR! No se pudo obtener las reservas futuras: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

	    return codigo;
	}
	
	
	/**
	 * Función que cancela una reserva si no se ha excedido el plazo de 24 horas antes.
	 * @param idReserva Identificador único de la reserva a cancelar.
	 * @return Devuelve true si consiguió borrar la reserva del fichero correctamente, y devuelve false si hubo algún error.
	 */
	public boolean cancelarReserva(String idReserva) {
		boolean eliminada= false;
		
		String queryBuscarReserva = properties.getProperty("buscar_reserva"); // Consulta para buscar la reserva
	    String queryEliminarReserva = properties.getProperty("eliminar_reserva"); // Consulta para eliminar la reserva
	    
	    DBConnection db = new DBConnection();
	    Connection conexion = db.getConnection();

	    try (PreparedStatement stmtBuscar = conexion.prepareStatement(queryBuscarReserva)) {
	        // Establecer el parámetro para la consulta de búsqueda de la reserva
	        stmtBuscar.setString(1, idReserva);

	        // Ejecutar la consulta para obtener la reserva
	        ResultSet rs = stmtBuscar.executeQuery();

	        if (rs.next()) {
	            // Obtener la fecha de la reserva
	            Date fechaReserva = rs.getTimestamp("fechaReserva");

	            // Verificar si el plazo de 24 horas no ha sido excedido
	            if (!plazoExcedido(fechaReserva)) {
	                // Si el plazo no ha sido excedido, procedemos con la eliminación de la reserva

	                try (PreparedStatement stmtEliminar = conexion.prepareStatement(queryEliminarReserva)) {
	                    // Establecer el parámetro para la consulta de eliminación
	                    stmtEliminar.setString(1, idReserva);

	                    // Ejecutar la eliminación
	                    int filasAfectadas = stmtEliminar.executeUpdate();
	                    if (filasAfectadas > 0) {
	                        eliminada = true;  // Si la eliminación fue exitosa, se marca como verdadera
	                    }
	                }
	            } else {
	                System.out.println("ERROR! No se puede eliminar la reserva porque el plazo de 24 horas ha sido excedido.");
	            }
	        } else {
	            System.out.println("ERROR! No se encontró una reserva con el id proporcionado.");
	        }

	    } catch (SQLException e) {
	        System.out.println("ERROR! Error al ejecutar la consulta: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // Cerrar la conexión a la base de datos
	        db.closeConnection();
	    }
	    return eliminada;
	}
	
	
	/**
	 * Función que obtiene el numero de sesiones restantes de un bono.
	 * @param bonoId Identificador único del bono a buscar.
	 * @return sesionesRestantes Es la cantidad de sesiones que le quedan al bono.
	 */
	public int obtenerSesionesRestantes(int bonoId) {
		String query = properties.getProperty("buscar_bono");
		int sesionesRestantes=0;
		
		DBConnection db = new DBConnection();
        connection = db.getConnection();
		
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bonoId);
            
            ResultSet rs= statement.executeQuery();
            
            if (rs.next()) {
            	sesionesRestantes = rs.getInt("sesiones"); 
            }
            
            
        } catch (SQLException e) {
            System.err.println("Error inserting pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        
	    return sesionesRestantes;
	}
	
	
	/**
	 * Verifica si ya existe una reserva para la misma pista y horario.
	 * @param nombrePista Nombre de la pista a reservar.
	 * @param fechaHora Día y hora de la reserva de la pista.
	 * @return Devuelve true si ya existe una reserva para la misma pista y horario, y false si no existe.
	 */
	private boolean existeReservaParaPistaYHora(String nombrePista, Date fechaHora) {
	    try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas))) {
	        String linea;

	        while ((linea = reader.readLine()) != null) {
	            String[] datos = linea.split(";");
	            String pistaId = datos[3];
	            String fechaHoraString = datos[4];
	            Date fechaReserva;
	            
	            try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    fechaReserva = sdf.parse(fechaHoraString);
                } catch (ParseException e) {
                    System.out.println("Error al parsear la fecha: " + fechaHoraString + ". " +
                                       "Formato esperado: dd/MM/yyyy HH:mm.");
                    continue; // Saltar a la siguiente línea en caso de error
                }

	            // Compara la pista y la hora
	            if (pistaId.equals(nombrePista) && fechaReserva.equals(fechaHora)) {
	                return true; // Ya existe una reserva para la misma pista y hora
	            }
	        }
	    } catch (IOException e) {
	        System.out.println(" ERROR! Error al comprobar reservas existentes: " + e.getMessage());
	    }
	    return false;
	}
	
	
	/**
	 * Verifica si la fecha de la reserva es una fecha futura.
	 * @param fechaReserva Fecha de la reserva a verificar.
	 * @return Devuelve true si la fecha es futura, y false si la fecha ya ha pasado.
	 */
	private boolean esReservaFutura(Date fechaReserva) {
	    Date fechaActual = new Date();
	    return fechaReserva.after(fechaActual);
	}

	
	/**
	 * Obtiene el tamaño de pistas del bono. 
	 * @param bonoId Es el identificador de bono.
	 * @return Devuelve el string del tamaño del bono.
	 */
	public String obtenerTamanoBono(String bonoId) {
		String tamanoBono= " ERROR!";
		String query= properties.getProperty("buscar_bono");
		
		DBConnection db = new DBConnection();
	    Connection conexion = db.getConnection();
	    
	    try (PreparedStatement stmt = conexion.prepareStatement(query)) {
	        // Establecer el parámetro de la consulta
	        stmt.setString(1, bonoId);

	        // Ejecutar la consulta
	        ResultSet rs = stmt.executeQuery();

	        // Verificar si se encontró el bono
	        if (rs.next()) {
	            // Obtener el tamaño de la pista del bono
	            tamanoBono = rs.getString("tamanoPista");
	        } else {
	            // Si no se encuentra el bono
	            tamanoBono = "ERROR! Bono no encontrado.";
	        }

	    } catch (SQLException e) {
	        System.out.println("ERROR! Error al ejecutar la consulta: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // Cerrar la conexión a la base de datos
	        db.closeConnection();
	    }
	    return tamanoBono;
	}
	
	
	
	public ReservaDTO obtenerReservaPorId(String idReserva) {
        try {
            // Abrimos el archivo en modo lectura
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas));
            String linea;

            // Leemos cada línea del archivo
            while ((linea = reader.readLine()) != null) {
                // Cada línea tiene el formato:
                // R_0000;ADULTOS;carlosdelatorrefrias@gmail.com;Baloncesto0;30/11/2024 19:30;90;30.0;0.0;10
                String[] datos = linea.split(";");

                // Comprobar si el identificador de la reserva coincide con el proporcionado
                if (datos.length >= 9 && datos[0].equals(idReserva)) {
                    // Extraer los datos de la reserva
                    String tamanoPista = datos[1];
                    String usuarioId = datos[2];
                    String pistaId = datos[3];
                    String fechaHoraString = datos[4];
                    int duracion = Integer.parseInt(datos[5]);
                    float precio = Float.parseFloat(datos[6]);
                    float descuento = Float.parseFloat(datos[7]);
                    int particip = Integer.parseInt(datos[8]);

                    // Intentar parsear la fecha y hora
                    Date fechaHora = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        fechaHora = sdf.parse(fechaHoraString);
                    } catch (ParseException e) {
                        System.out.println(" ERROR! No se pudo parsear la fecha de la reserva: " + fechaHoraString);
                        continue; // Saltar esta línea si la fecha no es válida
                    }

                    ReservaDTO reserva;
                    if(tamanoPista== "ADULTOS") {
                    	reserva = new ReservaAdultosDTO(usuarioId, fechaHora, duracion, pistaId, precio, descuento,particip);
                    }
                    
                    else if(tamanoPista== "FAMILIAR") {
                    	int numadultos = Integer.parseInt(datos[9]);
                    	reserva = new ReservaFamiliarDTO(usuarioId, fechaHora, duracion, pistaId, precio, descuento,numadultos,particip);
                    }
                    
                    else {
                    	reserva = new ReservaInfantilDTO(usuarioId, fechaHora, duracion, pistaId, precio, descuento,particip);
                    }
                    // Crear una nueva instancia de Reserva con los datos

                    reader.close(); // Cerrar el archivo
                    return reserva; // Devolver la reserva encontrada
                }
            }

            reader.close(); // Cerrar el archivo
            System.out.println(" ERROR! No se encontró ninguna reserva con el ID proporcionado.");
        } catch (IOException e) {
            System.out.println(" ERROR! Hubo un problema al acceder al archivo: " + e.getMessage());
        }

        return null; // Devolver null si no se encontró la reserva
    }

}


