package es.uco.pw.business.DTOs;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;


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
public class JugadorDTO {

    /**
     * Nombre y apellidos del jugador
     */
    private String nombreCompleto;

    /**
     * Nombre del jugador
     */
    private String nombre;
    
    /**
     * Apellidos del jugador
     */
    private String apellidos;
    
    
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
    public JugadorDTO() {
    	
    }
    
    /**
     * Constructor parametrizado de la clase Jugador
     * 
     * @param nombreCompleto Nombre y apellidos del jugador
     * @param fechaNacimiento Fecha de nacimiento del jugador
     * @param correoElectronico Correo electrónico del jugador
     */
    public JugadorDTO(String nombreCompleto, Date fechaNacimiento, String correoElectronico) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this.fechaInscripcion = new Date(); // Hora actual del sistema
        
        separarNombreYApellidos(nombreCompleto);
    }


    /**
     * Devuelve la fecha de inscripcion.
     * @return nombreCompleto Nombre del jugador
     */
    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }
    
    /**
     * Devuelve el nombre completo del jugador
     * @return nombreCompleto Nombre del jugador
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    /**
     * Devuelve el nombre del jugador.
     * @return nombre Nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Devuelve los apellidos del jugador.
     * @return apellidos Apellidos del jugador.
     */
    public String getApellidos() {
        return apellidos;
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
    
    /**
     * Separa el nombre y apellidos de NombreCompleto.
     * @param nombreCompleto Nombre Completo del usuario.
     */
    private void separarNombreYApellidos(String nombreCompleto) {
        String[] partes = nombreCompleto.split(" ");

        if (partes.length == 2 || partes.length == 3) {
            // Si hay 2 o 3 partes: El primer término es el nombre, el resto son apellidos
            this.nombre = partes[0];
            this.apellidos = String.join(" ", Arrays.copyOfRange(partes, 1, partes.length));
        } else if (partes.length == 4) {
            // Si hay 4 partes: Los dos primeros son el nombre, el resto son los apellidos
            this.nombre = partes[0] + " " + partes[1]; // Nombre completo con primer y segundo nombre
            this.apellidos = partes[2] + " " + partes[3]; // Los dos últimos son los apellidos
        } else {
            // Para otros casos
            this.nombre = partes[0]; // Solo hay un nombre
            this.apellidos = partes.length > 1 ? String.join(" ", Arrays.copyOfRange(partes, 1, partes.length)) : "";
        }
    }
    
    
}
