package es.uco.pw.business.DTOs;
import java.util.Date;

/**
 * Clase que representa una reserva de adultos de una pista.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaAdultosDTO extends ReservaDTO {

    /**
     * Número de adultos en la reserva.
     */
    private int numAdultos;

    /**
     * Constructor vacío de la clase ReservaAdultos.
     */
    public ReservaAdultosDTO() {}

    
    /**
     * Constructor parametrizable de la subclase ReservaAdultos.
     * @param idUsuario Identificador del usuario.
     * @param fechaHora Fecha hora.
     * @param duracionMinutos Duración en minutos.
     * @param idPista Identificador de la pista.
     * @param precio Precio.
     * @param descuento Descuento a aplicar.
     * @param numeroAdultos Número de adultos.
     */
    public ReservaAdultosDTO(int idUsuario, Date fechaHora, int duracionMinutos, String idPista, float precio, float descuento, int numeroAdultos) {
        super(idUsuario, fechaHora, duracionMinutos, idPista, precio, descuento);
        this.numAdultos = numeroAdultos;
    }
    
    /**
     * Devuelve el número de adultos en la reserva.
     * @return numAdultos Número de adultos.
     */
    public int getNumAdultos() {
        return numAdultos;
    }

    /**
     * Modifica el número de adultos en la reserva.
     * @param numAdultos Número de adultos.
     */
    public void setNumAdultos(int numAdultos) {
        this.numAdultos = numAdultos;
    }

    /**
     * Muestra la información de la reserva de adultos.
     * @return String Información de la reserva.
     */
    @Override
    public String toString() {
        return super.toString() + ", Tipo: Adultos, Número de adultos: " + numAdultos;
    }
}
