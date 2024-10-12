package es.uco.pw.data;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Clase que representa a una persona que va a hacer uso de las instalaciones deportivas.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 08-10-2024
 *  @version 1.0
 */
public class Jugador {

    /**
     * Nombre y apellidos del jugador
     */
    private String nombreCompleto;

    /**
     * Fecha de nacimiento del jugador
     */
    private Date fechaNacimiento;

    /**
     * Fecha de inscripción (primera reserva)
     */
    private Date fechaInscripcion;

    /**
     * Correo electrónico del jugador (debe ser único)
     */
    private String correoElectronico;

    /**
     * Constructor vacio clase Jugador
     */
    public Jugador() {
    	
    }
    
    /**
     * Constructor parametrizado de la clase Jugador
     * 
     * @param nombreCompleto Nombre y apellidos del jugador
     * @param fechaNacimiento Fecha de nacimiento del jugador
     * @param correoElectronico Correo electrónico del jugador
     */
    public Jugador(String nombreCompleto, Date fechaNacimiento, String correoElectronico) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this.fechaInscripcion = new Date(); // Hora actual del sistema
    }

    /**
     * Devuelve el nombre completo del jugador
     * @return nombreCompleto Nombre del jugador
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Modifica el nombre completo del jugador
     * @param nombreCompleto Nombre del jugador
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Devuelve la fecha de nacimiento del jugador
     * @return fechaNacimiento Fecha de nacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Modifica la fecha de nacimiento del jugador
     * @param fechaNacimiento Fecha de nacimiento
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve el correo electrónico del jugador
     * @return correoElectronico Correo electrónico
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Modifica el correo electrónico del jugador
     * @param correoElectronico Correo electrónico
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /**
     * Calcula los años de antigüedad del jugador
     * @return años de antigüedad
     */
    public int calcularAntiguedad() {
    	
    	if (fechaInscripcion == null) {
    		return 0;
    	}
    	
        long diffInMillis = new Date().getTime() - fechaInscripcion.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
    }

    /**
     * Muestra la información del jugador
     * @return String Información del jugador
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return "Jugador [Nombre: " + nombreCompleto + ", Fecha de nacimiento: " + sdf.format(fechaNacimiento) +  ", Fecha de inscripción: " + sdf.format(fechaInscripcion) + ", Correo: " + correoElectronico +  ", Antigüedad: " + calcularAntiguedad() + " años]";
    }
}
