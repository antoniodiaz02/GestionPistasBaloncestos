package es.uco.pw.data;

/**
 * Clase que representa una reserva infantil de una pista.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaInfantil extends Reserva {

    /**
     * Número de niños en la reserva
     */
    private int numNinos;

    /**
     * Constructor vacío de la clase ReservaInfantil
     */
    public ReservaInfantil() {}

    /**
     * Devuelve el número de niños en la reserva
     * @return numNinos Número de niños
     */
    public int getNumNinos() {
        return numNinos;
    }

    /**
     * Modifica el número de niños en la reserva
     * @param numNinos Número de niños
     */
    public void setNumNinos(int numNinos) {
        this.numNinos = numNinos;
    }

    /**
     * Muestra la información de la reserva infantil
     * @return String Información de la reserva
     */
    @Override
    public String toString() {
        return super.toString() + ", Tipo: Infantil, Número de niños: " + numNinos;
    }
}
