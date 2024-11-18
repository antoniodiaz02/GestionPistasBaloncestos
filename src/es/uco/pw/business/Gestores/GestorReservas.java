package es.uco.pw.business.Gestores;

import java.io.*;

import java.util.Date;



import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;
import es.uco.pw.data.DAOs.ReservaDAO;

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
public class GestorReservas {
	ReservaDAO daoReserva = new ReservaDAO();

    /**
     * Constructor de la clase GestorReservas.
     */
    public GestorReservas() {
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
    	return daoReserva.hacerReservaIndividual(correoUsuario, nombrePista, fechaHora, duracion, numeroAdultos, numeroNinos, tipoReserva);
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
    	return daoReserva.hacerReservaBono(correoUsuario, nombrePista, fechaHora, duracion, numeroAdultos, numeroNinos, tipoReserva, bonoId);
    }	
    
    
    /**
	 * Genera un nuevo bono de usuario.
	 * @param correoUsuario Correo del usuario que pide el bono.
	 * @param tamano Tipo de pista a la que asignar el bono.
	 * @return Devuelve true si el procedimiento de creacion del bono se ha hecho de manera correcta, y false si hay algo que falla.
	 */
    public boolean hacerNuevoBono(String correoUsuario, TamanoPista tamano){
    	return daoReserva.hacerNuevoBono(correoUsuario, tamano);
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
    public int buscarIdJugador(String correoElectronico) {
    	return daoReserva.buscarIdJugador(correoElectronico);
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
    	return daoReserva.buscarPista(nombre);
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
    public boolean comprobarBono(int bonoId, String correoUsuario, TamanoPista tamano) {
    	return daoReserva.comprobarBono(bonoId, correoUsuario, tamano);
    }

    
    /**
	 * Función que decrementa el numero de sesiones del bono y que añade la fecha al final si es la primera reserva del bono.
	 * @param bonoId Identificador único del bono.
	 * @return Si se ha realizado el procedimiento correctamente devuelve true, y devuelve false si ha habido algun error.
	 */
	public boolean actualizarSesionesBono(int bonoId) {
		return daoReserva.actualizarSesionesBono(bonoId);
	}
	
	/**
	 * Función que muestra todos los detalles de las reservas futuras.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 */
	public int listarReservasFuturas() {
		return daoReserva.listarReservasFuturas();
    }
	
	
	/**
	 * Función que modifica una reserva buscada por identificador único.
	 * @param idReserva Identificador único de la reserva a modificar.
	 * @param nuevaReserva Clase Reserva con todos los nuevos detalles modificados.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 * @throws IOException Si ocurre un error de entrada/salida al modificar el archivo de reservas.
	 */
	public int modificarReserva(String idReserva, ReservaDTO nuevaReserva) throws IOException {
		return daoReserva.modificarReserva(idReserva, nuevaReserva);
    }
	
	
	/**
	 * Función que calcula si muestra todos los detalles de las reservas con una fecha y pista exacta.
	 * @param fechaBuscada Fecha de la reserva a filtrar.
	 * @param idPista Identificador de la pista a filtrar.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 */
	public int listarReservasPorFechaYPista(Date fechaBuscada, String idPista) {
		return daoReserva.listarReservasPorFechaYPista(fechaBuscada, idPista);
	}
	
	
	/**
	 * Función que cancela una reserva si no se ha excedido el plazo de 24 horas antes.
	 * @param idReserva Identificador único de la reserva a cancelar.
	 * @return Devuelve true si consiguió borrar la reserva del fichero correctamente, y devuelve false si hubo algún error.
	 */
	public boolean cancelarReserva(String idReserva) {
		return daoReserva.cancelarReserva(idReserva);
	}
	
	
	/**
	 * Función que obtiene el numero de sesiones restantes de un bono.
	 * @param bonoId Identificador único del bono a buscar.
	 * @return sesionesRestantes Es la cantidad de sesiones que le quedan al bono.
	 */
	public int obtenerSesionesRestantes(int bonoId) {
		return daoReserva.obtenerSesionesRestantes(bonoId);
	}

	
	/**
	 * Obtiene el tamaño de pistas del bono. 
	 * @param bonoId Es el identificador de bono.
	 * @return Devuelve el string del tamaño del bono.
	 */
	public String obtenerTamanoBono(String bonoId) {
		return daoReserva.obtenerTamanoBono(bonoId);
	}
	
	
	
	public ReservaDTO obtenerReservaPorId(String idReserva) {
		return daoReserva.obtenerReservaPorId(idReserva);
    }

}
