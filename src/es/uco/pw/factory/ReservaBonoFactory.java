package es.uco.pw.factory;

import es.uco.pw.data.*;
import java.util.Date;
/**
 * Clase que representa una fábrica para crear reservas que forman parte de un bono.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class ReservaBonoFactory extends ReservaFactory {
    
    private String usuarioId;
    private Date fechaHora;
    private int duracion;
    private String pistaId;
    private float precio;
    private float descuento;
    private String tipo; // "adultos", "familia", "infantil"
    private int numParticipantes; // Para adultos o niños
    private String bonoId; // Identificador del bono
    private int sesion; // Número de sesión dentro del bono

    public ReservaBonoFactory(String usuarioId, Date fechaHora, int duracion, String pistaId, float precio, float descuento, String tipo, int numParticipantes, String bonoId, int sesion) {
        this.usuarioId = usuarioId;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.pistaId = pistaId;
        this.precio = precio;
        this.descuento = descuento;
        this.tipo = tipo;
        this.numParticipantes = numParticipantes;
        this.bonoId = bonoId;
        this.sesion = sesion;
    }

    @Override
    public Reserva crearReserva() {
        Reserva reserva = null;

        switch (tipo.toLowerCase()) {
            case "adultos":
                reserva = new ReservaAdultos();
                ((ReservaAdultos) reserva).setNumAdultos(numParticipantes);
                break;
            case "familia":
                reserva = new ReservaFamiliar();
                ((ReservaFamiliar) reserva).setNumAdultos(numParticipantes); // Debes definir setNumFamilia en ReservaFamiliar
                break;
            case "infantil":
                reserva = new ReservaInfantil();
                ((ReservaInfantil) reserva).setNumNinos(numParticipantes); // Debes definir setNumNinos en ReservaInfantil
                break;
            default:
                throw new IllegalArgumentException("Tipo de reserva no válida");
        }

        reserva.setUsuarioId(usuarioId);
        reserva.setFechaHora(fechaHora);
        reserva.setDuracion(duracion);
        reserva.setPistaId(pistaId);
        reserva.setPrecio(precio);
        reserva.setDescuento(descuento);
        
        // Añadir logica para gestionar bono

        return reserva;
    }
}
