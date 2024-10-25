package es.uco.pw.display.main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== Menú Principal =====");
            System.out.println("1. Menú de Usuarios");
            System.out.println("2. Menú de Pistas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    // Llamar al menú de usuarios
                    MenuUsuarios menuUsuarios = new MenuUsuarios();
                    menuUsuarios.mostrarMenu();
                    break;
                case 2:
                    // Llamar al menú de gestión de pistas
                    MenuPistas menuPistas = new MenuPistas();
                    menuPistas.mostrarMenu();
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 3);

        sc.close();
    }
}