package es.uco.pw.display.main;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.MaterialDTO.EstadoMaterial;
import es.uco.pw.business.DTOs.MaterialDTO.TipoMaterial;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;
import es.uco.pw.business.Gestores.GestorPistas;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 08-10-2024
 *  @version 1.0
 */

/**
 * Menú de Gestión de Pistas dentro del sistema.
 */
public class MenuPistas {

    private GestorPistas gestor;
    private Scanner sc;

    /**
     * Constructor de la clase MenuPistas.
     */
    public MenuPistas() {
        this.gestor = new GestorPistas();
        this.sc = new Scanner(System.in);
    }

    /**
     * Muestra el Menú Pistas.
     */
    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
        	System.out.println("\n────────────────────────────────────────");
            System.out.println("======= Menú de Gestión de Pistas ======");
            System.out.println("────────────────────────────────────────");

            System.out.println("■ GESTIÓN DE PISTAS");
            System.out.println("  1. Crear Pista");
            System.out.println("  2. Mostrar todas las Pistas");
            System.out.println("  3. Buscar Pista por Nombre");
            System.out.println("  4. Listar Pistas No Disponibles");
            System.out.println("  5. Buscar Pistas Libres");

            System.out.println("────────────────────────────────────────");

            System.out.println("■ GESTIÓN DE MATERIALES");
            System.out.println("  6. Crear Material");
            System.out.println("  7. Asociar Material a Pista");

            System.out.println("────────────────────────────────────────");
            System.out.println("  8. Salir");
            System.out.println("────────────────────────────────────────");
            
            System.out.print("\n Ingrese su opción: ");

            try {
                int opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        crearPista();
                        break;
                    case 2:
                        mostrarTodasLasPistas();
                        break;
                    case 3:
                        buscarPistaPorNombre();
                        break;
                    case 4:
                        listarPistasNoDisponibles();
                        break;
                    case 5:
                        buscarPistasLibres();
                        break;
                    case 6:
                        crearMaterial();
                        break;
                    case 7:
                        asociarMaterialAPista();
                        break;
                    case 8:
                        salir = true;
                        System.out.println("\nSaliendo del menú...");
                        break;
                    default:
                        System.out.println("ERROR! Opción no válida. Por favor intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
                sc.nextLine();
            }
        }
    }

    /**
     * Crea la pista.
     */
    private void crearPista() {
        System.out.print("\n Ingrese el nombre de la pista: ");
        String nombre = sc.nextLine();

        System.out.print("¿Está disponible? (true/false): ");
        boolean disponible = sc.nextBoolean();

        System.out.print("¿Es interior? (true/false): ");
        boolean esInterior = sc.nextBoolean();

        System.out.println("Seleccione el tamaño de la pista:");
        for (TamanoPista tamano : TamanoPista.values()) {
            System.out.println("- " + tamano);
        }
        TamanoPista tamanoPista = TamanoPista.valueOf(sc.next().toUpperCase());

        System.out.print("Ingrese el máximo de jugadores permitidos: ");
        int maxJugadores = sc.nextInt();
        sc.nextLine();

        // Crear objeto PistaDTO
        PistaDTO nuevaPista = new PistaDTO(nombre, disponible, esInterior, tamanoPista, maxJugadores);

        // Crear pista utilizando el GestorPistasDAO
        if (gestor.crearPista(nuevaPista)) {
            System.out.println("Pista creada exitosamente.");
        } else {
            System.out.println("¡ERROR! Error al crear la pista.");
        }
    }

    /**
     * Crea el material.
     */
    private void crearMaterial() {
        System.out.print("\n Ingrese el ID del material: ");
        int idMaterial = sc.nextInt();
        sc.nextLine();

        System.out.println("Seleccione el tipo de material:");
        for (TipoMaterial tipo : TipoMaterial.values()) {
            System.out.println("- " + tipo);
        }
        TipoMaterial tipoMaterial = TipoMaterial.valueOf(sc.nextLine().toUpperCase());

        System.out.print("¿Es para uso interior? (true/false): ");
        boolean usoInterior = sc.nextBoolean();

        System.out.println("Seleccione el estado del material:");
        for (EstadoMaterial estado : EstadoMaterial.values()) {
            System.out.println("- " + estado);
        }
        EstadoMaterial estadoMaterial = EstadoMaterial.valueOf(sc.next().toUpperCase());
        sc.nextLine();

        // Crear objeto MaterialDTO
        MaterialDTO nuevoMaterial = new MaterialDTO(idMaterial, tipoMaterial, usoInterior, estadoMaterial);

        // Crear material utilizando el gestor
        if (gestor.crearMaterial(nuevoMaterial)) {
            System.out.println("Material creado exitosamente.");
        } else {
            System.out.println("¡ERROR! Error al crear el material.");
        }
    }

    /**
     * Asocia el material a la pista.
     */
    private void asociarMaterialAPista() {
        System.out.print("\n Ingrese el nombre de la pista a la que desea asociar el material: ");
        String nombrePista = sc.nextLine();
        PistaDTO pista = gestor.buscarPistaPorNombre(nombrePista);

        if (pista == null) {
            System.out.println(" ¡ERROR! Pista no encontrada.");
            return;
        }

        System.out.print("Ingrese el ID del material a asociar: ");
        int idMaterial = sc.nextInt();
        sc.nextLine();  // Limpiar buffer de entrada

        MaterialDTO material = gestor.getDaoMaterial().findMaterialById(idMaterial);

        if (material == null) {
            System.out.println("¡ERROR! Material no encontrado.");
            return;
        }

        // Intentar asociar el material a la pista
        if (gestor.asociarMaterialAPista(pista.getNombre(), material.getIdMaterial())) {
            System.out.println("Material asociado exitosamente.");
        } else {
            System.out.println("¡ERROR! No se pudo asociar el material a la pista.");
        }
    }

    /**
     * Método para listar y mostrar las pistas que no están disponibles.
     */
    private void listarPistasNoDisponibles() {
        List<PistaDTO> pistasNoDisponibles = gestor.listarPistasNoDisponibles();
        if (pistasNoDisponibles.isEmpty()) {
            System.out.println("No hay pistas no disponibles.");
        } else {
            for (PistaDTO pista : pistasNoDisponibles) {
                System.out.println(pista);
                List<MaterialDTO> materiales = gestor.obtenerMaterialesDePista(pista.getNombre());
                if (materiales.isEmpty()) {
                    System.out.println("No hay materiales asociados a esta pista.");
                } else {
                    System.out.println("  Materiales asociados:");
                    for (MaterialDTO material : materiales) {
                        System.out.println("    " + material);
                    }
                }
            }
        }
    }

    /**
     * Método para buscar y mostrar las pistas que están libres.
     */
    private void buscarPistasLibres() {
        System.out.print("\n Ingrese el número mínimo de jugadores: ");
        int numeroJugadores = sc.nextInt();
        System.out.print("¿Es interior? (true/false): ");
        boolean esInterior = sc.nextBoolean();
        sc.nextLine();

        List<PistaDTO> pistasLibres = gestor.buscarPistasLibres(numeroJugadores, esInterior);
        if (pistasLibres.isEmpty()) {
            System.out.println("No hay pistas libres que cumplan con los requisitos.");
        } else {
            for (PistaDTO pista : pistasLibres) {
                System.out.println(pista);
                List<MaterialDTO> materiales = gestor.obtenerMaterialesDePista(pista.getNombre());
                if (materiales.isEmpty()) {
                    System.out.println("  No hay materiales asociados a esta pista.");
                } else {
                    System.out.println("  Materiales asociados:");
                    for (MaterialDTO material : materiales) {
                        System.out.println("    " + material);
                    }
                }
            }
        }
    }

    /**
     * Muestra todas las pistas.
     */
    private void mostrarTodasLasPistas() {
        List<PistaDTO> todasLasPistas = gestor.listarPistas();
        if (todasLasPistas.isEmpty()) {
            System.out.println("No hay pistas disponibles.");
        } else {
            for (PistaDTO pista : todasLasPistas) {
                System.out.println(pista);
                List<MaterialDTO> materiales = gestor.obtenerMaterialesDePista(pista.getNombre());
                if (materiales.isEmpty()) {
                    System.out.println("  No hay materiales asociados a esta pista.");
                } else {
                    System.out.println("  Materiales asociados:");
                    for (MaterialDTO material : materiales) {
                        System.out.println("    " + material);
                    }
                }
            }
        }
    }

    /**
     * Busca una pista por su nombre y muestra los detalles.
     */
    private void buscarPistaPorNombre() {
        System.out.print("\n Ingrese el nombre de la pista que desea buscar: ");
        String nombrePista = sc.nextLine();
        
        PistaDTO pista = gestor.buscarPistaPorNombre(nombrePista);

        if (pista == null) {
            System.out.println("No se encontró una pista con el nombre ingresado.");
        } else {
            System.out.println("Pista encontrada: ");
            System.out.println(pista);
            List<MaterialDTO> materiales = gestor.obtenerMaterialesDePista(pista.getNombre());
            if (materiales.isEmpty()) {
                System.out.println("  No hay materiales asociados a esta pista.");
            } else {
                System.out.println("  Materiales asociados:");
                for (MaterialDTO material : materiales) {
                    System.out.println("    " + material);
                }
            }
        }
    }

    /**
     * Método principal que inicia la ejecución del programa.
     */
    public static void main(String[] args) {
        MenuPistas menu = new MenuPistas();
        menu.mostrarMenu();
    }
}
