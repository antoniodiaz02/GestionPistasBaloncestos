package es.uco.pw.business.Gestores;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.data.DAOs.JugadorDAO;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Clase pricipal para gestionar usuarios.
 */
public class GestorUsuarios {
	
	// Instanciación de los DAOs para acceder a la base de datos.
    JugadorDAO daoJugador = new JugadorDAO();
    
    /**
	 * Añade un nuevo usuario.
	 * @param jugador Jugador a añadir.
	 * @return codigo Código de salida.
	 */
    public int insertarUsuario(JugadorDTO jugador) {
        return daoJugador.insertJugador(jugador);
    }

    /**
	 * Lista usuarios en la base de datos.
	 * 
	 * @return codigo Código de salida.
	 */
    public int listarUsuarios() {
        return daoJugador.listarUsuarios();
    }
    
    /**
	 * Modifica el usuario en base a el parámetro deseado.
	 * 
	 * @param jugador Objeto Jugador de JugadorDTO.
	 * @param correoModificar Correo del usuario a identificar.
	 * @return codigo Código de salida.
	 */
    public int modificarUsuario(JugadorDTO jugador, String correoModificar) {
        return daoJugador.modificarUsuario(jugador, correoModificar);
    }

    /**
	 * Busca usuario por correo.
	 * 
	 * @param correo Correo del jugador a buscar en la base de datos.
	 * @return codigo Código de salida.
	 */
    public int buscarUsuarioPorCorreo(String correo) {
        return daoJugador.buscarUsuarioPorCorreo(correo);
    }
}