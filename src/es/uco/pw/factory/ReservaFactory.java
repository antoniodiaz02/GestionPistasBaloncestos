package es.uco.pw.factory;
import es.uco.pw.data.Reserva;

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
     * Método abstracto para crear una reserva.
     * @return Reserva
     */
    public abstract Reserva crearReserva();
}
