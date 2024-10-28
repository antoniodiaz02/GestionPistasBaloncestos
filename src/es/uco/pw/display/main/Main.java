package es.uco.pw.display.main;

import java.util.Scanner;

/**
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 08-10-2024
 *  @version 1.0
 */

/**
 * Clase Main dentro del sistema.
 */
public class Main {

	/**
     * Constructor por defecto de la clase Main.
     * Este constructor no realiza ninguna acción específica,
     * ya que la lógica principal se encuentra en el método main.
     */
    public Main() {
        // Constructor por defecto
    }
	
	/**
	 * Método principal que inicia la ejecución del programa.
	 * Este método se encarga de recibir la entrada del usuario y gestiona la interacción con el sistema.
	 * @param args Argumentos de línea de comandos que pueden ser utilizados
	 *             para inicializar el programa (no se utilizan en esta implementación).
	 */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 99;

        while (opcion != 4) {
            System.out.println("\n===== Menú Principal =====");
            System.out.println("1. Menú de Usuarios");
            System.out.println("2. Menú de Pistas");
            System.out.println("3. Menú de Reservas");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    // OPCION 1: Llamar al menú de usuarios
                    MenuUsuarios menuUsuarios = new MenuUsuarios();
                    menuUsuarios.mostrarMenu();
                    break;
                    
                case 2:
                    // OPCION 2: Llamar al menú de gestión de pistas
                    MenuPistas menuPistas = new MenuPistas();
                    menuPistas.mostrarMenu();
                    break;
                    
                case 3:
                	// OPCION 3: LLamar al menú de gestión de reservas
                    MenuReservas menuReservas = new MenuReservas();
                    menuReservas.mostrarMenu(); // Llamar al método mostrarMenu de MenuReservas
                    break;
                	
                case 4:
                	// OPCION 4: Salir del Menu
                	System.out.println("\n Saliendo del programa...");
                	break;
                default:
                    System.out.println("\n ERROR! Opción no válida. Por favor intente de nuevo.");
            }
        }
        sc.close();
    }
}