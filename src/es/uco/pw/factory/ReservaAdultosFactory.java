package es.uco.pw.factory;

import es.uco.pw.data.ReservaAdultos;

/**
 * Clase que implementa la interfaz ReservaFactory para crear reservas de adultos.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaAdultosFactory implements ReservaFactory {

    /**
     * Crea una reserva individual de tipo adultos.
     * 
     * @return ReservaAdultos creada
     */
    @Override
    public ReservaAdultos crearReservaIndividual() {
        return new ReservaAdultos();
    }

    /**
     * Crea una reserva de tipo adultos utilizando un bono.
     * 
     * @param bonoId Identificador del bono
     * @param numSesion Número de sesiones del bono
     * @return ReservaAdultos creada utilizando el bono
     */
    @Override
    public ReservaAdultos crearReservaBono(String bonoId, int numSesion) {
        ReservaAdultos reserva = new ReservaAdultos();
        // Lógica adicional para aplicar el bono
        return reserva;
    }
}
