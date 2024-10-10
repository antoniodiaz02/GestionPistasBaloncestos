package es.uco.pw.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una pista de baloncesto disponible en las instalaciones.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 08-10-2024
 *  @version 1.0
 */
public class Pista {

    /**
     * Nombre de la pista
     */
    private String nombre;

    /**
     * Estado de la pista (disponible o no)
     */
    private boolean disponible;

    /**
     * Tipo de la pista (interior/exterior)
     */
    private boolean esInterior;

    /**
     * Tamaño de la pista
     */
    private TamanoPista tamanoPista;

    /**
     * Número máximo de jugadores autorizados
     */
    private int maxJugadores;

    /**
     * Lista de materiales asociados a la pista
     */
    private List<Material> materiales;

    public enum TamanoPista {
        MINIBASKET, ADULTOS, TRES_VS_TRES
    }

    /**
     * Constructor parametrizado de la clase Pista
     * 
     * @param nombre Nombre de la pista
     * @param disponible Estado de la pista
     * @param esInterior Tipo de pista (interior/exterior)
     * @param tamanoPista Tamaño de la pista
     * @param maxJugadores Máximo de jugadores permitidos
     */
    public Pista(String nombre, boolean disponible, boolean esInterior, TamanoPista tamanoPista, int maxJugadores) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.esInterior = esInterior;
        this.tamanoPista = tamanoPista;
        this.maxJugadores = maxJugadores;
        this.materiales = new ArrayList<>();
    }

    /**
     * Devuelve el nombre de la pista
     * @return nombre Nombre de la pista
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre de la pista
     * @param nombre Nombre de la pista
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el estado de la pista
     * @return disponible Estado de la pista
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Modifica el estado de la pista
     * @param disponible Estado de la pista
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Devuelve si la pista es interior
     * @return esInterior True si es interior, False si es exterior
     */
    public boolean isInterior() {
        return esInterior;
    }

    /**
     * Modifica si la pista es interior
     * @param esInterior True si es interior, False si es exterior
     */
    public void setInterior(boolean esInterior) {
        this.esInterior = esInterior;
    }

    /**
     * Devuelve el tamaño de la pista
     * @return tamanoPista Tamaño de la pista
     */
    public TamanoPista getTamanoPista() {
        return tamanoPista;
    }

    /**
     * Modifica el tamaño de la pista
     * @param tamanoPista Tamaño de la pista
     */
    public void setTamanoPista(TamanoPista tamanoPista) {
        this.tamanoPista = tamanoPista;
    }

    /**
     * Devuelve el número máximo de jugadores
     * @return maxJugadores Número máximo de jugadores
     */
    public int getMaxJugadores() {
        return maxJugadores;
    }

    /**
     * Modifica el número máximo de jugadores
     * @param maxJugadores Número máximo de jugadores
     */
    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    /**
     * Añade un material a la pista si cumple las restricciones de uso
     * @param material Material a asociar a la pista
     * @return True si se asoció correctamente, False en caso contrario
     */
    public boolean asociarMaterial(Material material) {
        if (esInterior || (!esInterior && !material.getUsoInterior())) {
            materiales.add(material);
            return true;
        }
        return false;
    }

    /**
     * Muestra la información de la pista
     * @return String Información de la pista
     */
    @Override
    public String toString() {
        return "Pista [Nombre: " + nombre + ", Disponible: " + disponible + ", Interior: " + esInterior + 
               ", Tamaño: " + tamanoPista + ", Max Jugadores: " + maxJugadores + "]";
    }
}
