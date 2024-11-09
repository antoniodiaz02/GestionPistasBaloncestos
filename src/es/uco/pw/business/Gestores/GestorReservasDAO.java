package es.uco.pw.business.Gestores;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.uco.pw.business.ReservaBonoFactory;
import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */


/**
 * Clase que gestiona las Reservas de las pistas de baloncesto.
 */
public class GestorReservasDAO {
	
    private final String rutaArchivoJugadores = "src/es/uco/pw/files/users.txt";
    private final String rutaArchivoPistas = "src/es/uco/pw/files/pistas.txt";
    private final String rutaArchivoReservas = "src/es/uco/pw/files/reservas.txt";
    private final String rutaArchivoBonos = "src/es/uco/pw/files/bonos.txt";

    /**
     * Constructor de la clase GestorReservas.
     */
    public GestorReservasDAO() {
        // Constructor vacío si no tenemos nada que inicializar
    }
    
	
    /**
	 * Realiza una reserva individual (sin bono).
	 * @param correoUsuario Correo del usuario que realiza la reserva.
	 * @param nombrePista Nombre de la pista a reservar.
	 * @param fechaHora Día y hora de la reserva de la pista.
	 * @param duracion Tiempo de duración de la reserva (30, 60 ó 120 mins).
	 * @param numeroAdultos Número de adultos que acuden.
	 * @param numeroNinos Número de niños que acuden.
	 * @param tipoReserva Clase Reserva que tiene más cantidad de detalles de la reserva como el tipo de reserva, el tamaño de la pista...
	 * @return Devuelve true si el procedimiento de reserva se ha hecho de manera correcta, y false si hay algo que se incumple.
	 */
    public boolean hacerReservaIndividual(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends ReservaDTO> tipoReserva) {
        JugadorDTO jugador = buscarJugador(correoUsuario);
        PistaDTO pista = buscarPista(nombrePista);
        
        
        // Comprobación adicional para evitar reservas en la misma pista y hora
        if (existeReservaParaPistaYHora(nombrePista, fechaHora)) {
            System.out.println(" ERROR! Ya existe una reserva para la misma pista y horario.");
            return false;
        }
        
        // Si no existe el jugador y la pista devuelve false.
        if (jugador == null) {
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

        String idReserva= generarIdentificadorUnicoReservas();
        
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
            guardarReservaEnArchivo(reserva, idReserva);
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
    public boolean hacerReservaBono(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends ReservaDTO> tipoReserva, String bonoId) {
    	JugadorDTO jugador = buscarJugador(correoUsuario);
        PistaDTO pista = buscarPista(nombrePista);
        
        
        // Comprobación adicional para evitar reservas en la misma pista y hora
        if (existeReservaParaPistaYHora(nombrePista, fechaHora)) {
            System.out.println(" ERROR! Ya existe una reserva para la misma pista y horario.");
            return false;
        }

        // Si no existe el jugador y la pista devuelve false.
        if (jugador == null) {
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
        
        // 2. Realización de la reserva.
        String idReserva= generarIdentificadorUnicoReservas();
        
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
            guardarReservaEnArchivo(reserva, idReserva);
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
    	JugadorDTO jugador = buscarJugador(correoUsuario);
    	if (jugador == null) return false;
    	
    	// Valor por defecto para las sesiones de un bono nuevo
    	int sesiones = 5;
    	
    	// La fecha de la primera sesión será un valor vacío
    	String fechaPrimeraSesion = "";
    	
    	// Generar identificador único para el bono
    	String bonoId = generarIdentificadorUnicoBonos();
    	
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoBonos, true))) {
    		writer.write(bonoId + ";" + correoUsuario + ";" + tamano.toString() + ";" + sesiones + ";" + fechaPrimeraSesion);
    		writer.newLine();  // Añadir una nueva línea al final
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	// Si todo fue bien, retornamos true
    	return true;
    }
    
    
    /**
	 * Genera un identificador unico respecto al último identificador del archivo bonos.txt
	 * @return Devuelve true si el procedimiento de generar el código único es correcto, y false si ha habido algún error.
	 */
    private String generarIdentificadorUnicoBonos() {
    	String ultimoId = "B_0000";  // Identificador inicial si el archivo está vacío o no existe
    	String patron = "B_(\\d{4})"; // Expresión regular para identificar el formato B_XXXX
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoBonos))) {
    		String linea;
    		String ultimoBono = null;
    		
    		while ((linea = reader.readLine()) != null) {
    			ultimoBono = linea;
    		}
    		
    		if (ultimoBono != null) {
    			
    			String[] partes = ultimoBono.split(";");
    			String idBonoActual = partes[0];
    			
    			
    			Pattern pattern = Pattern.compile(patron);
    			Matcher matcher = pattern.matcher(idBonoActual);
    			
    			if (matcher.matches()) {
    				// Extraer el número actual y convertirlo a entero
    				int numero = Integer.parseInt(matcher.group(1));
    				// Incrementar en 1 el número y formatearlo como B_XXXX
    				ultimoId = String.format("B_%04d", numero + 1);
    			}
    		}
    	} catch (IOException e) {
    		System.out.println(" ERROR! Error al generar el identificador unico: " + e.getMessage());
    		e.printStackTrace();
    	}
    	
    	return ultimoId;
    }
    
    
    /**
	 * Genera un identificador unico respecto al último identificador del archivo reservas.txt
	 * @return Devuelve true si el procedimiento de generar el código único es correcto, y false si ha habido algún error.
	 */
    private String generarIdentificadorUnicoReservas() {
    	String ultimoId = "R_0000";  // Identificador inicial si el archivo está vacío o no existe
    	String patron = "R_(\\d{4})"; // Expresión regular para identificar el formato B_XXXX
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas))) {
    		String linea;
    		String ultimoBono = null;
    		
    		while ((linea = reader.readLine()) != null) {
    			ultimoBono = linea;
    		}
    		
    		if (ultimoBono != null) {
    			
    			String[] partes = ultimoBono.split(";");
    			String idBonoActual = partes[0];
    			
    			
    			Pattern pattern = Pattern.compile(patron);
    			Matcher matcher = pattern.matcher(idBonoActual);
    			
    			if (matcher.matches()) {
    				// Extraer el número actual y convertirlo a entero
    				int numero = Integer.parseInt(matcher.group(1));
    				// Incrementar en 1 el número y formatearlo como B_XXXX
    				ultimoId = String.format("R_%04d", numero + 1);
    			}
    		}
    	} catch (IOException e) {
    		System.out.println(" ERROR! Error al generar el identificador unico: " + e.getMessage());
    		e.printStackTrace();
    	}
    	
    	return ultimoId;
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
     * Busca y devuelve un objeto Jugador a partir de su correo electrónico.
     * Lee el archivo de jugadores línea por línea hasta encontrar el correo electrónico solicitado.
     * Luego, convierte los datos en un objeto Jugador.
     *
     * @param correoElectronico Correo electrónico del jugador a buscar.
     * @return Un objeto Jugador si el correo existe en el archivo, o null si no se encuentra
     *         o si ocurre algún error.
     */
    public JugadorDTO buscarJugador(String correoElectronico) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoJugadores))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[2].equals(correoElectronico)) {
                    // Ajustamos la conversión de fecha
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Ajusta el formato según cómo esté almacenada la fecha en el archivo
                    Date fechaNacimiento;
                    
                    try {
                        fechaNacimiento = sdf.parse(datos[1]);  // Convertimos la fecha de nacimiento a Date
                    } catch (ParseException e) {
                        System.out.println(" ERROR! Error al parsear la fecha de nacimiento: " + e.getMessage());
                        return null;
                    }
                    
                    // Creamos el objeto Jugador con los datos adaptados
                    return new JugadorDTO(datos[0], fechaNacimiento, datos[2]); // nombreCompleto, fechaNacimiento, correoElectronico
                }
            }
        } catch (IOException e) {
            System.out.println(" ERROR! Error al buscar jugador: " + e.getMessage());
        }
        return null;
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
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoPistas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].equals(nombre)) {
                    return new PistaDTO(datos[0], Boolean.parseBoolean(datos[2]), Boolean.parseBoolean(datos[3]), PistaDTO.TamanoPista.valueOf(datos[1]), Integer.parseInt(datos[4]));

                }
            }
        } catch (IOException e) {
            System.out.println(" ERROR! Error al buscar pista: " + e.getMessage());
        }
        return null;
    }
    
    
    /**
	 * Guarda la reserva en el archivo reservas.txt
	 * @param reserva Clase Reserva que tiene todos los datos de la reserva.
	 * @param idReserva Identificador único de la reserva.
	 */
    private void guardarReservaEnArchivo(ReservaDTO reserva, String idReserva) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoReservas, true))) {
            String tipoReserva;
            if (reserva instanceof ReservaInfantilDTO) {
                tipoReserva = "INFANTIL";
            } else if (reserva instanceof ReservaFamiliarDTO) {
                tipoReserva = "FAMILIAR";
            } else if (reserva instanceof ReservaAdultosDTO) {
                tipoReserva = "ADULTOS";
            } else {
                throw new IllegalArgumentException("Tipo de reserva desconocido");
            }

            String linea = idReserva + ";" + tipoReserva + ";" + reserva.getUsuarioId() + ";" + reserva.getPistaId() + ";" 
                           + sdf.format(reserva.getFechaHora()) + ";" + reserva.getDuracion() + ";" 
                           + reserva.getPrecio() + ";" + reserva.getDescuento() + ";" 
                           + ((reserva instanceof ReservaInfantilDTO) ? ((ReservaInfantilDTO) reserva).getNumNinos()
                           : (reserva instanceof ReservaFamiliarDTO) ? ((ReservaFamiliarDTO) reserva).getNumNinos() + ";" + ((ReservaFamiliarDTO) reserva).getNumAdultos()
                           : ((ReservaAdultosDTO) reserva).getNumAdultos());

            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println(" ERROR! Error al guardar reserva en el archivo: " + e.getMessage());
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
    	File archivoBonos = new File(rutaArchivoBonos);
    	
    	boolean bonoFound = false;
    	int sesiones = 0;
    	Date fechaBono = null;
    	String correoPropietario = "";
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(archivoBonos))) {
    		String line;
    		
    		while ((line = reader.readLine()) != null) {
    			String[] fields = line.split(";");
    			if (fields.length >= 4 && fields[0].equals(bonoId)) {
    				bonoFound = true;
    				correoPropietario = fields[1].trim();
    				sesiones = Integer.parseInt(fields[3].trim());
    				
    				//Si hay fecha se guarda
    				if (fields.length >= 5) {
    					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    					fechaBono = sdf.parse(fields[4].trim());
    				}
    				
    				String tamanoString= fields[2];
    				TamanoPista tamanoBono = TamanoPista.valueOf(tamanoString.toUpperCase());
    				
    				//Comprueba si se realiza una reserva del mismo tamaño de pista que el del bono.
    				if(tamanoBono!=tamano) {
    					System.out.println(" ERROR! La reserva que se intenta hacer no es del mismo tipo de tamaño de pista que el del bono.");
    					return false;
    				}
    				break;
    			}
    			
    		}
    	} catch (IOException | ParseException e) {
    		System.out.println(" ERROR! Error al hacer el parseo.");
    		e.printStackTrace();
    		return false;
    	}
    	
    	// Comprobar que el bono existe, y tiene sesiones disponibles.
    	if (!bonoFound) {
    		System.out.println(" ERROR! El bono utiizado no existe.");
    		return false;
    	}
    	
    	if (sesiones == 0) {
    		System.out.println(" ERROR! El bono ya no le quedan sesiones");
    	}
    	
    	// Comprobar que el bono pertenece al usuario que intenta acceder a él.
    	if (!correoPropietario.equals(correoUsuario)) {
    		System.out.println(" ERROR! El bono no es del propietario que intenta hacer la reserva.");
    		return false;
    	}
    	
    	// Verificar que la fecha del bono no exceda en un año la fecha actual
    	if (fechaBono != null) {
    	    Calendar cal = Calendar.getInstance();
    	    cal.setTime(fechaBono);
    	    cal.add(Calendar.YEAR, 1);
    	    if (new Date().after(cal.getTime())) {
    	    	System.out.println(" ERROR! El bono está caducado.");
    	    	return false;
    	    }
    	}
    	
    	return true; // El bono es válido
    }

    
    /**
	 * Función que decrementa el numero de sesiones del bono y que añade la fecha al final si es la primera reserva del bono.
	 * @param bonoId Identificador único del bono.
	 * @return Si se ha realizado el procedimiento correctamente devuelve true, y devuelve false si ha habido algun error.
	 */
	public boolean actualizarSesionesBono(String bonoId) {
	    File archivoBonos = new File(rutaArchivoBonos);
	    File tempFile = new File(archivoBonos.getAbsolutePath() + ".tmp");
	
	    try (BufferedReader reader = new BufferedReader(new FileReader(archivoBonos));
	         PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
	
	        String line;
	        boolean bonoFound = false;
	
	        while ((line = reader.readLine()) != null) {
	            String[] fields = line.split(";");
	            if (fields.length >= 4 && fields[0].equals(bonoId)) {
	                // Se encontró el bono
	                int sesiones = Integer.parseInt(fields[3].trim());
	                bonoFound = true;
	
	                // Verificar si es la primera reserva que realiza el bono.
	                if (sesiones == 5) {
	                	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	                    String fechaActual = sdf.format(new Date());
	                    writer.println(fields[0] + ";" + fields[1] + ";" + fields[2] + ";" + (sesiones - 1) + ";" + fechaActual);
	                } else {
	                    // Decrementar el número de sesiones
	                    writer.println(fields[0] + ";" + fields[1] + ";" + fields[2] + ";" + (sesiones - 1) + ";" + fields[4]);
	                }
	            } else {
	                // Escribir la línea original si no es la del bono
	                writer.println(line);
	            }
	        }
	
	        if (bonoFound) {
	            // Renombrar el archivo original y el temporal
	            archivoBonos.delete();
	            tempFile.renameTo(archivoBonos);
	        } else {
	            // Si no se encontró el bono, eliminar el archivo temporal
	            tempFile.delete();
	        }
	
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
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

        try {
            // Abrimos el archivo en modo lectura
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas));
            String linea;
            Date fecha;
            boolean hayReservas = false;
            boolean esFutura= false;

            // Verificamos si el archivo contiene usuarios
            System.out.println("\n Reservas realizadas:");
            System.out.println("----------------------------");
            // Leemos cada línea del archivo
            while ((linea = reader.readLine()) != null) {
                // Cada línea tiene el formato: nombreCompleto, dd/MM/yyyy, correoElectronico
                String[] datos = linea.split(";");

                if (datos.length == 9 || datos.length == 10) {
                	hayReservas = true; // Se encontró al menos un usuario

                    String id= datos[0];
                    String tamano = datos[1];
                    String correoUsuario = datos[2];
                    String pistaId = datos[3];
                    String fechaHoraString = datos[4];
                    
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        fecha = sdf.parse(fechaHoraString);
                        Date fechaActual = new Date();
                        esFutura = fecha.after(fechaActual);
                    } catch (ParseException e) {
                        System.out.println("Error al parsear la fecha: " + fechaHoraString + ". " +
                                           "Formato esperado: dd/MM/yyyy HH:mm.");
                        continue; // Saltar a la siguiente línea en caso de error
                    }
                    
                    if(esFutura) {
                    	String duracion= datos[5];
                    	String precio= datos[6];
                    	String descuento= datos[7];
                    	String numParticipantes= datos[8];
                    	
                    	// Mostramos la información del usuario
                    	System.out.println("ID de reserva: " + id);
                    	System.out.println("Tamaño de pista: " + tamano);
                    	System.out.println("Correo del reservante: " + correoUsuario);
                    	System.out.println("ID de pista: " + pistaId);
                    	System.out.println("Fecha de reserva: " + fechaHoraString);
                    	System.out.println("Duración: " + duracion + " mins.");
                    	System.out.println("Precio: " + precio);
                    	System.out.println("Descuento: " + descuento);
                    	
                    	if(datos.length == 9) {
                    		System.out.println("Numero de participantes: " + numParticipantes);
                    		
                    	}
                    	else if(datos.length == 10){
                    		String numAdultos= datos[9];
                    		System.out.println("Numero de niños: " + numParticipantes);
                    		System.out.println("Numero de adultos: " + numAdultos);
                    	}
                    	
                    	System.out.println("----------------------------");
                    }
                    	
                 }
                    
            }

            reader.close(); // Cerramos el archivo

            if (!hayReservas) {
                codigo = -1; // No hay usuarios registrados
                System.out.println("No hay reservas registradas.");
            } else {
                codigo = 1; // Usuarios listados correctamente
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            codigo = -2; // Error al intentar listar usuarios
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
        int codigo = 0;
        List<String> lineas = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        // Comprobación adicional para asegurar que la nueva fecha es futura
        if (!esReservaFutura(nuevaReserva.getFechaHora())) {
            System.out.println(" ERROR! No se puede modificar la reserva a una fecha pasada.");
            return -1; // Código de error indicando que la fecha es pasada
        }
        
        if(plazoExcedido(nuevaReserva.getFechaHora())) return -1;

        try {
            // Abrimos el archivo en modo lectura
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas));
            String linea;
            boolean reservaModificada = false;
            

            // Leemos cada línea del archivo
            while ((linea = reader.readLine()) != null) {
                
                String[] datos = linea.split(";");

                if (datos.length >= 9) {
                    String id = datos[0];

                    // Si encontramos la reserva, lo actualizamos
                    if (id.equals(idReserva)) {
                        String nuevaLinea = idReserva + ";" 
                        		+ ((nuevaReserva instanceof ReservaInfantilDTO) ? "INFANTIL"
                                : (nuevaReserva instanceof ReservaFamiliarDTO) ? "FAMILIAR"
                                : "ADULTOS")
                        		+ ";" + nuevaReserva.getUsuarioId() + ";" + nuevaReserva.getPistaId() + ";" 
                                + sdf.format(nuevaReserva.getFechaHora()) + ";" + nuevaReserva.getDuracion() + ";" 
                                + calcularPrecio(nuevaReserva.getDuracion()) + ";" + nuevaReserva.getDescuento() + ";" 
                                + ((nuevaReserva instanceof ReservaInfantilDTO) ? ((ReservaInfantilDTO) nuevaReserva).getNumNinos()
                                : (nuevaReserva instanceof ReservaFamiliarDTO) ? ((ReservaFamiliarDTO) nuevaReserva).getNumNinos() + ((ReservaFamiliarDTO) nuevaReserva).getNumAdultos()
                                : ((ReservaAdultosDTO) nuevaReserva).getNumAdultos());
                        lineas.add(nuevaLinea);
                        reservaModificada = true;
                    } else {
                        // Si no es la reserva, simplemente añadimos la línea tal como está
                        lineas.add(linea);
                    }
                }
            }

            reader.close(); // Cerramos el archivo de lectura

            if (!reservaModificada) {
                return 0; // Reserva no encontrada
            }

            // Reescribimos todo el archivo con las líneas actualizadas
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoReservas, false)); // Sobrescribimos el archivo

            for (String nuevaLinea : lineas) {
                writer.write(nuevaLinea);
                writer.newLine();
            }

            writer.close(); // Cerramos el archivo de escritura
            codigo = 1; // Usuario modificado correctamente
        } catch (IOException e) {
            System.out.println("Error al modificar el archivo: " + e.getMessage());
            codigo = -1; // Error durante la modificación
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
	    int codigo = 0;
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Solo la fecha, sin hora

	    // Crear un calendario para establecer el tiempo a medianoche
	    Calendar calBuscada = Calendar.getInstance();
	    calBuscada.setTime(fechaBuscada);
	    calBuscada.set(Calendar.HOUR_OF_DAY, 0);
	    calBuscada.set(Calendar.MINUTE, 0);
	    calBuscada.set(Calendar.SECOND, 0);
	    calBuscada.set(Calendar.MILLISECOND, 0);
	    
	    try {
	        // Abrimos el archivo en modo lectura
	        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas));
	        String linea;
	        boolean hayReservas = false;

	        // Verificamos si el archivo contiene reservas
	        System.out.println("\n Reservas realizadas para la fecha: " + sdf.format(fechaBuscada) + " y la pista ID: " + idPista);
	        System.out.println("-----------------------------------");

	        // Leemos cada línea del archivo
	        while ((linea = reader.readLine()) != null) {
	            // Cada línea tiene el formato esperado
	            String[] datos = linea.split(";");

	            if (datos.length == 9 || datos.length == 10) {
	                String id = datos[0];
	                String tamano = datos[1];
	                String correoUsuario = datos[2];
	                String pistaId = datos[3];
	                String fechaHoraString = datos[4];

	                // Intentamos parsear la fecha de la reserva
	                Date fechaReserva;
	                try {
	                    // Aquí se asume que la fecha en el archivo incluye la hora
	                    SimpleDateFormat sdfReserva = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	                    fechaReserva = sdfReserva.parse(fechaHoraString);

	                    // Ajustar la fecha de la reserva a medianoche
	                    Calendar calReserva = Calendar.getInstance();
	                    calReserva.setTime(fechaReserva);
	                    calReserva.set(Calendar.HOUR_OF_DAY, 0);
	                    calReserva.set(Calendar.MINUTE, 0);
	                    calReserva.set(Calendar.SECOND, 0);
	                    calReserva.set(Calendar.MILLISECOND, 0);
	                    
	                    // Comprobar si la fecha de la reserva coincide y si el ID de pista coincide
	                    if (calReserva.getTime().equals(calBuscada.getTime()) && pistaId.equals(idPista)) {
	                        hayReservas = true; // Se encontró al menos una reserva

	                        String duracion = datos[5];
	                        String precio = datos[6];
	                        String descuento = datos[7];
	                        String numParticipantes = datos[8];

	                        // Mostramos la información de la reserva
	                        System.out.println("ID de reserva: " + id);
	                        System.out.println("Tamaño de pista: " + tamano);
	                        System.out.println("Correo del reservante: " + correoUsuario);
	                        System.out.println("ID de pista: " + pistaId);
	                        System.out.println("Fecha de reserva: " + fechaHoraString);
	                        System.out.println("Duración: " + duracion + " mins.");
	                        System.out.println("Precio: " + precio);
	                        System.out.println("Descuento: " + descuento);

	                        if (datos.length == 9) {
	                            System.out.println("Número de participantes: " + numParticipantes);
	                        } else if (datos.length == 10) {
	                            String numAdultos = datos[9];
	                            System.out.println("Número de niños: " + numParticipantes);
	                            System.out.println("Número de adultos: " + numAdultos);
	                        }

	                        System.out.println("-----------------------------------");
	                    }
	                } catch (ParseException e) {
	                    System.out.println("Error al parsear la fecha: " + fechaHoraString + ". " +
	                                       "Formato esperado: dd-MM-yyyy HH:mm:ss.");
	                    continue; // Saltar a la siguiente línea en caso de error
	                }
	            }
	        }

	        reader.close(); // Cerramos el archivo

	        if (!hayReservas) {
	            codigo = -1; // No hay reservas para la fecha y pista especificadas
	            System.out.println("No hay reservas registradas para la fecha y pista indicadas.");
	        } else {
	            codigo = 1; // Reservas listadas correctamente
	        }
	    } catch (IOException e) {
	        System.out.println("Error al leer el archivo: " + e.getMessage());
	        codigo = -2; // Error al intentar listar reservas
	    }

	    return codigo;
	}
	
	
	/**
	 * Función que cancela una reserva si no se ha excedido el plazo de 24 horas antes.
	 * @param idReserva Identificador único de la reserva a cancelar.
	 * @return Devuelve true si consiguió borrar la reserva del fichero correctamente, y devuelve false si hubo algún error.
	 */
	public boolean cancelarReserva(String idReserva) {
	    String rutaArchivoTemporal = "src/es/uco/pw/files/temp.txt"; // Archivo temporal para copiar el contenido
	    boolean reservaEliminada = false;

	    try {
	        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoReservas));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoTemporal));
	        String linea;
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	        while ((linea = reader.readLine()) != null) {
	            String[] datos = linea.split(";");
	            if (datos.length >= 9) {
	                String id = datos[0]; // Identificador de la reserva
	                String fechaHoraString = datos[4]; // FechaHora de la reserva

	                if (id.equals(idReserva)) {
	                    try {
	                        Date fechaReserva = sdf.parse(fechaHoraString);

	                        // Comprobar si la fecha de la reserva es dentro de las últimas 24 horas

	                        if (!plazoExcedido(fechaReserva)) {
	                            // Si la reserva es válida para eliminar (aún no ha pasado 24 horas antes), no copiar la línea
	                            reservaEliminada = true;
	                            System.out.println("<La reserva con ID " + id + " ha sido eliminada.>");
	                            continue;
	                        } else {
	                            // Si ya pasó el plazo de 24 horas, mantenemos la línea en el archivo
	                            System.out.println("No se puede eliminar la reserva, ya ha pasado el plazo de 24 horas.");
	                        }
	                    } catch (ParseException e) {
	                        System.out.println("Error al parsear la fecha de la reserva: " + fechaHoraString);
	                        writer.write(linea); // En caso de error en la fecha, no eliminar la reserva
	                    }
	                } else {
	                    // Si no es la reserva buscada, copiamos la línea al archivo temporal
	                    writer.write(linea);
	                }

	                writer.newLine(); // Añadir la nueva línea después de escribir en el archivo
	            }
	        }

	        reader.close();
	        writer.close();

	        // Reemplazamos el archivo original con el archivo temporal
	        File archivoOriginal = new File(rutaArchivoReservas);
	        File archivoTemporal = new File(rutaArchivoTemporal);
	        if (archivoOriginal.delete()) {
	            archivoTemporal.renameTo(archivoOriginal);
	        }

	    } catch (IOException e) {
	        System.out.println("Error al procesar el archivo: " + e.getMessage());
	        return false;
	    }

	    return reservaEliminada;
	}
	
	
	/**
	 * Función que obtiene el numero de sesiones restantes de un bono.
	 * @param bonoId Identificador único del bono a buscar.
	 * @return sesionesRestantes Es la cantidad de sesiones que le quedan al bono.
	 */
	public int obtenerSesionesRestantes(String bonoId) {
	    File archivoBonos = new File(rutaArchivoBonos);
	    int sesionesRestantes = -1; // Inicializamos con un valor de error por defecto (-1)

	    try (BufferedReader reader = new BufferedReader(new FileReader(archivoBonos))) {
	        String linea;

	        // Leemos línea por línea
	        while ((linea = reader.readLine()) != null) {
	            String[] datos = linea.split(";"); // Asumimos que los campos están separados por ';'

	            if (datos.length >= 4 && datos[0].equals(bonoId)) {
	                // Si encontramos el bono con el ID correcto
	                try {
	                    sesionesRestantes = Integer.parseInt(datos[3].trim()); // El cuarto elemento es el número de sesiones
	                } catch (NumberFormatException e) {
	                    System.out.println("ERROR! El formato del número de sesiones no es válido.");
	                }
	                break; // Salimos del bucle si encontramos el bono
	            }
	        }

	    } catch (IOException e) {
	        System.out.println(" ERROR! No se pudo leer el archivo de bonos: " + e.getMessage());
	    }

	    if (sesionesRestantes == -1) {
	        System.out.println(" ERROR! No se encontró el bono con ID: " + bonoId);
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
	    
	    File archivoBonos = new File(rutaArchivoBonos);
	    
	    try (BufferedReader reader = new BufferedReader(new FileReader(archivoBonos))) {
	        String linea;

	        // Leer línea por línea
	        while ((linea = reader.readLine()) != null) {
	            // Dividir la línea en partes (separadas por ";")
	            String[] datos = linea.split(";");
	            
	            // Verificar que la línea tiene al menos 3 elementos
	            if (datos.length >= 3) {
	                // Comparar el id del bono (primer campo) con el bonoId pasado por parámetro
	                if (datos[0].trim().equals(bonoId)) {
	                    // Devolver el valor del tamaño de pista (tercer campo)
	                    return datos[2].trim();
	                }
	            }
	        }
	    } catch (IOException e) {
	        System.out.println(" ERROR! No se pudo leer el archivo: " + e.getMessage());
	    }

	    // Si no se encuentra el bono o ocurre algún error
	    return " ERROR! Bono no encontrado o archivo inválido.";
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
