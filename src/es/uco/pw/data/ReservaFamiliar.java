package es.uco.pw.data;
import java.util.Date;



/**
 * Clase que representa una reserva familiar de una pista.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 08-10-2024
 *  @version 1.0
 */
public class ReservaFamiliar extends Reserva {

    /**
     * Número de adultos en la reserva
     */
    private int numAdultos;

    /**
     * Número de niños en la reserva
     */
    private int numNinos;

    /**
     * Constructor vacío de la clase ReservaFamiliar
     */
    public ReservaFamiliar() {}

    /** Constructor parametrizable de la subclase ReservaFamiliar
     * @param idUsuario
     * @param fechaHora
     * @param duracionMinutos
     * @param idPista
     * @param precio
     * @param descuento
     * @param numeroAdultos
     * @param numeroNinos
     */
    public ReservaFamiliar(String idUsuario, Date fechaHora, int duracionMinutos, String idPista, float precio, float descuento, int numeroAdultos, int numeroNinos) {
        super(idUsuario, fechaHora, duracionMinutos, idPista, precio, descuento);
        this.numAdultos = numeroAdultos;
        this.numNinos = numeroNinos;
    }
    
    
    /**
     * Devuelve el número de adultos en la reserva
     * @return numAdultos Número de adultos
     */
    public int getNumAdultos() {
        return numAdultos;
    }

    /**
     * Modifica el número de adultos en la reserva
     * @param numAdultos Número de adultos
     */
    public void setNumAdultos(int numAdultos) {
        this.numAdultos = numAdultos;
    }

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
     * Muestra la información de la reserva familiar
     * @return String Información de la reserva
     */
    @Override
    public String toString() {
        return super.toString() + ", Tipo: Familiar, Número de adultos: " + numAdultos + ", Número de niños: " + numNinos;
    }
}
