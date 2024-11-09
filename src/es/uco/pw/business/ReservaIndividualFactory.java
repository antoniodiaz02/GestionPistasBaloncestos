package es.uco.pw.business;


import java.util.Date;

import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;

/**
 * Clase que representa factory para crear reservas individuales.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaIndividualFactory extends ReservaFactory {

	/**
     * Constructor por defecto de la clase ReservaIndividualFactory.
     */
    public ReservaIndividualFactory() {
        // Constructor por defecto
    }
	
	/**
     * Crea una reserva infantil individual usando parametros personalizados
     *  @param idUsuario Identificador del usuario
     *  @param fecha Fecha
     *  @param duracion Duracion
     *  @param idPista Identificador de la pista
     *  @param precio Precio
     *  @param descuento Descuento 
     *  @param numeroNinos Numero de ninos
     *  @return reserva Objeto de tipo ReservaInfantil creado
     */
    @Override
    public ReservaInfantilDTO createReservaInfantil(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroNinos) {
        return new ReservaInfantilDTO(idUsuario, fecha, duracion, idPista, precio, descuento, numeroNinos);
    }

    /**
     * Crea una reserva familiar individual usando parametros personalizados
     *  @param idUsuario Identificador del usuario
     *  @param fecha Fecha
     *  @param duracion Duracion
     *  @param idPista Identificador de la pista
     *  @param precio Precio
     *  @param descuento Descuento
     *  @param numeroAdultos Numero de adultos 
     *  @param numeroNinos Numero de ninos
     *  @return reserva Objeto de tipo ReservaFamiliar creado
     */
    @Override
    public ReservaFamiliarDTO createReservaFamiliar(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos, int numeroNinos) {
        return new ReservaFamiliarDTO(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos, numeroNinos);
    }

    /**
     * Crea una reserva adultos individual  usando parametros personalizados.
     *  @param idUsuario Identificador del usuario.
     *  @param fecha Fecha.
     *  @param duracion Duracion.
     *  @param idPista Identificador de la pista.
     *  @param precio Precio.
     *  @param descuento Descuento. 
     *  @param numeroAdultos Numero de adultos.
     *  @return reserva Objeto de tipo ReservaAdultos creado.
     */
    @Override
    public ReservaAdultosDTO createReservaAdultos(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos) {
        return new ReservaAdultosDTO(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos);
    }
}