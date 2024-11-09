package es.uco.pw.business.DTOs;
import java.util.Date;


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
public class ReservaInfantilDTO extends ReservaDTO {

    /**
     * Número de niños en la reserva
     */
    private int numNinos;

    /**
     * Constructor vacío de la clase ReservaInfantil
     */
    public ReservaInfantilDTO() {}
   
    
    /**
     * Constructor parametrizable de la subclase ReservaInfantil
     * @param idUsuario Identificador del usuario.
     * @param fechaHora Fecha hora.
     * @param duracionMinutos Duracion en minutos.
     * @param idPista Identificador de la pista.
     * @param precio Precio.
     * @param descuento Descuento a aplicar.
     * @param numeroNinos Nuemero de ninos.
     */
    public ReservaInfantilDTO(String idUsuario, Date fechaHora, int duracionMinutos, String idPista, float precio, float descuento, int numeroNinos) {
        super(idUsuario, fechaHora, duracionMinutos, idPista, precio, descuento);
        this.numNinos = numeroNinos;
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
     * Muestra la información de la reserva infantil
     * @return String Información de la reserva
     */
    @Override
    public String toString() {
        return super.toString() + ", Tipo: Infantil, Número de niños: " + numNinos;
    }
}
