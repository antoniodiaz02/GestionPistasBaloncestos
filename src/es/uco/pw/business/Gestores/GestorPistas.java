package es.uco.pw.business.Gestores;

import java.util.ArrayList;
import java.util.List;

import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.data.DAOs.MaterialDAO;
import es.uco.pw.data.DAOs.PistaDAO;

/**
 * Clase que gestiona las pistas.
 */
public class GestorPistas {

    // Instanciación de los DAOs para acceder a las bases de datos correspondientes
    MaterialDAO daoMaterial = new MaterialDAO();
    PistaDAO daoPista = new PistaDAO();

    /**
     * Constructor de la clase GestorPistasDAO.
     */
    public GestorPistas() {}

    /**
     * Método para crear una nueva pista y guardarla.
     * 
     * @param pista PistaDTO con los datos de la pista a crear
     * @return int Código de respuesta (0: éxito, 1: error)
     */
    public boolean crearPista(PistaDTO pista) {
        // Insertar la nueva pista a través del DAO
        return daoPista.insertPista(pista);
    }
    
    public boolean crearMaterial(MaterialDTO material) {
        // Insertar la nueva pista a través del DAO
        return daoMaterial.insertMaterial(material);
    }

    /**
     * Método para listar todas las pistas disponibles.
     * 
     * @return List<PistaDTO> Lista de pistas
     */
    public List<PistaDTO> listarPistas() {
        // Obtener todas las pistas a través del DAO
        return daoPista.listarPistas();
    }

    /**
     * Método para buscar una pista por nombre.
     * 
     * @param nombre Nombre de la pista a buscar
     * @return PistaDTO Pista encontrada
     */
    public PistaDTO buscarPistaPorNombre(String nombre) {
        // Buscar la pista por nombre a través del DAO
        return daoPista.findPistaByNombre(nombre);
    }

    /**
     * Método para actualizar los datos de una pista.
     * 
     * @param pista PistaDTO con los datos actualizados
     * @return int Código de respuesta (0: éxito, 1: error)
     */
    public boolean actualizarPista(PistaDTO pista) {
        // Actualizar la pista a través del DAO
        return daoPista.updatePista(pista);
    }

    /**
     * Método para eliminar una pista.
     * 
     * @param idPista ID de la pista a eliminar
     * @return int Código de respuesta (0: éxito, 1: error)
     */
    public boolean eliminarPista(String nombrePista) {
        // Eliminar la pista a través del DAO
        return daoPista.deletePista(nombrePista);
    }

    /**
     * Método para asociar un material a una pista, si es posible.
     * 
     * @param pista PistaDTO a la que se quiere asociar el material
     * @param material MaterialDTO a asociar
     * @return boolean True si la asociación fue exitosa, False si no.
     */
    public boolean asociarMaterialAPista(PistaDTO pista, MaterialDTO material) {
        // Comprobar si el material está disponible para asociarse a la pista
        if (material.getEstadoMaterial() != MaterialDTO.EstadoMaterial.DISPONIBLE) {
            System.out.println("El material no está disponible.");
            return false;
        }
        
        // Verificar que la pista está disponible
        if (!pista.isDisponible()) {
            System.out.println("La pista no está disponible.");
            return false;
        }
        
        // Asociar el material a la pista (a través del método en la PistaDTO)
        if (pista.asociarMaterial(material)) {
            material.setEstadoMaterial(MaterialDTO.EstadoMaterial.RESERVADO);
            // Actualizar el estado del material en el sistema
            daoMaterial.updateMaterial(material);
            System.out.println("Material asociado a la pista con éxito.");
            return true;
        } else {
            System.out.println("No se pudo asociar el material a la pista.");
            return false;
        }
    }

    /**
     * Método para listar las pistas que no están disponibles.
     * 
     * @return List<PistaDTO> Lista de pistas no disponibles
     */
    public List<PistaDTO> listarPistasNoDisponibles() {
        List<PistaDTO> pistasNoDisponibles = new ArrayList<>();
        
        // Recorrer las pistas y verificar si no están disponibles
        for (PistaDTO pista : daoPista.listarPistas()) {
            if (!pista.isDisponible()) {
                pistasNoDisponibles.add(pista);
            }
        }
        
        return pistasNoDisponibles;
    }

    /**
     * Método para buscar pistas libres según el número de jugadores y tipo de pista.
     * 
     * @param numeroJugadores Número de jugadores que caben en la pista
     * @param esInterior Si la pista debe ser interior o exterior
     * @return List<PistaDTO> Lista de pistas libres
     */
    public List<PistaDTO> buscarPistasLibres(int numeroJugadores, boolean esInterior) {
        List<PistaDTO> pistasLibres = new ArrayList<>();

        // Verificar pistas disponibles y cumplir requisitos de capacidad y tipo
        for (PistaDTO pista : daoPista.listarPistas()) {
            if (pista.isDisponible() && pista.getMaxJugadores() >= numeroJugadores && pista.isInterior() == esInterior) {
                pistasLibres.add(pista);
            }
        }

        return pistasLibres;
    }
}
