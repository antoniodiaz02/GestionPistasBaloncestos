package es.uco.pw.business.DTOs;

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
public class PistaDTO {

    /**
     * Nombre de la pista.
     */
    private String nombre;

    /**
     * Estado de la pista (disponible o no).
     */
    private boolean disponible;

    /**
     * Tipo de la pista (interior/exterior).
     */
    private boolean esInterior;

    /**
     * Tamaño de la pista.
     */
    private TamanoPista tamanoPista;

    /**
     * Número máximo de jugadores autorizados.
     */
    private int maxJugadores;

    /**
     * Lista de materiales asociados a la pista.
     */
    private List<MaterialDTO> materiales;

    /**
     * Enumeración que representa los diferentes tamaños de las pistas de baloncesto.
     * Esta enumeración se utiliza para clasificar las pistas según su tamaño y 
     * el tipo de juego que se puede llevar a cabo en ellas, facilitando la 
     * gestión y la asignación adecuada de recursos.
     */
    public enum TamanoPista {
        
        /**
         * Tamaño de pista destinado a juegos de minibasket, adecuado para 
         * jugadores jóvenes o principiantes.
         */
        MINIBASKET,
        
        /**
         * Tamaño de pista diseñado para jugadores adultos, utilizado en 
         * partidos y entrenamientos de baloncesto estándar.
         */
        ADULTOS,
        
        /**
         * Tamaño de pista destinado a juegos de tres contra tres, 
         * que permite una dinámica de juego más compacta y rápida.
         */
        TRES_VS_TRES
    }

    /**
     * Constructor vacío de la clase Pista.
     */
    public PistaDTO() {
    	
    }
    
    /**
     * Constructor parametrizado de la clase Pista.
     * 
     * @param nombre Nombre de la pista.
     * @param disponible Estado de la pista.
     * @param esInterior Tipo de pista (interior/exterior).
     * @param tamanoPista Tamaño de la pista.
     * @param maxJugadores Máximo de jugadores permitidos.
     */
    public PistaDTO(String nombre, boolean disponible, boolean esInterior, TamanoPista tamanoPista, int maxJugadores) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.esInterior = esInterior;
        this.tamanoPista = tamanoPista;
        this.maxJugadores = maxJugadores;
        this.materiales = new ArrayList<>();
    }

    /**
     * Devuelve el nombre de la pista.
     * @return nombre Nombre de la pista.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre de la pista.
     * @param nombre Nombre de la pista.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el estado de la pista.
     * @return disponible Estado de la pista.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Modifica el estado de la pista.
     * @param disponible Estado de la pista.
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Devuelve si la pista es interior.
     * @return esInterior True si es interior, False si es exterior.
     */
    public boolean isInterior() {
        return esInterior;
    }

    /**
     * Modifica si la pista es interior.
     * @param esInterior True si es interior, False si es exterior.
     */
    public void setInterior(boolean esInterior) {
        this.esInterior = esInterior;
    }

    /**
     * Devuelve el tamaño de la pista.
     * @return tamanoPista Tamaño de la pista.
     */
    public TamanoPista getTamanoPista() {
        return tamanoPista;
    }

    /**
     * Modifica el tamaño de la pista.
     * @param tamanoPista Tamaño de la pista.
     */
    public void setTamanoPista(TamanoPista tamanoPista) {
        this.tamanoPista = tamanoPista;
    }

    /**
     * Devuelve el número máximo de jugadores.
     * @return maxJugadores Número máximo de jugadores.
     */
    public int getMaxJugadores() {
        return maxJugadores;
    }

    /**
     * Modifica el número máximo de jugadores.
     * @param maxJugadores Número máximo de jugadores.
     */
    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    /**
     * Devuelve el subconjunto de materiales disponible.
     * @return disponibles
     */
    public List<MaterialDTO> consultarMaterialesDisponibles() {
        List<MaterialDTO> disponibles = new ArrayList<>();
        for (MaterialDTO material : materiales) {
            if (material.getEstadoMaterial() == MaterialDTO.EstadoMaterial.DISPONIBLE) {
                disponibles.add(material);
            }
        }
        return disponibles;
    }
     
    /**
     * Añade un material a la pista si cumple las restricciones de uso.
     * @param material Material a asociar a la pista.
     * @return True si se asoció correctamente, False en caso contrario.
     */
    public boolean asociarMaterial(MaterialDTO material) {
    	  
    	    // Si la pista es exterior solo aceptar materiales de exterior.
    	    if (!esInterior && material.getUsoInterior()) {
    	        return false;
    	    }

    	    // Contar el número de pelotas, canastas y conos ya asociados.
    	    long pelotas = materiales.stream().filter(m -> m.getTipoMaterial() == MaterialDTO.TipoMaterial.PELOTAS).count();
    	    long canastas = materiales.stream().filter(m -> m.getTipoMaterial() == MaterialDTO.TipoMaterial.CANASTAS).count();
    	    long conos = materiales.stream().filter(m -> m.getTipoMaterial() == MaterialDTO.TipoMaterial.CONOS).count();

    	    if (material.getTipoMaterial() == MaterialDTO.TipoMaterial.PELOTAS && pelotas >= 12) {
    	        return false;
    	    }
    	    if (material.getTipoMaterial() == MaterialDTO.TipoMaterial.CANASTAS && canastas >= 2) {
    	        return false;
    	    }
    	    if (material.getTipoMaterial() == MaterialDTO.TipoMaterial.CONOS && conos >= 20) {
    	        return false;
    	    }
    	    
    	    // Validar que el material no esté ya asociado.
    	    if (materiales.contains(material)) {
    	        return false;
    	    }

    	    materiales.add(material);
    	    return true;
    	}
    
    /**
     * Asocia la lista de materiales.
     * @param materiales Lista de materiales.
     */
    public void setMateriales(List<MaterialDTO> materiales) {
        this.materiales = materiales;
    }
    
    /**
     * Muestra toda la información de la pista.
     * @return String Información de la pista.
     */
    @Override
    public String toString() {
    	return "Pista [Nombre: " + nombre + ", Disponible: " + disponible + ", Interior: " + esInterior + ", Tamaño: " + tamanoPista + ", Max Jugadores: " + maxJugadores + "]";
    }
}