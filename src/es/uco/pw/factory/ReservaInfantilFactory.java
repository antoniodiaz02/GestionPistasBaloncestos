package es.uco.pw.factory;

import es.uco.pw.data.ReservaInfantil;

/**
 * Clase que implementa la interfaz ReservaFactory para crear reservas infantiles.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaInfantilFactory implements ReservaFactory {

    /**
     * Crea una reserva individual de tipo infantil.
     * 
     * @return ReservaInfantil creada
     */
    @Override
    public ReservaInfantil crearReservaIndividual() {
        return new ReservaInfantil();
    }

    /**
     * Crea una reserva de tipo infantil utilizando un bono.
     * 
     * @param bonoId Identificador del bono
     * @param numSesion Número de sesiones del bono
     * @return ReservaInfantil creada utilizando el bono
     */
    @Override
    public ReservaInfantil crearReservaBono(String bonoId, int numSesion) {
        ReservaInfantil reserva = new ReservaInfantil();
        // Lógica adicional para aplicar el bono
        return reserva;
    }
}
