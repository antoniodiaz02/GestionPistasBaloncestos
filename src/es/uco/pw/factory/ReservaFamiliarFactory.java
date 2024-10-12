package es.uco.pw.factory;

import es.uco.pw.data.ReservaFamiliar;

/**
 * Clase que implementa la interfaz ReservaFactory para crear reservas familiares.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaFamiliarFactory implements ReservaFactory {

    /**
     * Crea una reserva individual de tipo familiar.
     * 
     * @return ReservaFamiliar creada
     */
    @Override
    public ReservaFamiliar crearReservaIndividual() {
        return new ReservaFamiliar();
    }

    /**
     * Crea una reserva de tipo familiar utilizando un bono.
     * 
     * @param bonoId Identificador del bono
     * @param numSesion Número de sesiones del bono
     * @return ReservaFamiliar creada utilizando el bono
     */
    @Override
    public ReservaFamiliar crearReservaBono(String bonoId, int numSesion) {
        ReservaFamiliar reserva = new ReservaFamiliar();
        // Lógica adicional para aplicar el bono
        return reserva;
    }
}
