package es.uco.pw.factory;

import java.util.Date;

import es.uco.pw.business.dto.ReservaAdultosDTO;
import es.uco.pw.business.dto.ReservaFamiliarDTO;
import es.uco.pw.business.dto.ReservaInfantilDTO;

/**
 * Factory concreta para crear reservas bono con parámetros
 * 
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

public class ReservaBonoFactory extends ReservaFactory {

    /**
     * Identificador del bono
     */
    private String bonoId;
    
    /**
     * Numero de sesion dentro del bono
     */
    private int sesion;

    /**
     * Constructor de la clase ReservaBonoFactory
     * @param bonoId Identificador del bono
     * @param sesion Numero de sesion dentro del bono
     */
    public ReservaBonoFactory(String bonoId, int sesion) {
        this.bonoId = bonoId;
        this.sesion = sesion;
    }
    
    /**
     * Crea una reserva infantil con bono usando parametros personalizados
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
        
        ReservaInfantilDTO reserva = new ReservaInfantilDTO(idUsuario, fecha, duracion, idPista, precio, descuento, numeroNinos);
        reserva.setBonoId(bonoId); 
        reserva.setSesion(sesion); 
        return reserva;
    }

    /**
     * Crea una reserva familiar con bono usando parametros personalizados
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
        
        ReservaFamiliarDTO reserva = new ReservaFamiliarDTO(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos, numeroNinos);
        reserva.setBonoId(bonoId);
        reserva.setSesion(sesion);
        return reserva;
    }

    /**
     * Crea una reserva adultos con bono usando parametros personalizados
     *  @param idUsuario Identificador del usuario
     *  @param fecha Fecha
     *  @param duracion Duracion
     *  @param idPista Identificador de la pista
     *  @param precio Precio
     *  @param descuento Descuento
     *  @param numeroAdultos Numero de adultos 
     *  @return reserva Objeto de tipo ReservaAdultos creado
     */
    @Override
    public ReservaAdultosDTO createReservaAdultos(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos) {
        
        ReservaAdultosDTO reserva = new ReservaAdultosDTO(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos);
        reserva.setBonoId(bonoId); 
        reserva.setSesion(sesion); 
        return reserva;
    }
}