package es.uco.pw.factory;

import java.util.Date;
import es.uco.pw.data.*;

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

    private String bonoId; // Identificador del bono
    private int sesion; // Número de sesión dentro del bono

    public ReservaBonoFactory(String bonoId, int sesion) {
        this.bonoId = bonoId;
        this.sesion = sesion;
    }
    

    public ReservaInfantil createReservaInfantil(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroNinos) {
        // Creación de reserva infantil con bono usando parámetros personalizados
        ReservaInfantil reserva = new ReservaInfantil(idUsuario, fecha, duracion, idPista, precio, descuento, numeroNinos);
        reserva.setBonoId(bonoId); // Asignar el bono a la reserva
        reserva.setSesion(sesion); // Establecer la sesión dentro del bono
        return reserva;
    }

    public ReservaFamiliar createReservaFamiliar(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos, int numeroNinos) {
        // Creación de reserva familiar con bono usando parámetros personalizados
        ReservaFamiliar reserva = new ReservaFamiliar(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos, numeroNinos);
        reserva.setBonoId(bonoId); // Asignar el bono a la reserva
        reserva.setSesion(sesion); // Establecer la sesión dentro del bono
        return reserva;
    }

    public ReservaAdultos createReservaAdultos(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos) {
        // Creación de reserva adultos con bono usando parámetros personalizados
        ReservaAdultos reserva = new ReservaAdultos(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos);
        reserva.setBonoId(bonoId); // Asignar el bono a la reserva
        reserva.setSesion(sesion); // Establecer la sesión dentro del bono
        return reserva;
    }
}

