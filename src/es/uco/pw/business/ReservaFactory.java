package es.uco.pw.business;

import java.util.Date;

import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;

/**
 * Clase abstracta que representa una fábrica de reservas.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public abstract class ReservaFactory {
    
	/**
     * Constructor por defecto de la clase ReservaFactory.
     */
    public ReservaFactory() {
        // Constructor por defecto.
    }
	
    /**
     * Método abstracto para crear una reserva infantil.
     * @param idUsuario Id del usuario que realiza la reserva.
     * @param fecha Fecha de la reserva.
     * @param duracion Duración de la reserva en horas.
     * @param idPista Id de la pista a reservar.
     * @param precio Precio de la reserva.
     * @param descuento Descuento aplicado a la reserva.
     * @param numeroNinos Número de niños en la reserva.
     */
    public abstract ReservaInfantilDTO createReservaInfantil(int idUsuario, Date fecha, int duracion, int idPista, float precio, float descuento, int numeroNinos);
    
    /**
     * Método abstracto para crear una reserva familiar.
     * @param idUsuario Id del usuario que realiza la reserva.
     * @param fecha Fecha de la reserva.
     * @param duracion Duración de la reserva en horas.
     * @param idPista Id de la pista a reservar.
     * @param precio Precio de la reserva.
     * @param descuento Descuento aplicado a la reserva.
     * @param numeroAdultos Número de adultos en la reserva.
     * @param numeroNinos Número de niños en la reserva.
     */
    public abstract ReservaFamiliarDTO createReservaFamiliar(int idUsuario, Date fecha, int duracion, int idPista, float precio, float descuento, int numeroAdultos, int numeroNinos);
    
    /**
     * Método abstracto para crear una reserva para adultos.
     * @param idUsuario Id del usuario que realiza la reserva.
     * @param fecha Fecha de la reserva.
     * @param duracion Duración de la reserva en horas.
     * @param idPista Id de la pista a reservar.
     * @param precio Precio de la reserva.
     * @param descuento Descuento aplicado a la reserva.
     * @param numeroAdultos Número de adultos en la reserva.
     */
    public abstract ReservaAdultosDTO createReservaAdultos(int idUsuario, Date fecha, int duracion, int idPista, float precio, float descuento, int numeroAdultos);

}
