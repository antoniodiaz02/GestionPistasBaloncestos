package es.uco.pw.factory;

import es.uco.pw.data.Reserva;

/**
 * Interfaz que define los métodos para la creación de diferentes tipos de reservas.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public interface ReservaFactory {

    /**
     * Crea una reserva individual.
     * 
     * @return Reserva creada del tipo individual
     */
    Reserva crearReservaIndividual();

    /**
     * Crea una reserva utilizando un bono.
     * 
     * @param bonoId Identificador del bono
     * @param numSesion Número de sesiones del bono
     * @return Reserva creada utilizando el bono
     */
    Reserva crearReservaBono(String bonoId, int numSesion);
}
