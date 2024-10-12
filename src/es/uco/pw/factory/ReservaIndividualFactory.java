package es.uco.pw.factory;

import es.uco.pw.data.*;
import java.util.Date;

/**
 * Clase que representa una fábrica para crear reservas individuales.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaIndividualFactory extends ReservaFactory {

    @Override
    public ReservaInfantil createReservaInfantil(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroNinos) {
        return new ReservaInfantil(idUsuario, fecha, duracion, idPista, precio, descuento, numeroNinos);
    }

    @Override
    public ReservaFamiliar createReservaFamiliar(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos, int numeroNinos) {
        return new ReservaFamiliar(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos, numeroNinos);
    }

    @Override
    public ReservaAdultos createReservaAdultos(String idUsuario, Date fecha, int duracion, String idPista, float precio, float descuento, int numeroAdultos) {
        return new ReservaAdultos(idUsuario, fecha, duracion, idPista, precio, descuento, numeroAdultos);
    }
}
