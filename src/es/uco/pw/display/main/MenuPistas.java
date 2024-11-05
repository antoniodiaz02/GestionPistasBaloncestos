package es.uco.pw.display.main;

import java.util.InputMismatchException;
import java.util.Scanner;

import es.uco.pw.data.dao.GestorPistasDAO;
import es.uco.pw.business.dto.MaterialDTO;
import es.uco.pw.business.dto.PistaDTO;
import es.uco.pw.business.dto.MaterialDTO.EstadoMaterial;
import es.uco.pw.business.dto.MaterialDTO.TipoMaterial;
import es.uco.pw.business.dto.PistaDTO.TamanoPista;

/**
* Menu Pistas dentro del sistema.
 */
public class MenuPistas {

    private GestorPistasDAO gestor;
    private Scanner sc;

    /**
    * Constructor de la clase MenuPistas
     */
    public MenuPistas() {
        this.gestor = new GestorPistasDAO();
        this.sc = new Scanner(System.in);
    }

    /**
     * Muestra el Menú Pistas.
     */
    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== Menú de Gestión de Pistas =====");
            System.out.println("  1. Crear Pista");
            System.out.println("  2. Crear Material");
            System.out.println("  3. Asociar Material a Pista");
            System.out.println("  4. Listar Pistas No Disponibles");
            System.out.println("  5. Buscar Pistas Libres");
            System.out.println("  6. Mostrar todas las Pistas");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        crearPista();
                        break;
                    case 2:
                        crearMaterial();
                        break;
                    case 3:
                        asociarMaterialAPista();
                        break;
                    case 4:
                        listarPistasNoDisponibles();
                        break;
                    case 5:
                        buscarPistasLibres();
                        break;
                    case 6:
                        mostrarTodasLasPistas();
                        break;
                    case 7:
                        salir = true;
                        System.out.println("Saliendo del menú...");
                        break;
                    default:
                        System.out.println(" ERROR! Opción no válida. Por favor intente de nuevo.");
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
        System.out.print("Ingrese el nombre de la pista: ");
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

        gestor.crearPista(nombre, disponible, esInterior, tamanoPista, maxJugadores);
        System.out.println("Pista creada exitosamente.");
    }

    /**
     * Crea el material.
     */
    private void crearMaterial() {
        System.out.print("Ingrese el ID del material: ");
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

        gestor.crearMaterial(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
        System.out.println("Material creado exitosamente.");
    }

    
    /**
     * Asocia el material a la pista.
     */
    private void asociarMaterialAPista() {
        System.out.print("Ingrese el nombre de la pista a la que desea asociar el material: ");
        String nombrePista = sc.nextLine();
        PistaDTO pista = null;

        // Buscamos en todas las pistas (disponibles y no disponibles)
        for (PistaDTO p : gestor.listarTodasLasPistas()) {
            if (p.getNombre().equalsIgnoreCase(nombrePista)) {
                pista = p;
                break;
            }
        }

        if (pista == null) {
            System.out.println("Pista no encontrada.");
            return;
        }

        System.out.print("Ingrese el ID del material a asociar: ");
        int idMaterial = sc.nextInt();
        MaterialDTO material = null;

        for (MaterialDTO m : gestor.getMateriales()) {
            if (m.getIdMaterial() == idMaterial) {
                material = m;
                break;
            }
        }

        if (material == null) {
            System.out.println("Material no encontrado.");
            return;
        }

        // Intenta asociar el material a la pista seleccionada
        if (gestor.asociarMaterialAPista(pista, material)) {
            System.out.println("Material asociado exitosamente.");
        } else {
            System.out.println("No se pudo asociar el material a la pista.");
        }
    }

    /**
     * Método para listar y mostrar las pistas que no están disponibles.
     */
    private void listarPistasNoDisponibles() {
        // Delegamos la impresión al método imprimirPistasNoDisponibles del gestor
        gestor.imprimirPistasNoDisponibles();
    }

    /**
     * Método para buscar y mostrar las pistas que están libres.
     */
    private void buscarPistasLibres() {
        System.out.print("Ingrese el número mínimo de jugadores: ");
        int numeroJugadores = sc.nextInt();
        System.out.print("¿Es interior? (true/false): ");
        boolean esInterior = sc.nextBoolean();
        sc.nextLine();

        // Llamamos al método imprimirPistasLibres del gestor para realizar la búsqueda e impresión
        gestor.imprimirPistasLibres(numeroJugadores, esInterior);
    }

    /**
     * Muestra todas las pitas
     */
    private void mostrarTodasLasPistas() {
        gestor.imprimirTodasLasPistas();
    }

    /**
     * Método principal que inicia la ejecución del programa.
     * Este método crea una instancia de la clase MenuPistas y llama
     * al método mostrarMenu para presentar al usuario el menú.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        MenuPistas menu = new MenuPistas();
        menu.mostrarMenu();
    }
}
