package es.uco.pw.gestores;

import java.util.ArrayList;
import java.util.List;

import es.uco.pw.data.Material;
import es.uco.pw.data.Pista;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

/**
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 22-10-2024
 *  @version 1.0
 */

/**
 * Clase que gestiona las pistas y los materiales asociados a ellas.
 * Se encarga de crear pistas, asignar materiales, listar pistas no disponibles y 
 * buscar pistas libres en función del número de jugadores.
 */
public class GestorPistas {
    
    private List<Pista> pistas;
    private List<Material> materiales;
    private static final String RUTA_ARCHIVO_PISTAS = "src/es/uco/pw/files/pistas.txt";
    private static final String RUTA_ARCHIVO_MATERIALES = "src/es/uco/pw/files/materiales.txt";

    /**
     * Constructor de la clase GestorPistas.
     */
    public GestorPistas() {
        this.pistas = new ArrayList<>();
        this.materiales = new ArrayList<>();
        cargarPistasDesdeArchivo();
        cargarMaterialesDesdeArchivo();
    }

    /**
     * Método para crear una nueva pista y guardarla en el archivo pistas.txt.
     * 
     * @param nombre Nombre de la pista
     * @param disponible Estado de la pista (disponible o no)
     * @param esInterior Tipo de pista (interior/exterior)
     * @param tamanoPista Tamaño de la pista
     * @param maxJugadores Máximo de jugadores permitidos
     */
    public void crearPista(String nombre, boolean disponible, boolean esInterior, Pista.TamanoPista tamanoPista, int maxJugadores) {
        Pista nuevaPista = new Pista(nombre, disponible, esInterior, tamanoPista, maxJugadores);
        pistas.add(nuevaPista);
        guardarPistaEnArchivo(nuevaPista);
    }

    /**
     * Método para guardar una pista en el archivo pistas.txt.
     * 
     * @param pista Pista a guardar
     */
    private void guardarPistaEnArchivo(Pista pista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO_PISTAS, true))) {
            writer.write(pista.getNombre() + ";" + pista.isDisponible() + ";" + pista.isInterior() + ";" + pista.getTamanoPista() + ";" + pista.getMaxJugadores());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para cargar las pistas desde el archivo pistas.txt.
     */
    private void cargarPistasDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_PISTAS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                String nombre = datos[0];
                boolean disponible = Boolean.parseBoolean(datos[1]);
                boolean esInterior = Boolean.parseBoolean(datos[2]);
                Pista.TamanoPista tamanoPista = Pista.TamanoPista.valueOf(datos[3]);
                int maxJugadores = Integer.parseInt(datos[4]);
                
                Pista pista = new Pista(nombre, disponible, esInterior, tamanoPista, maxJugadores);
                pistas.add(pista);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para crear un nuevo material y guardarlo en el archivo materiales.txt.
     * 
     * @param idMaterial Identificador del material
     * @param tipoMaterial Tipo del material
     * @param usoInterior Indica si es para uso interior
     * @param estadoMaterial Estado del material
     */
    public void crearMaterial(int idMaterial, Material.TipoMaterial tipoMaterial, boolean usoInterior, Material.EstadoMaterial estadoMaterial) {
        Material nuevoMaterial = new Material(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
        materiales.add(nuevoMaterial);
        guardarMaterialEnArchivo(nuevoMaterial);
    }

    /**
     * Método para guardar un material en el archivo materiales.txt.
     * 
     * @param material Material a guardar
     */
    private void guardarMaterialEnArchivo(Material material) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO_MATERIALES, true))) {
            writer.write(material.getIdMaterial() + ";" + material.getTipoMaterial() + ";" + material.getUsoInterior() + ";" + material.getEstadoMaterial());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para cargar los materiales desde el archivo materiales.txt.
     */
    private void cargarMaterialesDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_MATERIALES))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                int idMaterial = Integer.parseInt(datos[0]);
                Material.TipoMaterial tipoMaterial = Material.TipoMaterial.valueOf(datos[1]);
                boolean usoInterior = Boolean.parseBoolean(datos[2]);
                Material.EstadoMaterial estadoMaterial = Material.EstadoMaterial.valueOf(datos[3]);
                
                Material material = new Material(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
                materiales.add(material);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Método para asociar un material a una pista, cumpliendo las restricciones.
     * 
     * @param pista La pista a la que se desea asociar el material
     * @param material El material a asociar
     * @return true si se asoció correctamente, false en caso contrario
     */
    public boolean asociarMaterialAPista(Pista pista, Material material) {
        // Verifica que la pista esté disponible en el momento de la asociación
        if (!pista.isDisponible()) {
            System.out.println("La pista no está disponible.");
            return false;
        }

        // Verifica si el material ya está asignado a otra pista
        if (materialYaAsignado(material)) {
            System.out.println("El material ya está asociado a otra pista o está en mantenimiento.");
            return false;
        }

        // Verifica la compatibilidad entre material y pista
        if (!esMaterialCompatibleConPista(pista, material)) {
            System.out.println("El material no es compatible con el tipo de pista.");
            return false;
        }

        // Si todo está en orden, realiza la asociación
        if (pista.asociarMaterial(material)) {
        	material.setEstadoMaterial(Material.EstadoMaterial.RESERVADO);
            System.out.println("Material asociado a la pista con éxito.");
            
            
            List<String> lineasArchivo = new ArrayList<>();

            // Leer todas las líneas del archivo y modificar la línea del material
            try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_MATERIALES))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    // Separar la línea para identificar el material
                    String[] partes = linea.split(";");
                    int idMaterialArchivo = Integer.parseInt(partes[0]);

                    // Si es el material que buscamos, cambiamos el estado a RESERVADO
                    if (idMaterialArchivo == material.getIdMaterial()) {
                        partes[3] = Material.EstadoMaterial.RESERVADO.toString();
                        linea = String.join(";", partes);
                    }
                    
                    lineasArchivo.add(linea);
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de materiales: " + e.getMessage());
            }

            // Sobrescribir el archivo con los datos actualizados
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO_MATERIALES))) {
                for (String linea : lineasArchivo) {
                    writer.write(linea);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error al escribir en el archivo de materiales: " + e.getMessage());
            }
            
            return true;
        } else {
            System.out.println("No se pudo asociar el material a la pista.");
            return false;
        }
    }
    /**
     * Verifica si un material ya está asignado a otra pista o en mal estado.
     * 
     * @param material El material a verificar
     * @return true si el material está asignado, false en caso contrario
     */
    private boolean materialYaAsignado(Material material) {
        // Verifica si el estado del material no es DISPONIBLE
        if (material.getEstadoMaterial() != Material.EstadoMaterial.DISPONIBLE) {
            return true;
        }
        for (Pista pista : pistas) {
            if (!pista.isDisponible() || pista.consultarMaterialesDisponibles().contains(material)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si un material es compatible con el tipo de pista.
     * 
     * @param pista La pista a verificar
     * @param material El material a verificar
     * @return true si son compatibles, false en caso contrario
     */
    private boolean esMaterialCompatibleConPista(Pista pista, Material material) {
        // Si la pista es exterior, el material no debe ser de uso interior
    	if((pista.isInterior() && material.getUsoInterior()) || (!pista.isInterior() && !material.getUsoInterior())) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }
    
    /**
     * Método para listar todas las pistas que no están disponibles.
     * 
     * @return Una lista de pistas no disponibles.
     */
    public List<Pista> listarPistasNoDisponibles() {
        List<Pista> pistasNoDisponibles = new ArrayList<>();

        for (Pista pista : pistas) {
            if (!pista.isDisponible()) {
                pistasNoDisponibles.add(pista);
            }
        }

        return pistasNoDisponibles;
    }

    /**
     * Método para imprimir la información de las pistas no disponibles en la consola.
     */
    public void imprimirPistasNoDisponibles() {
        List<Pista> pistasNoDisponibles = listarPistasNoDisponibles();

        if (pistasNoDisponibles.isEmpty()) {
            System.out.println("No hay pistas no disponibles en este momento.");
        } else {
            System.out.println("Pistas no disponibles:");
            System.out.println("----------------------------");
            for (Pista pista : pistasNoDisponibles) {
                System.out.println("Nombre: " + pista.getNombre());
                System.out.println("Disponible: " + (pista.isDisponible() ? "Sí" : "No"));
                System.out.println("Interior: " + (pista.isInterior() ? "Sí" : "No"));
                System.out.println("Tamaño: " + pista.getTamanoPista());
                System.out.println("Máximo de Jugadores: " + pista.getMaxJugadores());
                System.out.println("Materiales Asociados: " + pista.consultarMaterialesDisponibles().size());
                System.out.println("----------------------------");
            }
        }
    }
    

    /**
     * Método para buscar pistas libres que tengan al menos el número de jugadores requerido
     * y del tipo de pista especificado.
     *
     * @param numeroJugadores Número mínimo de jugadores que deben caber en la pista.
     * @param esInterior True si se busca una pista interior, False si es exterior.
     * @return Una lista de pistas libres que cumplen los requisitos.
     */
    public List<Pista> buscarPistasLibres(int numeroJugadores, boolean esInterior) {
        List<Pista> pistasLibres = new ArrayList<>();

        for (Pista pista : pistas) {
            // Comprobar si la pista está disponible y si su capacidad mínima se cumple
            if (pista.isDisponible() && pista.getMaxJugadores() >= numeroJugadores && pista.isInterior() == esInterior) {
                pistasLibres.add(pista);
            }
        }

        return pistasLibres;
    }

    /**
     * Método para imprimir la información de las pistas libres que cumplen los requisitos.
     *
     * @param numeroJugadores Número mínimo de jugadores.
     * @param esInterior True si se busca una pista interior, False si es exterior.
     */
    public void imprimirPistasLibres(int numeroJugadores, boolean esInterior) {
        List<Pista> pistasLibres = buscarPistasLibres(numeroJugadores, esInterior);

        if (pistasLibres.isEmpty()) {
            System.out.println("No hay pistas libres que cumplan con los requisitos especificados.");
        } else {
            System.out.println("Pistas libres disponibles para " + numeroJugadores + " jugadores:");
            System.out.println("----------------------------");
            for (Pista pista : pistasLibres) {
                System.out.println("Nombre: " + pista.getNombre());
                System.out.println("Disponible: " + (pista.isDisponible() ? "Sí" : "No"));
                System.out.println("Interior: " + (pista.isInterior() ? "Sí" : "No"));
                System.out.println("Tamaño: " + pista.getTamanoPista());
                System.out.println("Máximo de Jugadores: " + pista.getMaxJugadores());
                System.out.println("Materiales Asociados: " + pista.consultarMaterialesDisponibles().size());
                System.out.println("----------------------------");
            }
        }
    }
    
    /**
     * Método para imprimir la información de todas las pistas.
     */
    public void imprimirTodasLasPistas() {
        if (pistas.isEmpty()) {
            System.out.println("No hay pistas registradas.");
        } else {
            System.out.println("Lista de todas las pistas:");
            System.out.println("----------------------------");
            for (Pista pista : pistas) {
                System.out.println("Nombre: " + pista.getNombre());
                System.out.println("Disponible: " + (pista.isDisponible() ? "Sí" : "No"));
                System.out.println("Interior: " + (pista.isInterior() ? "Sí" : "No"));
                System.out.println("Tamaño: " + pista.getTamanoPista());
                System.out.println("Máximo de Jugadores: " + pista.getMaxJugadores());
                System.out.println("Materiales Asociados: " + pista.consultarMaterialesDisponibles().size());
                System.out.println("----------------------------");
            }
        }
    }
    
    /**
     * Método para obtener la lista de materiales.
     * 
     * @return Lista de materiales.
     */
    public List<Material> getMateriales() {
        return this.materiales;
    }
    /**
     * Lista todas las pistas.
     * 
     * @return Lista de pistas.
     */
    public List<Pista> listarTodasLasPistas() {
        return pistas;
    }

}